package com.tenthouse.controller;
import com.tenthouse.dto.ItemRequestDto;
import com.tenthouse.entity.*;
import com.tenthouse.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;

@RestController @RequestMapping("/api/admin/items")
public class AdminItemController {
    private final ItemService svc;
    public AdminItemController(ItemService svc) { this.svc = svc; }
    @GetMapping public List<Item> list(@RequestParam(required=false) Long categoryId, @RequestParam(required=false) Item.ItemStatus status, @RequestParam(required=false) String keyword, @RequestParam(defaultValue="id") String sortBy, @RequestParam(defaultValue="asc") String dir) { return svc.searchItems(categoryId, status, keyword, sortBy, dir); }
    @GetMapping("/{id}") public Item one(@PathVariable Long id) { return svc.getById(id); }
    @PostMapping public Item create(@Valid @RequestBody ItemRequestDto d, Authentication a) { return svc.createItem(d, a.getName()); }
    @PutMapping("/{id}") public Item update(@PathVariable Long id, @Valid @RequestBody ItemRequestDto d, Authentication a) { return svc.updateItem(id, d, a.getName()); }
    @DeleteMapping("/{id}") public Map<String,String> del(@PathVariable Long id) { svc.deleteItem(id); return Map.of("message","Deleted"); }
    @GetMapping("/{id}/price-history") public List<ItemPriceHistory> hist(@PathVariable Long id) { return svc.getPriceHistory(id); }
    @PostMapping("/{id}/images") public ItemImage upImg(@PathVariable Long id, @RequestParam("file") MultipartFile f, @RequestParam(defaultValue="false") boolean primary) throws IOException { return svc.uploadImage(id, f, primary); }
    @DeleteMapping("/images/{imageId}") public Map<String,String> delImg(@PathVariable Long imageId) { svc.deleteImage(imageId); return Map.of("message","Deleted"); }
    @GetMapping("/export/excel") public ResponseEntity<byte[]> exp() throws IOException {
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sriakamatalli-items.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).body(svc.exportItemsToExcel());
    }
    @PostMapping("/import/excel") public Map<String,Object> imp(@RequestParam("file") MultipartFile f, Authentication a) throws IOException { return svc.importItemsFromExcel(f, a.getName()); }
}