package com.tenthouse.repository;
import com.tenthouse.entity.ItemPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ItemPriceHistoryRepository extends JpaRepository<ItemPriceHistory, Long> {
    List<ItemPriceHistory> findByItemIdOrderByChangedAtDesc(Long itemId);
}