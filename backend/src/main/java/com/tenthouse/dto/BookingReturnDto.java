package com.tenthouse.dto;
import java.util.List;
public class BookingReturnDto {
    private List<ReturnLine> lines;
    private String remarks;
    public static class ReturnLine {
        private Long bookingItemId;
        private int returnedQty;
        private int damagedQty;
        private int missingQty;
        public Long getBookingItemId() { return bookingItemId; } public void setBookingItemId(Long bookingItemId) { this.bookingItemId = bookingItemId; }
        public int getReturnedQty() { return returnedQty; } public void setReturnedQty(int returnedQty) { this.returnedQty = returnedQty; }
        public int getDamagedQty() { return damagedQty; } public void setDamagedQty(int damagedQty) { this.damagedQty = damagedQty; }
        public int getMissingQty() { return missingQty; } public void setMissingQty(int missingQty) { this.missingQty = missingQty; }
    }
    public List<ReturnLine> getLines() { return lines; } public void setLines(List<ReturnLine> lines) { this.lines = lines; }
    public String getRemarks() { return remarks; } public void setRemarks(String remarks) { this.remarks = remarks; }
}