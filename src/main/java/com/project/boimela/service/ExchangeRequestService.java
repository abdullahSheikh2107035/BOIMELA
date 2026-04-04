package com.project.boimela.service;

import com.project.boimela.dto.PurchaseResponse;
import com.project.boimela.entity.Book;
import com.project.boimela.entity.ExchangeRequest;
import com.project.boimela.entity.User;
import com.project.boimela.repository.BookRepository;
import com.project.boimela.repository.ExchangeRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExchangeRequestService {

    private final ExchangeRequestRepository repository;
    private final BookRepository bookRepository;

    public ExchangeRequestService(ExchangeRequestRepository repository, BookRepository bookRepository) {
        this.repository = repository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public PurchaseResponse createPurchaseRequest(Long bookId, User buyer) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found."));

        if (!book.isAvailable()) {
            throw new RuntimeException("Book is no longer available for purchase.");
        }
        if (book.getSeller().getId().equals(buyer.getId())) {
            throw new RuntimeException("You cannot buy your own book.");
        }

        // Mark book unavailable immediately
        book.setAvailable(false);
        bookRepository.save(book);

        ExchangeRequest req = new ExchangeRequest(book, buyer, "PENDING");
        return toResponse(repository.save(req));
    }

    public List<PurchaseResponse> getMyPurchases(User buyer) {
        return repository.findByBuyer(buyer).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<PurchaseResponse> getIncomingOrders(User seller) {
        return repository.findByBookSeller(seller).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PurchaseResponse updateStatus(Long requestId, String status, User seller) {
        ExchangeRequest req = repository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!req.getBook().getSeller().getId().equals(seller.getId())) {
            throw new RuntimeException("You are not the seller of this book.");
        }

        String upperStatus = status.toUpperCase();
        if (!upperStatus.equals("ACCEPTED") && !upperStatus.equals("REJECTED")) {
            throw new RuntimeException("Status must be ACCEPTED or REJECTED.");
        }

        req.setStatus(upperStatus);

        // If rejected, make the book available again so others can buy it
        if (upperStatus.equals("REJECTED")) {
            Book book = req.getBook();
            book.setAvailable(true);
            bookRepository.save(book);
        }

        return toResponse(repository.save(req));
    }

    public List<PurchaseResponse> getAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private PurchaseResponse toResponse(ExchangeRequest req) {
        return new PurchaseResponse(
                req.getId(),
                req.getBook().getTitle(),
                req.getBook().getSeller().getUsername(),
                req.getStatus(),
                req.getBook().getPrice()
        );
    }
}
