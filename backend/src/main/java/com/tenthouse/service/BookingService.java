package com.tenthouse.service;

import com.tenthouse.dto.*;
import com.tenthouse.entity.*;
import com.tenthouse.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.*;
import java.time.LocalDate;
import java.util.*;

@Service
public class BookingService {
    private final BookingRepository bookingRepo;
    private final CustomerRepository custRepo;
    private final EventRepository eventRepo;
    private final ItemRepository itemRepo;
    private final StockTransactionRepository stockRepo;
    private final UserRepository userRepo;

    public BookingService(BookingRepository bookingRepo, CustomerRepository custRepo, EventRepository eventRepo,
                          ItemRepository itemRepo, StockTransactionRepository stockRepo, UserRepository userRepo) {
        this.bookingRepo = bookingRepo; this.custRepo = custRepo; this.eventRepo = eventRepo;
        this.itemRepo = itemRepo; this.stockRepo = stockRepo; this.userRepo = userRepo;
    }

    public int getAvailableForDateRange(Long itemId, LocalDate start, LocalDate end, Long excludeId) {
        Item item = itemRepo.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Item not found"));
        int usable = Math.max(0, item.getTotalQuantity() - item.getDamagedQuantity());
        Integer booked = bookingRepo.sumBookedQtyForDates(itemId, start, end, excludeId);
        return Math.max(0, usable - (booked != null ? booked : 0));
    }

    public List<Map<String, Object>> getAllItemsAvailability(LocalDate start, LocalDate end) {
        List<Map<String, Object>> out = new ArrayList<>();
        for (Item item : itemRepo.findAll()) {
            if (item.getStatus() == Item.ItemStatus.INACTIVE) continue;
            int usable = Math.max(0, item.getTotalQuantity() - item.getDamagedQuantity());
            Integer b = bookingRepo.sumBookedQtyForDates(item.getId(), start, end, null);
            int booked = b != null ? b : 0;
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", item.getId()); m.put("itemCode", item.getItemCode()); m.put("name", item.getName());
            m.put("nameTe", item.getNameTe()); m.put("rentalPrice", item.getRentalPrice());
            m.put("priceUnit", item.getPriceUnit().name()); m.put("totalUsable", usable);
            m.put("alreadyBooked", booked); m.put("availableForDates", Math.max(0, usable - booked));
            out.add(m);
        }
        return out;
    }

