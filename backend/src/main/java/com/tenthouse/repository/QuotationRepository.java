package com.tenthouse.repository;
import com.tenthouse.entity.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface QuotationRepository extends JpaRepository<Quotation, Long> { List<Quotation> findAllByOrderByIdDesc(); }