package com.tenthouse.controller;
import com.tenthouse.dto.*;
import com.tenthouse.entity.Booking;
import com.tenthouse.repository.BookingRepository;
import com.tenthouse.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;

@RestController @RequestMapping("/api/admin/bookings")
public class AdminBookingController {
    private final BookingRepository repo; private final BookingService svc;
    public AdminBookingController(BookingRepository repo, BookingService svc) { this.repo = repo; this.svc = svc; }
    @GetMapping public List<Booking> all() { return repo.findAllByOrderByIdDesc(); }
    @GetMapping("/{id}") public Booking one(@PathVariable Long id) { return repo.findById(id).orElseThrow(); }
    @GetMapping("/availability") public List<Map<String,Object>> avail(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endDate) { return svc.getAllItemsAvailability(startDate, endDate); }
    @PostMapping public Booking create(@Valid @RequestBody BookingRequestDto d, Authentication a) { return svc.createBooking(d, a.getName()); }
    @PutMapping("/{id}/status") public Booking status(@PathVariable Long id, @RequestParam Booking.BookingStatus status, Authentication a) { return svc.updateStatus(id, status, a.getName()); }
    @PostMapping("/{id}/complete") public Booking complete(@PathVariable Long id, @RequestBody BookingReturnDto d, Authentication a) { return svc.completeAndReconcile(id, d, a.getName()); }
}