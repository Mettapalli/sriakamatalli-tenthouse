package com.tenthouse.controller;
import com.tenthouse.entity.BusinessDetails;
import com.tenthouse.repository.BusinessDetailsRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@RestController @RequestMapping("/api/admin/business")
public class BusinessDetailsController {
    private final BusinessDetailsRepository repo;
    @Value("${app.upload.dir:uploads}") private String uploadDir;
    public BusinessDetailsController(BusinessDetailsRepository repo) { this.repo = repo; }

    @GetMapping public ResponseEntity<BusinessDetails> get() { return repo.findAll().stream().findFirst().map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }

    @PutMapping public ResponseEntity<BusinessDetails> update(@Valid @RequestBody BusinessDetails in) {
        BusinessDetails ex = repo.findAll().stream().findFirst().orElse(new BusinessDetails());
        ex.setBusinessName(in.getBusinessName()); ex.setTagline(in.getTagline()); ex.setOwnerNames(in.getOwnerNames());
        ex.setPrimaryMobile(in.getPrimaryMobile()); ex.setSecondaryMobile(in.getSecondaryMobile());
        ex.setWhatsappPrimary(in.getWhatsappPrimary()); ex.setWhatsappSecondary(in.getWhatsappSecondary());
        ex.setEmail(in.getEmail()); ex.setAddress(in.getAddress()); ex.setCity(in.getCity()); ex.setState(in.getState());
        ex.setPinCode(in.getPinCode()); ex.setGstNumber(in.getGstNumber()); ex.setWebsiteName(in.getWebsiteName());
        ex.setTermsAndConditions(in.getTermsAndConditions());
        return ResponseEntity.ok(repo.save(ex));
    }

    @PostMapping("/logo")
    public ResponseEntity<Map<String, String>> logo(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) return ResponseEntity.badRequest().body(Map.of("error", "Select a file"));
        String orig = file.getOriginalFilename() != null ? file.getOriginalFilename() : "logo.png";
        String fn = "logo-" + UUID.randomUUID() + (orig.contains(".") ? orig.substring(orig.lastIndexOf('.')) : ".png");
        Path dir = Paths.get(uploadDir, "logos").toAbsolutePath().normalize(); Files.createDirectories(dir);
        Files.copy(file.getInputStream(), dir.resolve(fn), StandardCopyOption.REPLACE_EXISTING);
        String p = "/uploads/logos/" + fn;
        BusinessDetails ex = repo.findAll().stream().findFirst().orElseThrow(); ex.setLogoPath(p); repo.save(ex);
        return ResponseEntity.ok(Map.of("logoPath", p));
    }
}