package com.project.boimela.dto;

public class PurchaseResponse {
    private Long id;
    private String bookTitle;
    private String sellerUsername;
    private String status;
    private Double price;

    public PurchaseResponse() {}

    public PurchaseResponse(Long id, String bookTitle, String sellerUsername,
                            String status, Double price) {
        this.id = id;
        this.bookTitle = bookTitle;
        this.sellerUsername = sellerUsername;
        this.status = status;
        this.price = price;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public String getSellerUsername() { return sellerUsername; }
    public void setSellerUsername(String sellerUsername) { this.sellerUsername = sellerUsername; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
