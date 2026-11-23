package com.tenthouse.repository;
import com.tenthouse.entity.Item;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.*;
public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByItemCode(String itemCode);
    boolean existsByItemCode(String itemCode);
    long countByCategoryId(Long categoryId);

    @Query("SELECT i FROM Item i WHERE i.status <> 'INACTIVE' AND (:categoryId IS NULL OR i.category.id = :categoryId) AND (:keyword IS NULL OR :keyword = '' OR LOWER(i.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR i.nameTe LIKE CONCAT('%', :keyword, '%') OR LOWER(i.itemCode) LIKE LOWER(CONCAT('%', :keyword, '%'))) ORDER BY i.category.displayOrder ASC, i.id ASC")
    List<Item> searchPublicItems(@Param("categoryId") Long categoryId, @Param("keyword") String keyword);

    @Query("SELECT i FROM Item i WHERE (:categoryId IS NULL OR i.category.id = :categoryId) AND (:status IS NULL OR i.status = :status) AND (:keyword IS NULL OR :keyword = '' OR LOWER(i.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR i.nameTe LIKE CONCAT('%', :keyword, '%') OR LOWER(i.itemCode) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Item> searchAdminItems(@Param("categoryId") Long categoryId, @Param("status") Item.ItemStatus status, @Param("keyword") String keyword, Sort sort);
}