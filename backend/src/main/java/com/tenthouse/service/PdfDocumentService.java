package com.tenthouse.service;

import com.tenthouse.entity.*;
import com.tenthouse.repository.*;
import org.openpdf.text.*;
import org.openpdf.text.pdf.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.List;

@Service
public class PdfDocumentService {
    private final BusinessDetailsRepository bizRepo;
    private final ItemRepository itemRepo;
    @Value("${app.upload.dir:uploads}") private String uploadDir;

    public PdfDocumentService(BusinessDetailsRepository bizRepo, ItemRepository itemRepo) {
        this.bizRepo = bizRepo; this.itemRepo = itemRepo;
    }

    public byte[] generateQuotationPdf(Quotation q) {
        BusinessDetails biz = bizRepo.findAll().stream().findFirst().orElse(null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter.getInstance(doc, out); doc.open();
        Font normal = loadFont(10, Font.NORMAL), bold = loadFont(10, Font.BOLD);
        addHeader(doc, biz, "QUOTATION");

        PdfPTable meta = new PdfPTable(2); meta.setWidthPercentage(100); meta.setSpacingAfter(10);
        meta.addCell(noBorder("Quotation #: " + q.getQuotationNumber() + "\nDate: " + q.getQuotationDate() + "\nValid Until: " + (q.getValidUntil() != null ? q.getValidUntil() : "-"), normal));
        meta.addCell(noBorder("Customer: " + q.getCustomer().getFullName() + "\nMobile: " + q.getCustomer().getMobileNumber() + (q.getEvent() != null ? "\nEvent: " + q.getEvent().getEventName() : ""), normal));
        doc.add(meta);

        PdfPTable tbl = new PdfPTable(new float[]{0.8f, 4.7f, 1.1f, 1.7f, 1.7f}); tbl.setWidthPercentage(100);
        addHdr(tbl, List.of("#", "Item Description", "Qty", "Rate (Rs.)", "Total (Rs.)"));
        int i = 1;
        for (QuotationItem qi : q.getItems()) {
            tbl.addCell(new Phrase(String.valueOf(i++), normal));
            tbl.addCell(new Phrase(bilingual(qi.getItemId(), qi.getItemName()), normal));
            tbl.addCell(new Phrase(String.valueOf(qi.getQuantity()), normal));
            tbl.addCell(new Phrase(qi.getUnitPrice().toPlainString(), normal));
            tbl.addCell(new Phrase(qi.getLineTotal().toPlainString(), normal));
        }
        doc.add(tbl);
        Paragraph tot = new Paragraph("\nSubtotal: Rs. " + q.getItemsSubtotal() + "\nExtra Charges: Rs. " + q.getAdditionalChargesTotal() + "\nDiscount: - Rs. " + q.getDiscountAmount() + "\nGrand Total: Rs. " + q.getGrandTotal(), bold);
        tot.setAlignment(Element.ALIGN_RIGHT); doc.add(tot);
        if (q.getTermsAndConditions() != null && !q.getTermsAndConditions().isBlank()) {
            doc.add(new Paragraph("\nTerms & Conditions:", bold));
            doc.add(new Paragraph(q.getTermsAndConditions(), loadFont(9, Font.NORMAL)));
        }
        doc.close(); return out.toByteArray();
    }

    public byte[] generateInvoicePdf(Invoice inv) {
        BusinessDetails biz = bizRepo.findAll().stream().findFirst().orElse(null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter.getInstance(doc, out); doc.open();
        Font normal = loadFont(10, Font.NORMAL), bold = loadFont(10, Font.BOLD);
        addHeader(doc, biz, "INVOICE / BILL");

        PdfPTable meta = new PdfPTable(2); meta.setWidthPercentage(100); meta.setSpacingAfter(10);
        meta.addCell(noBorder("Invoice #: " + inv.getInvoiceNumber() + "\nBooking #: " + inv.getBooking().getBookingNumber() + "\nDate: " + inv.getInvoiceDate() + "\nStatus: " + inv.getPaymentStatus(), normal));
        meta.addCell(noBorder("Customer: " + inv.getCustomer().getFullName() + "\nMobile: " + inv.getCustomer().getMobileNumber() + "\nEvent: " + inv.getBooking().getEvent().getEventName(), normal));
        doc.add(meta);

        PdfPTable tbl = new PdfPTable(new float[]{0.8f, 4.7f, 1.1f, 1.7f, 1.7f}); tbl.setWidthPercentage(100);
        addHdr(tbl, List.of("#", "Item Description", "Qty", "Rate (Rs.)", "Total (Rs.)"));
        int i = 1;
        for (InvoiceItem ii : inv.getItems()) {
            tbl.addCell(new Phrase(String.valueOf(i++), normal));
            tbl.addCell(new Phrase(bilingual(ii.getItemId(), ii.getItemName()), normal));
            tbl.addCell(new Phrase(String.valueOf(ii.getQuantity()), normal));
            tbl.addCell(new Phrase(ii.getUnitPrice().toPlainString(), normal));
            tbl.addCell(new Phrase(ii.getLineTotal().toPlainString(), normal));
        }
        doc.add(tbl);
        Paragraph tot = new Paragraph("\nSubtotal: Rs. " + inv.getItemsSubtotal() + "\nExtra Charges: Rs. " + inv.getAdditionalChargesTotal() + "\nDiscount: - Rs. " + inv.getDiscountAmount()
                + (inv.getTaxAmount().compareTo(BigDecimal.ZERO) > 0 ? "\nTax: Rs. " + inv.getTaxAmount() : "")
                + "\nTotal: Rs. " + inv.getTotalAmount() + "\nPaid: Rs. " + inv.getPaidAmount() + "\nBalance: Rs. " + inv.getBalanceAmount(), bold);
        tot.setAlignment(Element.ALIGN_RIGHT); doc.add(tot);
        doc.close(); return out.toByteArray();
    }

    private String bilingual(Long id, String en) {
        if (id == null) return en;
        return itemRepo.findById(id).map(i -> (i.getNameTe() != null && !i.getNameTe().isBlank()) ? en + " (" + i.getNameTe() + ")" : en).orElse(en);
    }

    private Font loadFont(float s, int st) {
        try {
            ClassPathResource res = new ClassPathResource("fonts/NotoSansTelugu-Regular.ttf");
            if (res.exists()) {
                BaseFont bf = BaseFont.createFont("NotoSansTelugu-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, res.getInputStream().readAllBytes(), null);
                return new Font(bf, s, st);
            }
        } catch (Exception ignored) {}
        return FontFactory.getFont(FontFactory.HELVETICA, s, st);
    }

    private void addHeader(Document doc, BusinessDetails biz, String title) {
        if (biz != null && biz.getLogoPath() != null && biz.getLogoPath().startsWith("/uploads/")) {
            try {
                Path p = Paths.get(uploadDir).resolve(biz.getLogoPath().replaceFirst("^/uploads/", "")).toAbsolutePath().normalize();
                if (Files.exists(p)) { Image img = Image.getInstance(p.toString()); img.scaleToFit(70, 70); img.setAlignment(Element.ALIGN_CENTER); doc.add(img); }
            } catch (Exception ignored) {}
        }
        Paragraph p1 = new Paragraph(biz != null ? biz.getBusinessName() : "SRIAKAMATALLI TENT HOUSE", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, new Color(74, 13, 102)));
        p1.setAlignment(Element.ALIGN_CENTER); doc.add(p1);
        if (biz != null) {
            Paragraph p2 = new Paragraph("Owners: " + biz.getOwnerNames() + " | Ph: " + biz.getPrimaryMobile() + (biz.getSecondaryMobile() != null ? ", " + biz.getSecondaryMobile() : ""), FontFactory.getFont(FontFactory.HELVETICA, 9));
            p2.setAlignment(Element.ALIGN_CENTER); doc.add(p2);
        }
        Paragraph p3 = new Paragraph("\n" + title + "\n ", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13));
        p3.setAlignment(Element.ALIGN_CENTER); doc.add(p3);
    }

    private PdfPCell noBorder(String t, Font f) { PdfPCell c = new PdfPCell(new Phrase(t, f)); c.setBorder(Rectangle.NO_BORDER); return c; }
    private void addHdr(PdfPTable t, List<String> hs) {
        Font hf = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
        for (String h : hs) { PdfPCell c = new PdfPCell(new Phrase(h, hf)); c.setBackgroundColor(new Color(74, 13, 102)); c.setPadding(5); t.addCell(c); }
    }
}