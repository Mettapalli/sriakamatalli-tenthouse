package com.tenthouse.repository;
import com.tenthouse.entity.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {
    List<ItemImage> findByItemId(Long itemId);
}