    @Transactional
    public Booking createBooking(BookingRequestDto dto, String username) {
        if (dto.getEndDate().isBefore(dto.getStartDate())) throw new IllegalArgumentException("End date cannot be before start date");
        Customer cust = custRepo.findById(dto.getCustomerId()).orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        User admin = userRepo.findByUsername(username).orElse(null);

        Event ev = new Event();
        ev.setCustomer(cust); ev.setEventName(dto.getEventName().trim()); ev.setEventType(dto.getEventType());
        ev.setEventDate(dto.getStartDate()); ev.setEndDate(dto.getEndDate()); ev.setEventAddress(dto.getEventAddress().trim());
        ev.setGuestCount(dto.getGuestCount()); ev.setSpecialInstructions(dto.getSpecialInstructions());
        ev = eventRepo.save(ev);

        Booking bk = new Booking();
        bk.setBookingNumber("BK-" + System.currentTimeMillis());
        bk.setCustomer(cust); bk.setEvent(ev); bk.setStartDate(dto.getStartDate()); bk.setEndDate(dto.getEndDate());
        bk.setStatus(dto.getStatus() != null ? dto.getStatus() : Booking.BookingStatus.CONFIRMED);
        bk.setCreatedBy(admin); bk.setNotes(dto.getNotes());

        boolean reserve = bk.getStatus() == Booking.BookingStatus.CONFIRMED || bk.getStatus() == Booking.BookingStatus.IN_PROGRESS;
        BigDecimal sub = BigDecimal.ZERO;
        for (BookingRequestDto.ItemLine l : dto.getItems()) {
            if (l.getQuantity() <= 0) throw new IllegalArgumentException("Quantity must be > 0");
            Item item = itemRepo.findById(l.getItemId()).orElseThrow(() -> new IllegalArgumentException("Item not found"));
            if (reserve) {
                int avail = getAvailableForDateRange(item.getId(), dto.getStartDate(), dto.getEndDate(), null);
                if (l.getQuantity() > avail) throw new IllegalArgumentException("Insufficient stock for '" + item.getName() + "'. Available: " + avail + ", Requested: " + l.getQuantity());
            }
            BigDecimal up = l.getUnitPrice() != null ? l.getUnitPrice() : item.getRentalPrice();
            BigDecimal lt = up.multiply(BigDecimal.valueOf(l.getQuantity()));
            sub = sub.add(lt);
            BookingItem bi = new BookingItem();
            bi.setBooking(bk); bi.setItem(item); bi.setQuantity(l.getQuantity());
            bi.setUnitPrice(up); bi.setPriceUnit(item.getPriceUnit().name()); bi.setLineTotal(lt);
            bk.getItems().add(bi);
        }

        bk.setItemsSubtotal(sub);
        bk.setTransportationCharge(nz(dto.getTransportationCharge())); bk.setLabourCharge(nz(dto.getLabourCharge()));
        bk.setDecorationCharge(nz(dto.getDecorationCharge())); bk.setInstallationCharge(nz(dto.getInstallationCharge()));
        bk.setGeneratorCharge(nz(dto.getGeneratorCharge())); bk.setOtherCharges(nz(dto.getOtherCharges()));
        BigDecimal gross = sub.add(bk.getTransportationCharge()).add(bk.getLabourCharge()).add(bk.getDecorationCharge())
                .add(bk.getInstallationCharge()).add(bk.getGeneratorCharge()).add(bk.getOtherCharges());
        bk.setDiscountType(dto.getDiscountType()); bk.setDiscountValue(nz(dto.getDiscountValue()));
        BigDecimal disc = BigDecimal.ZERO;
        if (bk.getDiscountType() == Booking.DiscountType.PERCENTAGE) disc = gross.multiply(bk.getDiscountValue()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        else if (bk.getDiscountType() == Booking.DiscountType.FIXED) disc = bk.getDiscountValue();
        if (disc.compareTo(gross) > 0) disc = gross;
        bk.setDiscountAmount(disc);
        BigDecimal fin = gross.subtract(disc); bk.setFinalAmount(fin);
        BigDecimal adv = nz(dto.getAdvancePaid()); bk.setAdvancePaid(adv);
        bk.setBalanceAmount(fin.subtract(adv).max(BigDecimal.ZERO));
        Booking saved = bookingRepo.save(bk);
        if (reserve) saved.getItems().forEach(bi -> logStock(bi.getItem(), saved.getId(), StockTransaction.TransactionType.BOOKED, bi.getQuantity(), "Booked " + saved.getBookingNumber(), admin));
        return saved;
    }

    @Transactional
    public Booking updateStatus(Long id, Booking.BookingStatus newStatus, String username) {
        Booking b = bookingRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        boolean willRes = newStatus == Booking.BookingStatus.CONFIRMED || newStatus == Booking.BookingStatus.IN_PROGRESS;
        boolean wasRes = b.getStatus() == Booking.BookingStatus.CONFIRMED || b.getStatus() == Booking.BookingStatus.IN_PROGRESS;
        if (willRes && !wasRes) {
            for (BookingItem bi : b.getItems()) {
                int avail = getAvailableForDateRange(bi.getItem().getId(), b.getStartDate(), b.getEndDate(), b.getId());
                if (bi.getQuantity() > avail) throw new IllegalArgumentException("Insufficient stock for " + bi.getItem().getName() + " (Available: " + avail + ")");
            }
        }
        b.setStatus(newStatus);
        return bookingRepo.save(b);
    }

    @Transactional
    public Booking completeAndReconcile(Long id, BookingReturnDto ret, String username) {
        Booking b = bookingRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        User admin = userRepo.findByUsername(username).orElse(null);
        if (ret.getLines() != null) {
            for (BookingReturnDto.ReturnLine l : ret.getLines()) {
                BookingItem bi = b.getItems().stream().filter(x -> x.getId().equals(l.getBookingItemId())).findFirst().orElseThrow();
                bi.setReturnedQty(l.getReturnedQty()); bi.setDamagedQty(l.getDamagedQty()); bi.setMissingQty(l.getMissingQty());
                Item it = bi.getItem();
                if (l.getReturnedQty() > 0) logStock(it, b.getId(), StockTransaction.TransactionType.RETURNED, l.getReturnedQty(), "Returned " + b.getBookingNumber(), admin);
                if (l.getDamagedQty() > 0) { it.setDamagedQuantity(it.getDamagedQuantity() + l.getDamagedQty()); logStock(it, b.getId(), StockTransaction.TransactionType.DAMAGED, l.getDamagedQty(), "Damaged " + b.getBookingNumber(), admin); }
                if (l.getMissingQty() > 0) { it.setTotalQuantity(Math.max(0, it.getTotalQuantity() - l.getMissingQty())); logStock(it, b.getId(), StockTransaction.TransactionType.MISSING, l.getMissingQty(), "Missing " + b.getBookingNumber(), admin); }
                it.setAvailableQuantity(Math.max(0, it.getTotalQuantity() - it.getDamagedQuantity()));
                itemRepo.save(it);
            }
        }
        b.setStatus(Booking.BookingStatus.COMPLETED);
        return bookingRepo.save(b);
    }

    private void logStock(Item i, Long bid, StockTransaction.TransactionType t, int q, String r, User u) {
        StockTransaction tx = new StockTransaction(); tx.setItem(i); tx.setBookingId(bid); tx.setTransactionType(t); tx.setQuantity(q); tx.setRemarks(r); tx.setCreatedBy(u); stockRepo.save(tx);
    }
    private BigDecimal nz(BigDecimal v) { return v != null ? v : BigDecimal.ZERO; }
}