package com.tenthouse.repository;
import com.tenthouse.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByBookingIdOrderByPaymentDateDescIdDesc(Long bookingId);
    List<Payment> findAllByOrderByPaymentDateDescIdDesc();
}