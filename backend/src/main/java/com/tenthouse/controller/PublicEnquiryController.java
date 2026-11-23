package com.tenthouse.controller;
import com.tenthouse.entity.*;
import com.tenthouse.repository.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

@RestController @RequestMapping("/api/public")
public class PublicEnquiryController {
    private final CustomerRepository custRepo; private final EventRepository eventRepo; private final BookingRepository bkRepo;
    public PublicEnquiryController(CustomerRepository custRepo, EventRepository eventRepo, BookingRepository bkRepo) {
        this.custRepo = custRepo; this.eventRepo = eventRepo; this.bkRepo = bkRepo;
    }
    public static class EnquiryRequest {
        @NotBlank public String fullName; @NotBlank public String mobileNumber; public String whatsappNumber;
        @NotBlank public String eventName; @NotNull public Event.EventType eventType = Event.EventType.WEDDING;
        @NotNull public LocalDate startDate; @NotNull public LocalDate endDate;
        @NotBlank public String eventAddress; public Integer guestCount; public String specialInstructions;
    }
    @PostMapping("/booking-request") @Transactional
    public Map<String, Object> submit(@Valid @RequestBody EnquiryRequest req) {
        if (req.endDate.isBefore(req.startDate)) throw new IllegalArgumentException("End date invalid");
        Customer c = custRepo.findFirstByMobileNumber(req.mobileNumber.trim()).orElseGet(() -> {
            Customer nc = new Customer(); nc.setCustomerCode("CUST-" + (custRepo.count() + 1001));
            nc.setFullName(req.fullName.trim()); nc.setMobileNumber(req.mobileNumber.trim());
            nc.setWhatsappNumber(req.whatsappNumber); nc.setDefaultEventLocation(req.eventAddress.trim());
            return custRepo.save(nc);
        });
        Event ev = new Event(); ev.setCustomer(c); ev.setEventName(req.eventName.trim()); ev.setEventType(req.eventType);
        ev.setEventDate(req.startDate); ev.setEndDate(req.endDate); ev.setEventAddress(req.eventAddress.trim());
        ev.setGuestCount(req.guestCount); ev.setSpecialInstructions(req.specialInstructions); ev = eventRepo.save(ev);
        Booking bk = new Booking(); bk.setBookingNumber("ENQ-" + System.currentTimeMillis()); bk.setCustomer(c); bk.setEvent(ev);
        bk.setStartDate(req.startDate); bk.setEndDate(req.endDate); bk.setStatus(Booking.BookingStatus.ENQUIRY);
        bk.setNotes(req.specialInstructions); bk = bkRepo.save(bk);
        return Map.of("bookingNumber", bk.getBookingNumber(), "message", "Enquiry submitted successfully!");
    }
}