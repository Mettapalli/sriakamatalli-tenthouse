package com.tenthouse;

import com.tenthouse.dto.BookingRequestDto;
import com.tenthouse.entity.*;
import com.tenthouse.repository.*;
import com.tenthouse.service.BookingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
    @Mock BookingRepository bookingRepo; @Mock CustomerRepository customerRepo; @Mock EventRepository eventRepo;
    @Mock ItemRepository itemRepo; @Mock StockTransactionRepository stockRepo; @Mock UserRepository userRepo;
    @InjectMocks BookingService bookingService;

    @Test
    void preventsDoubleBookingWhenOverlappingDatesExceedAvailableStock() {
        Customer c = new Customer(); c.setId(1L); c.setFullName("Test");
        Item chair = new Item(); chair.setId(62L); chair.setName("Handless Chair");
        chair.setTotalQuantity(500); chair.setDamagedQuantity(0);
        chair.setRentalPrice(new BigDecimal("6.00")); chair.setPriceUnit(Item.PriceUnit.PER_DAY);

        when(customerRepo.findById(1L)).thenReturn(Optional.of(c));
        when(eventRepo.save(any(Event.class))).thenAnswer(inv -> inv.getArgument(0));
        when(itemRepo.findById(62L)).thenReturn(Optional.of(chair));
        when(bookingRepo.sumBookedQtyForDates(62L, LocalDate.of(2026,12,1), LocalDate.of(2026,12,2), null)).thenReturn(200);

        BookingRequestDto dto = new BookingRequestDto();
        dto.setCustomerId(1L); dto.setEventName("Wedding"); dto.setEventAddress("Vijayawada");
        dto.setStartDate(LocalDate.of(2026,12,1)); dto.setEndDate(LocalDate.of(2026,12,2));
        BookingRequestDto.ItemLine l = new BookingRequestDto.ItemLine(); l.setItemId(62L); l.setQuantity(301);
        dto.setItems(List.of(l));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> bookingService.createBooking(dto, "admin"));
        assertTrue(ex.getMessage().contains("Available: 300"));
    }
}