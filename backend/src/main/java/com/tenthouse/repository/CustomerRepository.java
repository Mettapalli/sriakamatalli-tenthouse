package com.tenthouse.repository;
import com.tenthouse.entity.Customer;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.*;
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findFirstByMobileNumber(String mobileNumber);
    @Query("SELECT c FROM Customer c WHERE (:q IS NULL OR :q = '' OR LOWER(c.fullName) LIKE LOWER(CONCAT('%', :q, '%')) OR c.mobileNumber LIKE CONCAT('%', :q, '%') OR LOWER(c.customerCode) LIKE LOWER(CONCAT('%', :q, '%'))) ORDER BY c.id DESC")
    List<Customer> search(@Param("q") String q);
}