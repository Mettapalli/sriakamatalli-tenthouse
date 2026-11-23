package com.tenthouse.controller;
import com.tenthouse.dto.DashboardSummaryDto;
import jakarta.persistence.*;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController @RequestMapping("/api/admin/dashboard")
public class DashboardController {
    @PersistenceContext private EntityManager em;
    @GetMapping("/summary")
    public DashboardSummaryDto summary() {
        long items = ((Number) em.createNativeQuery("SELECT COUNT(*) FROM items").getSingleResult()).longValue();
        long cats = ((Number) em.createNativeQuery("SELECT COUNT(*) FROM categories WHERE is_active = 1").getSingleResult()).longValue();
        long custs = ((Number) em.createNativeQuery("SELECT COUNT(*) FROM customers").getSingleResult()).longValue();
        long bks = ((Number) em.createNativeQuery("SELECT COUNT(*) FROM bookings").getSingleResult()).longValue();
        long today = ((Number) em.createNativeQuery("SELECT COUNT(*) FROM events WHERE CURDATE() BETWEEN event_date AND end_date").getSingleResult()).longValue();
        long up = ((Number) em.createNativeQuery("SELECT COUNT(*) FROM events WHERE event_date > CURDATE()").getSingleResult()).longValue();
        BigDecimal inc = bd(em.createNativeQuery("SELECT COALESCE(SUM(amount), 0) FROM payments WHERE MONTH(payment_date)=MONTH(CURDATE()) AND YEAR(payment_date)=YEAR(CURDATE())").getSingleResult());
        BigDecimal pend = bd(em.createNativeQuery("SELECT COALESCE(SUM(balance_amount), 0) FROM bookings WHERE status NOT IN ('CANCELLED')").getSingleResult());
        BigDecimal exp = bd(em.createNativeQuery("SELECT COALESCE(SUM(amount), 0) FROM expenses WHERE MONTH(expense_date)=MONTH(CURDATE()) AND YEAR(expense_date)=YEAR(CURDATE())").getSingleResult());
        return new DashboardSummaryDto(items, cats, custs, bks, today, up, inc, pend, exp);
    }
    private BigDecimal bd(Object v) { return v == null ? BigDecimal.ZERO : new BigDecimal(v.toString()); }
}