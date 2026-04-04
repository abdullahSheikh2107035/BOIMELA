package com.project.boimela.dto;

public class PurchaseResponse {
    private Long requestId;
    private String bookTitle;
    private String sellerUsername;
    private String status;
    private Double price;

    public PurchaseResponse() {}

    public PurchaseResponse(Long requestId, String bookTitle, String sellerUsername,
                            String status, Double price) {
        this.requestId = requestId;
        this.bookTitle = bookTitle;
        this.sellerUsername = sellerUsername;
        this.status = status;
        this.price = price;
    }

    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public String getSellerUsername() { return sellerUsername; }
    public void setSellerUsername(String sellerUsername) { this.sellerUsername = sellerUsername; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
