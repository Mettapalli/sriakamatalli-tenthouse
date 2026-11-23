package com.tenthouse.repository;
import com.tenthouse.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByIsActiveTrueOrderByDisplayOrderAscNameAsc();
    List<Category> findAllByOrderByDisplayOrderAscNameAsc();
    Optional<Category> findByNameIgnoreCase(String name);
}