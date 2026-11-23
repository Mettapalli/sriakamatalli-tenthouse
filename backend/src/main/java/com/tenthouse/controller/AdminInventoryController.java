package com.tenthouse.controller;
import com.tenthouse.entity.*;
import com.tenthouse.repository.*;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/admin/inventory")
public class AdminInventoryController {
    private final ItemRepository itemRepo; private final StockTransactionRepository txRepo; private final UserRepository userRepo;
    public AdminInventoryController(ItemRepository itemRepo, StockTransactionRepository txRepo, UserRepository userRepo) {
        this.itemRepo = itemRepo; this.txRepo = txRepo; this.userRepo = userRepo;
    }
    @GetMapping("/transactions") public List<Map<String,Object>> txs() {
        List<Map<String,Object>> out = new ArrayList<>();
        for (StockTransaction t : txRepo.findAll(Sort.by(Sort.Direction.DESC, "id")).stream().limit(100).toList()) {
            Map<String,Object> m = new LinkedHashMap<>();
            m.put("id", t.getId()); m.put("itemCode", t.getItem().getItemCode()); m.put("itemName", t.getItem().getName());
            m.put("transactionType", t.getTransactionType().name()); m.put("quantity", t.getQuantity());
            m.put("remarks", t.getRemarks()); m.put("transactionDate", t.getTransactionDate()); out.add(m);
        }
        return out;
    }
    @PostMapping("/adjust") @Transactional
    public Item adjust(@RequestBody Map<String,Object> b, Authentication a) {
        Item it = itemRepo.findById(Long.valueOf(b.get("itemId").toString())).orElseThrow();
        StockTransaction.TransactionType type = StockTransaction.TransactionType.valueOf(b.get("type").toString());
        int q = Integer.parseInt(b.get("quantity").toString());
        switch (type) {
            case PURCHASED -> it.setTotalQuantity(it.getTotalQuantity() + q);
            case DAMAGED -> it.setDamagedQuantity(Math.min(it.getTotalQuantity(), it.getDamagedQuantity() + q));
            case REPAIRED -> it.setDamagedQuantity(Math.max(0, it.getDamagedQuantity() - q));
            case MISSING -> it.setTotalQuantity(Math.max(0, it.getTotalQuantity() - q));
            default -> throw new IllegalArgumentException("Unsupported type");
        }
        it.setAvailableQuantity(Math.max(0, it.getTotalQuantity() - it.getDamagedQuantity()));
        it.setStatus(it.getAvailableQuantity() > 0 ? Item.ItemStatus.AVAILABLE : Item.ItemStatus.OUT_OF_STOCK);
        Item saved = itemRepo.save(it);
        StockTransaction tx = new StockTransaction(); tx.setItem(saved); tx.setTransactionType(type); tx.setQuantity(q);
        tx.setRemarks(b.getOrDefault("remarks","").toString()); tx.setCreatedBy(userRepo.findByUsername(a.getName()).orElse(null));
        txRepo.save(tx); return saved;
    }
}