package com.tenthouse.controller;
import com.tenthouse.entity.*;
import com.tenthouse.repository.*;
import com.tenthouse.service.PdfDocumentService;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.math.*;
import java.time.LocalDate;
import java.util.*;

@RestController @RequestMapping("/api/admin/invoices")
public class AdminInvoiceController {
    private final InvoiceRepository invRepo; private final BookingRepository bkRepo; private final PaymentRepository payRepo;
    private final UserRepository userRepo; private final PdfDocumentService pdf;
    public AdminInvoiceController(InvoiceRepository invRepo, BookingRepository bkRepo, PaymentRepository payRepo, UserRepository userRepo, PdfDocumentService pdf) {
        this.invRepo = invRepo; this.bkRepo = bkRepo; this.payRepo = payRepo; this.userRepo = userRepo; this.pdf = pdf;
    }
    @GetMapping public List<Invoice> all() { return invRepo.findAllByOrderByIdDesc(); }
    @PostMapping("/from-booking/{bookingId}") @Transactional
    public Invoice fromBk(@PathVariable Long bookingId, @RequestParam(defaultValue="0") BigDecimal taxPercentage) {
        return invRepo.findByBookingId(bookingId).orElseGet(() -> {
            Booking b = bkRepo.findById(bookingId).orElseThrow();
            BigDecimal ex = b.getTransportationCharge().add(b.getLabourCharge()).add(b.getDecorationCharge()).add(b.getInstallationCharge()).add(b.getGeneratorCharge()).add(b.getOtherCharges());
            BigDecimal taxable = b.getItemsSubtotal().add(ex).subtract(b.getDiscountAmount()).max(BigDecimal.ZERO);
            BigDecimal tax = taxable.multiply(taxPercentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            BigDecimal tot = taxable.add(tax), paid = b.getAdvancePaid(), bal = tot.subtract(paid).max(BigDecimal.ZERO);
            Invoice inv = new Invoice(); inv.setInvoiceNumber("INV-" + System.currentTimeMillis()); inv.setBooking(b); inv.setCustomer(b.getCustomer());
            inv.setInvoiceDate(LocalDate.now()); inv.setDueDate(b.getEndDate()); inv.setItemsSubtotal(b.getItemsSubtotal());
            inv.setAdditionalChargesTotal(ex); inv.setDiscountAmount(b.getDiscountAmount()); inv.setTaxPercentage(taxPercentage);
            inv.setTaxAmount(tax); inv.setTotalAmount(tot); inv.setPaidAmount(paid); inv.setBalanceAmount(bal);
            inv.setPaymentStatus(bal.compareTo(BigDecimal.ZERO)==0 ? Invoice.PaymentStatus.PAID : (paid.compareTo(BigDecimal.ZERO)>0 ? Invoice.PaymentStatus.PARTIALLY_PAID : Invoice.PaymentStatus.UNPAID));
            for (BookingItem bi : b.getItems()) {
                InvoiceItem ii = new InvoiceItem(); ii.setInvoice(inv); ii.setItemId(bi.getItem().getId()); ii.setItemName(bi.getItem().getName());
                ii.setQuantity(bi.getQuantity()); ii.setUnitPrice(bi.getUnitPrice()); ii.setPriceUnit(bi.getPriceUnit()); ii.setLineTotal(bi.getLineTotal()); inv.getItems().add(ii);
            }
            Invoice saved = invRepo.save(inv);
            if (paid.compareTo(BigDecimal.ZERO) > 0) {
                Payment p = new Payment(); p.setReceiptNumber("RCPT-" + System.currentTimeMillis()); p.setBookingId(b.getId());
                p.setInvoiceId(saved.getId()); p.setCustomer(b.getCustomer()); p.setPaymentDate(LocalDate.now()); p.setAmount(paid);
                p.setIsAdvance(true); p.setNotes("Initial booking advance"); payRepo.save(p);
            }
            return saved;
        });
    }
    @PostMapping("/{id}/payments") @Transactional
    public Invoice pay(@PathVariable Long id, @RequestBody Map<String,Object> body, Authentication a) {
        Invoice inv = invRepo.findById(id).orElseThrow();
        BigDecimal amt = new BigDecimal(body.get("amount").toString());
        Payment p = new Payment(); p.setReceiptNumber("RCPT-" + System.currentTimeMillis()); p.setBookingId(inv.getBooking().getId());
        p.setInvoiceId(inv.getId()); p.setCustomer(inv.getCustomer()); p.setPaymentDate(LocalDate.now()); p.setAmount(amt);
        p.setPaymentMethod(Payment.PaymentMethod.valueOf(body.getOrDefault("paymentMethod","CASH").toString()));
        p.setReferenceNumber(body.get("referenceNumber") != null ? body.get("referenceNumber").toString() : null);
        p.setNotes(body.get("notes") != null ? body.get("notes").toString() : null);
        p.setReceivedBy(userRepo.findByUsername(a.getName()).map(User::getId).orElse(null));
        payRepo.save(p);
        BigDecimal np = inv.getPaidAmount().add(amt), nb = inv.getTotalAmount().subtract(np).max(BigDecimal.ZERO);
        inv.setPaidAmount(np); inv.setBalanceAmount(nb);
        inv.setPaymentStatus(nb.compareTo(BigDecimal.ZERO)==0 ? Invoice.PaymentStatus.PAID : Invoice.PaymentStatus.PARTIALLY_PAID);
        Booking b = inv.getBooking(); b.setAdvancePaid(np); b.setBalanceAmount(b.getFinalAmount().subtract(np).max(BigDecimal.ZERO)); bkRepo.save(b);
        return invRepo.save(inv);
    }
    @GetMapping("/{id}/pdf") public ResponseEntity<byte[]> pdf(@PathVariable Long id) {
        Invoice inv = invRepo.findById(id).orElseThrow();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + inv.getInvoiceNumber() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF).body(pdf.generateInvoicePdf(inv));
    }
}