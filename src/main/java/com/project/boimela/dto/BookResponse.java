package com.project.boimela.dto;

public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String description;
    private Double price;
    private boolean available;
    private String sellerUsername;

    public BookResponse() {}

    public BookResponse(Long id, String title, String author, String description,
                        Double price, boolean available, String sellerUsername) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.price = price;
        this.available = available;
        this.sellerUsername = sellerUsername;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public String getSellerUsername() { return sellerUsername; }
    public void setSellerUsername(String sellerUsername) { this.sellerUsername = sellerUsername; }
}
