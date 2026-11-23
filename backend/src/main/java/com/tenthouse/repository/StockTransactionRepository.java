package com.tenthouse.repository;
import com.tenthouse.entity.StockTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {
    List<StockTransaction> findByItemIdOrderByTransactionDateDesc(Long itemId);
}