package com.tenthouse.controller;
import com.tenthouse.entity.*;
import com.tenthouse.repository.*;
import com.tenthouse.service.PdfDocumentService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController @RequestMapping("/api/admin/quotations")
public class AdminQuotationController {
    private final QuotationRepository qRepo; private final BookingRepository bRepo; private final BusinessDetailsRepository bizRepo; private final PdfDocumentService pdf;
    public AdminQuotationController(QuotationRepository qRepo, BookingRepository bRepo, BusinessDetailsRepository bizRepo, PdfDocumentService pdf) {
        this.qRepo = qRepo; this.bRepo = bRepo; this.bizRepo = bizRepo; this.pdf = pdf;
    }
    @GetMapping public List<Quotation> all() { return qRepo.findAllByOrderByIdDesc(); }
    @PostMapping("/from-booking/{bookingId}") public Quotation fromBk(@PathVariable Long bookingId) {
        Booking b = bRepo.findById(bookingId).orElseThrow();
        BigDecimal ex = b.getTransportationCharge().add(b.getLabourCharge()).add(b.getDecorationCharge()).add(b.getInstallationCharge()).add(b.getGeneratorCharge()).add(b.getOtherCharges());
        Quotation q = new Quotation(); q.setQuotationNumber("QT-" + System.currentTimeMillis()); q.setBookingId(b.getId());
        q.setCustomer(b.getCustomer()); q.setEvent(b.getEvent()); q.setQuotationDate(LocalDate.now()); q.setValidUntil(LocalDate.now().plusDays(15));
        q.setItemsSubtotal(b.getItemsSubtotal()); q.setAdditionalChargesTotal(ex); q.setDiscountAmount(b.getDiscountAmount()); q.setGrandTotal(b.getFinalAmount());
        q.setTermsAndConditions(bizRepo.findAll().stream().findFirst().map(BusinessDetails::getTermsAndConditions).orElse(""));
        for (BookingItem bi : b.getItems()) {
            QuotationItem qi = new QuotationItem(); qi.setQuotation(q); qi.setItemId(bi.getItem().getId()); qi.setItemName(bi.getItem().getName());
            qi.setQuantity(bi.getQuantity()); qi.setUnitPrice(bi.getUnitPrice()); qi.setPriceUnit(bi.getPriceUnit()); qi.setLineTotal(bi.getLineTotal()); q.getItems().add(qi);
        }
        return qRepo.save(q);
    }
    @GetMapping("/{id}/pdf") public ResponseEntity<byte[]> pdf(@PathVariable Long id) {
        Quotation q = qRepo.findById(id).orElseThrow();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + q.getQuotationNumber() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF).body(pdf.generateQuotationPdf(q));
    }
}