package com.tenthouse.service;

import com.tenthouse.dto.ItemRequestDto;
import com.tenthouse.entity.*;
import com.tenthouse.repository.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.*;

@Service
public class ItemService {
    private final ItemRepository itemRepo;
    private final CategoryRepository catRepo;
    private final ItemPriceHistoryRepository priceRepo;
    private final ItemImageRepository imgRepo;
    private final StockTransactionRepository stockRepo;
    private final UserRepository userRepo;
    @Value("${app.upload.dir:uploads}") private String uploadDir;

    public ItemService(ItemRepository itemRepo, CategoryRepository catRepo, ItemPriceHistoryRepository priceRepo,
                       ItemImageRepository imgRepo, StockTransactionRepository stockRepo, UserRepository userRepo) {
        this.itemRepo = itemRepo; this.catRepo = catRepo; this.priceRepo = priceRepo;
        this.imgRepo = imgRepo; this.stockRepo = stockRepo; this.userRepo = userRepo;
    }

    public List<Item> searchItems(Long categoryId, Item.ItemStatus status, String keyword, String sortBy, String dir) {
        String f = switch (sortBy != null ? sortBy : "id") {
            case "rentalPrice", "name", "itemCode", "totalQuantity", "availableQuantity" -> sortBy;
            default -> "id";
        };
        Sort s = "desc".equalsIgnoreCase(dir) ? Sort.by(f).descending() : Sort.by(f).ascending();
        return itemRepo.searchAdminItems(categoryId, status, keyword, s);
    }

