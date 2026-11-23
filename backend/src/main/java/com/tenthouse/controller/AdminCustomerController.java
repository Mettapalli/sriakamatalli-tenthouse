package com.tenthouse.controller;
import com.tenthouse.entity.*;
import com.tenthouse.repository.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/admin/customers")
public class AdminCustomerController {
    private final CustomerRepository custRepo; private final BookingRepository bkRepo;
    public AdminCustomerController(CustomerRepository custRepo, BookingRepository bkRepo) { this.custRepo = custRepo; this.bkRepo = bkRepo; }
    @GetMapping public List<Customer> search(@RequestParam(required=false) String q) { return custRepo.search(q); }
    @PostMapping public Customer create(@Valid @RequestBody Customer c) {
        if (c.getCustomerCode() == null || c.getCustomerCode().isBlank()) c.setCustomerCode("CUST-" + (custRepo.count() + 1001));
        return custRepo.save(c);
    }
    @PutMapping("/{id}") public Customer update(@PathVariable Long id, @Valid @RequestBody Customer in) {
        Customer c = custRepo.findById(id).orElseThrow();
        c.setFullName(in.getFullName().trim()); c.setMobileNumber(in.getMobileNumber().trim()); c.setWhatsappNumber(in.getWhatsappNumber());
        c.setEmail(in.getEmail()); c.setAddress(in.getAddress()); c.setDefaultEventLocation(in.getDefaultEventLocation()); c.setNotes(in.getNotes());
        return custRepo.save(c);
    }
    @DeleteMapping("/{id}") public Map<String,String> del(@PathVariable Long id) { custRepo.deleteById(id); return Map.of("message","Deleted"); }
    @GetMapping("/{id}/bookings") public List<Booking> hist(@PathVariable Long id) { return bkRepo.findByCustomerIdOrderByIdDesc(id); }
}