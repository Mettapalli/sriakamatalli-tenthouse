package com.tenthouse.controller;
import com.tenthouse.dto.CategoryDto;
import com.tenthouse.entity.Category;
import com.tenthouse.repository.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/admin/categories")
public class AdminCategoryController {
    private final CategoryRepository catRepo; private final ItemRepository itemRepo;
    public AdminCategoryController(CategoryRepository catRepo, ItemRepository itemRepo) { this.catRepo = catRepo; this.itemRepo = itemRepo; }
    @GetMapping public List<Category> all() { return catRepo.findAllByOrderByDisplayOrderAscNameAsc(); }
    @PostMapping public ResponseEntity<?> create(@Valid @RequestBody CategoryDto d) {
        if (catRepo.findByNameIgnoreCase(d.getName().trim()).isPresent()) return ResponseEntity.badRequest().body(Map.of("error", "Category exists"));
        Category c = new Category(); c.setName(d.getName().trim()); c.setNameTe(d.getNameTe()); c.setDescription(d.getDescription());
        c.setDisplayOrder(d.getDisplayOrder() != null ? d.getDisplayOrder() : 0); c.setIsActive(d.getIsActive() != null ? d.getIsActive() : true);
        return ResponseEntity.ok(catRepo.save(c));
    }
    @PutMapping("/{id}") public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody CategoryDto d) {
        Category c = catRepo.findById(id).orElseThrow();
        c.setName(d.getName().trim()); c.setNameTe(d.getNameTe()); c.setDescription(d.getDescription());
        c.setDisplayOrder(d.getDisplayOrder() != null ? d.getDisplayOrder() : 0); c.setIsActive(d.getIsActive() != null ? d.getIsActive() : true);
        return ResponseEntity.ok(catRepo.save(c));
    }
    @DeleteMapping("/{id}") public ResponseEntity<?> del(@PathVariable Long id) {
        long u = itemRepo.countByCategoryId(id);
        if (u > 0) return ResponseEntity.badRequest().body(Map.of("error", "Used by " + u + " items"));
        catRepo.deleteById(id); return ResponseEntity.ok(Map.of("message", "Deleted"));
    }
}