    public Item getById(Long id) { return itemRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Item not found")); }

    @Transactional
    public Item createItem(ItemRequestDto dto, String username) {
        Category cat = catRepo.findById(dto.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("Invalid category"));
        String code = (dto.getItemCode() != null && !dto.getItemCode().isBlank()) ? dto.getItemCode().trim().toUpperCase() : nextCode();
        if (itemRepo.existsByItemCode(code)) throw new IllegalArgumentException("Item code already exists: " + code);
        Item item = new Item(); item.setItemCode(code); item.setCategory(cat); applyDto(item, dto);
        Item saved = itemRepo.save(item);
        if (saved.getTotalQuantity() > 0) logStock(saved, StockTransaction.TransactionType.PURCHASED, saved.getTotalQuantity(), "Initial entry", userRepo.findByUsername(username).orElse(null));
        return saved;
    }

    @Transactional
    public Item updateItem(Long id, ItemRequestDto dto, String username) {
        Item item = getById(id);
        Category cat = catRepo.findById(dto.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("Invalid category"));
        BigDecimal oldP = item.getRentalPrice(); String oldU = item.getPriceUnit().name();
        int oldT = item.getTotalQuantity(), oldD = item.getDamagedQuantity();
        item.setCategory(cat);
        if (dto.getItemCode() != null && !dto.getItemCode().isBlank()) {
            String nc = dto.getItemCode().trim().toUpperCase();
            if (!nc.equals(item.getItemCode()) && itemRepo.existsByItemCode(nc)) throw new IllegalArgumentException("Duplicate code");
            item.setItemCode(nc);
        }
        applyDto(item, dto);
        Item saved = itemRepo.save(item);
        User admin = userRepo.findByUsername(username).orElse(null);
        if (oldP.compareTo(saved.getRentalPrice()) != 0 || !oldU.equals(saved.getPriceUnit().name())) {
            ItemPriceHistory h = new ItemPriceHistory();
            h.setItem(saved); h.setOldPrice(oldP); h.setNewPrice(saved.getRentalPrice());
            h.setOldUnit(oldU); h.setNewUnit(saved.getPriceUnit().name()); h.setChangedBy(admin);
            h.setChangeReason(dto.getPriceChangeReason() != null && !dto.getPriceChangeReason().isBlank() ? dto.getPriceChangeReason() : "Manual update");
            priceRepo.save(h);
        }
        if (saved.getTotalQuantity() != oldT) logStock(saved, saved.getTotalQuantity() > oldT ? StockTransaction.TransactionType.PURCHASED : StockTransaction.TransactionType.ADJUSTMENT, Math.abs(saved.getTotalQuantity() - oldT), "Stock updated", admin);
        if (saved.getDamagedQuantity() != oldD) logStock(saved, saved.getDamagedQuantity() > oldD ? StockTransaction.TransactionType.DAMAGED : StockTransaction.TransactionType.REPAIRED, Math.abs(saved.getDamagedQuantity() - oldD), "Damaged updated", admin);
        return saved;
    }

    @Transactional public void deleteItem(Long id) { itemRepo.delete(getById(id)); }
    public List<ItemPriceHistory> getPriceHistory(Long id) { return priceRepo.findByItemIdOrderByChangedAtDesc(id); }

    @Transactional
    public ItemImage uploadImage(Long itemId, MultipartFile file, boolean primary) throws IOException {
        if (file.isEmpty()) throw new IllegalArgumentException("Empty file");
        Item item = getById(itemId);
        String orig = file.getOriginalFilename() != null ? file.getOriginalFilename() : "item.jpg";
        String ext = orig.contains(".") ? orig.substring(orig.lastIndexOf('.')) : ".jpg";
        String fn = "item-" + itemId + "-" + UUID.randomUUID() + ext;
        Path dir = Paths.get(uploadDir, "items").toAbsolutePath().normalize();
        Files.createDirectories(dir);
        Files.copy(file.getInputStream(), dir.resolve(fn), StandardCopyOption.REPLACE_EXISTING);
        if (primary || item.getImages().isEmpty()) { item.getImages().forEach(i -> i.setIsPrimary(false)); primary = true; }
        ItemImage im = new ItemImage(); im.setItem(item); im.setImageUrl("/uploads/items/" + fn); im.setIsPrimary(primary);
        return imgRepo.save(im);
    }

    @Transactional public void deleteImage(Long id) { imgRepo.deleteById(id); }

    public byte[] exportItemsToExcel() throws IOException {
        List<Item> items = itemRepo.findAll(Sort.by("id").ascending());
        try (Workbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sh = wb.createSheet("TentHouseItems");
            String[] hdr = {"Item Code","Category","Item Name (English)","Item Name (Telugu)","Total Qty","Available Qty","Damaged Qty","Rental Price","Price Unit","Purchase Price","Status","Notes"};
            Row hr = sh.createRow(0);
            for (int c = 0; c < hdr.length; c++) hr.createCell(c).setCellValue(hdr[c]);
            int r = 1;
            for (Item i : items) {
                Row row = sh.createRow(r++);
                row.createCell(0).setCellValue(i.getItemCode()); row.createCell(1).setCellValue(i.getCategory().getName());
                row.createCell(2).setCellValue(i.getName()); row.createCell(3).setCellValue(i.getNameTe() != null ? i.getNameTe() : "");
                row.createCell(4).setCellValue(i.getTotalQuantity()); row.createCell(5).setCellValue(i.getAvailableQuantity());
                row.createCell(6).setCellValue(i.getDamagedQuantity()); row.createCell(7).setCellValue(i.getRentalPrice().doubleValue());
                row.createCell(8).setCellValue(i.getPriceUnit().name());
                row.createCell(9).setCellValue(i.getPurchasePrice() != null ? i.getPurchasePrice().doubleValue() : 0.0);
                row.createCell(10).setCellValue(i.getStatus().name()); row.createCell(11).setCellValue(i.getNotes() != null ? i.getNotes() : "");
            }
            wb.write(out); return out.toByteArray();
        }
    }

    @Transactional
    public Map<String, Object> importItemsFromExcel(MultipartFile file, String username) throws IOException {
        int created = 0, updated = 0; List<String> errors = new ArrayList<>();
        User admin = userRepo.findByUsername(username).orElse(null); DataFormatter fmt = new DataFormatter();
        try (Workbook wb = WorkbookFactory.create(file.getInputStream())) {
            Sheet sh = wb.getSheetAt(0);
            for (int i = 1; i <= sh.getLastRowNum(); i++) {
                Row row = sh.getRow(i); if (row == null) continue;
                try {
                    String code = fmt.formatCellValue(row.getCell(0)).trim().toUpperCase();
                    String catName = fmt.formatCellValue(row.getCell(1)).trim();
                    String name = fmt.formatCellValue(row.getCell(2)).trim();
                    if (name.isBlank()) continue;
                    String nameTe = fmt.formatCellValue(row.getCell(3)).trim();
                    int tq = pInt(fmt.formatCellValue(row.getCell(4)), 0);
                    int aq = pInt(fmt.formatCellValue(row.getCell(5)), tq);
                    int dq = pInt(fmt.formatCellValue(row.getCell(6)), 0);
                    BigDecimal pr = pDec(fmt.formatCellValue(row.getCell(7)));
                    Item.PriceUnit un = pUnit(fmt.formatCellValue(row.getCell(8)).trim());
                    Category cat = catRepo.findByNameIgnoreCase(catName.isBlank() ? "Other Accessories" : catName).orElseGet(() -> {
                        Category c = new Category(); c.setName(catName.isBlank() ? "Other Accessories" : catName); return catRepo.save(c);
                    });
                    Optional<Item> ex = !code.isBlank() ? itemRepo.findByItemCode(code) : Optional.empty();
                    Item it = ex.orElseGet(Item::new); boolean isNew = ex.isEmpty();
                    if (isNew) it.setItemCode(!code.isBlank() ? code : nextCode());
                    it.setCategory(cat); it.setName(name); it.setNameTe(nameTe);
                    it.setTotalQuantity(tq); it.setDamagedQuantity(Math.min(dq, tq));
                    it.setAvailableQuantity(Math.min(aq, Math.max(0, tq - it.getDamagedQuantity())));
                    it.setRentalPrice(pr); it.setPriceUnit(un);
                    it.setStatus(it.getAvailableQuantity() > 0 ? Item.ItemStatus.AVAILABLE : Item.ItemStatus.OUT_OF_STOCK);
                    Item saved = itemRepo.save(it);
                    if (isNew) { created++; if (saved.getTotalQuantity() > 0) logStock(saved, StockTransaction.TransactionType.PURCHASED, saved.getTotalQuantity(), "Excel import", admin); }
                    else updated++;
                } catch (Exception e) { errors.add("Row " + (i + 1) + ": " + e.getMessage()); }
            }
        }
        return Map.of("created", created, "updated", updated, "errors", errors);
    }

    private void applyDto(Item item, ItemRequestDto dto) {
        item.setName(dto.getName().trim()); item.setNameTe(dto.getNameTe() != null ? dto.getNameTe().trim() : null);
        item.setDescription(dto.getDescription());
        int t = dto.getTotalQuantity(), d = Math.min(dto.getDamagedQuantity(), t), maxA = Math.max(0, t - d);
        int a = dto.getAvailableQuantity() != null ? Math.min(dto.getAvailableQuantity(), maxA) : maxA;
        item.setTotalQuantity(t); item.setDamagedQuantity(d); item.setAvailableQuantity(a);
        item.setRentalPrice(dto.getRentalPrice()); item.setPriceUnit(dto.getPriceUnit());
        item.setCustomUnitLabel(dto.getCustomUnitLabel()); item.setPurchasePrice(dto.getPurchasePrice());
        item.setStatus(dto.getStatus() != null ? dto.getStatus() : Item.ItemStatus.AVAILABLE); item.setNotes(dto.getNotes());
    }
    private void logStock(Item i, StockTransaction.TransactionType t, int q, String r, User u) {
        StockTransaction tx = new StockTransaction(); tx.setItem(i); tx.setTransactionType(t); tx.setQuantity(q); tx.setRemarks(r); tx.setCreatedBy(u); stockRepo.save(tx);
    }
    private String nextCode() { long n = itemRepo.count() + 1; String c; do { c = String.format("TH-%03d", n++); } while (itemRepo.existsByItemCode(c)); return c; }
    private int pInt(String v, int f) { try { return v.isBlank() ? f : (int) Double.parseDouble(v); } catch (Exception e) { return f; } }
    private BigDecimal pDec(String v) { try { return v.isBlank() ? BigDecimal.ZERO : new BigDecimal(v); } catch (Exception e) { return BigDecimal.ZERO; } }
    private Item.PriceUnit pUnit(String v) { try { return v.isBlank() ? Item.PriceUnit.PER_DAY : Item.PriceUnit.valueOf(v.toUpperCase()); } catch (Exception e) { return Item.PriceUnit.PER_DAY; } }
}