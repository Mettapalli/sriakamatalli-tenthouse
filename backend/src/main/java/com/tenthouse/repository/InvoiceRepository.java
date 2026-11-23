package com.tenthouse.repository;
import com.tenthouse.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findAllByOrderByIdDesc();
    Optional<Invoice> findByBookingId(Long bookingId);
}