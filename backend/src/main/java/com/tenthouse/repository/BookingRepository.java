package com.tenthouse.repository;
import com.tenthouse.entity.Booking;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByOrderByIdDesc();
    List<Booking> findByCustomerIdOrderByIdDesc(Long customerId);
    @Query("SELECT COALESCE(SUM(bi.quantity), 0) FROM BookingItem bi JOIN bi.booking b WHERE bi.item.id = :itemId AND b.status IN ('CONFIRMED', 'IN_PROGRESS') AND b.startDate <= :endDate AND b.endDate >= :startDate AND (:excludeBookingId IS NULL OR b.id <> :excludeBookingId)")
    Integer sumBookedQtyForDates(@Param("itemId") Long itemId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("excludeBookingId") Long excludeBookingId);
}