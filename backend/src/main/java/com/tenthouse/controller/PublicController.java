package com.tenthouse.controller;
import com.tenthouse.entity.*;
import com.tenthouse.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/public")
public class PublicController {
    private final BusinessDetailsRepository bizRepo;
    private final CategoryRepository catRepo;
    private final ItemRepository itemRepo;
    public PublicController(BusinessDetailsRepository bizRepo, CategoryRepository catRepo, ItemRepository itemRepo) {
        this.bizRepo = bizRepo; this.catRepo = catRepo; this.itemRepo = itemRepo;
    }
    @GetMapping("/business") public ResponseEntity<BusinessDetails> biz() { return bizRepo.findAll().stream().findFirst().map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }
    @GetMapping("/categories") public List<Category> cats() { return catRepo.findByIsActiveTrueOrderByDisplayOrderAscNameAsc(); }
    @GetMapping("/items") public List<Item> items(@RequestParam(required = false) Long categoryId, @RequestParam(required = false) String keyword) { return itemRepo.searchPublicItems(categoryId, keyword); }
}