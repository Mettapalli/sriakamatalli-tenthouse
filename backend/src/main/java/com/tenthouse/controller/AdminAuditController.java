package com.tenthouse.controller;
import com.tenthouse.entity.AuditLog;
import com.tenthouse.repository.AuditLogRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/admin/audit-logs")
public class AdminAuditController {
    private final AuditLogRepository repo;
    public AdminAuditController(AuditLogRepository repo) { this.repo = repo; }
    @GetMapping public List<AuditLog> latest() { return repo.findTop100ByOrderByIdDesc(); }
}