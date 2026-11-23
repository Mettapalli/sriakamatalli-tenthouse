package com.tenthouse.controller;
import com.tenthouse.entity.*;
import com.tenthouse.repository.*;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/admin/expenses")
public class AdminExpenseController {
    private final ExpenseRepository repo; private final UserRepository userRepo;
    public AdminExpenseController(ExpenseRepository repo, UserRepository userRepo) { this.repo = repo; this.userRepo = userRepo; }
    @GetMapping public List<Expense> all() { return repo.findAllByOrderByExpenseDateDescIdDesc(); }
    @PostMapping public Expense create(@Valid @RequestBody Expense e, Authentication a) { userRepo.findByUsername(a.getName()).map(User::getId).ifPresent(e::setRecordedBy); return repo.save(e); }
    @DeleteMapping("/{id}") public Map<String,String> del(@PathVariable Long id) { repo.deleteById(id); return Map.of("message","Deleted"); }
}