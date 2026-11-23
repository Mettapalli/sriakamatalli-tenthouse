package com.tenthouse.controller;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@RestController @RequestMapping("/api/admin/reports")
public class AdminReportController {
    @PersistenceContext private EntityManager em;

    @GetMapping("/financial-summary")
    public Map<String,Object> summary(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate startDate,
                                      @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endDate) {
        BigDecimal billed = bd(em.createNativeQuery("SELECT COALESCE(SUM(final_amount), 0) FROM bookings WHERE status <> 'CANCELLED' AND start_date BETWEEN :s AND :e").setParameter("s", startDate).setParameter("e", endDate).getSingleResult());
        BigDecimal collected = bd(em.createNativeQuery("SELECT COALESCE(SUM(amount), 0) FROM payments WHERE payment_date BETWEEN :s AND :e").setParameter("s", startDate).setParameter("e", endDate).getSingleResult());
        BigDecimal expenses = bd(em.createNativeQuery("SELECT COALESCE(SUM(amount), 0) FROM expenses WHERE expense_date BETWEEN :s AND :e").setParameter("s", startDate).setParameter("e", endDate).getSingleResult());
        BigDecimal outstanding = bd(em.createNativeQuery("SELECT COALESCE(SUM(balance_amount), 0) FROM bookings WHERE status <> 'CANCELLED'").getSingleResult());
        long count = ((Number) em.createNativeQuery("SELECT COUNT(*) FROM bookings WHERE status <> 'CANCELLED' AND start_date BETWEEN :s AND :e").setParameter("s", startDate).setParameter("e", endDate).getSingleResult()).longValue();
        Map<String,Object> out = new LinkedHashMap<>();
        out.put("bookingsCount", count); out.put("billedRevenue", billed); out.put("collectedPayments", collected);
        out.put("totalExpenses", expenses); out.put("estimatedProfit", collected.subtract(expenses)); out.put("outstandingBalances", outstanding);
        return out;
    }

    @GetMapping("/outstanding-customers") @SuppressWarnings("unchecked")
    public List<Map<String,Object>> outstanding() {
        List<Object[]> rows = em.createNativeQuery("SELECT c.full_name, c.mobile_number, b.booking_number, b.start_date, b.final_amount, b.advance_paid, b.balance_amount FROM bookings b JOIN customers c ON c.id = b.customer_id WHERE b.status <> 'CANCELLED' AND b.balance_amount > 0 ORDER BY b.balance_amount DESC").getResultList();
        List<Map<String,Object>> l = new ArrayList<>();
        for (Object[] r : rows) {
            Map<String,Object> m = new LinkedHashMap<>();
            m.put("customerName", r[0]); m.put("mobileNumber", r[1]); m.put("bookingNumber", r[2]);
            m.put("eventDate", r[3]); m.put("finalAmount", r[4]); m.put("paidAmount", r[5]); m.put("balanceAmount", r[6]); l.add(m);
        }
        return l;
    }

    @GetMapping("/top-rented-items") @SuppressWarnings("unchecked")
    public List<Map<String,Object>> topItems() {
        List<Object[]> rows = em.createNativeQuery("SELECT i.item_code, i.name, COALESCE(SUM(bi.quantity), 0), COALESCE(SUM(bi.line_total), 0) FROM booking_items bi JOIN bookings b ON b.id = bi.booking_id JOIN items i ON i.id = bi.item_id WHERE b.status <> 'CANCELLED' GROUP BY i.id, i.item_code, i.name ORDER BY SUM(bi.line_total) DESC LIMIT 25").getResultList();
        List<Map<String,Object>> l = new ArrayList<>();
        for (Object[] r : rows) {
            Map<String,Object> m = new LinkedHashMap<>();
            m.put("itemCode", r[0]); m.put("itemName", r[1]); m.put("totalRentedQty", r[2]); m.put("totalRevenue", r[3]); l.add(m);
        }
        return l;
    }

    private BigDecimal bd(Object v) { return v == null ? BigDecimal.ZERO : new BigDecimal(v.toString()); }
}