package com.project.boimela.controller;

import com.project.boimela.dto.PurchaseResponse;
import com.project.boimela.entity.User;
import com.project.boimela.repository.UserRepository;
import com.project.boimela.service.ExchangeRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/purchases")
public class ExchangeRequestController {

    private final ExchangeRequestService exchangeService;
    private final UserRepository userRepository;

    public ExchangeRequestController(ExchangeRequestService exchangeService, UserRepository userRepository) {
        this.exchangeService = exchangeService;
        this.userRepository = userRepository;
    }

    /** Buyer: request to buy a book */
    @PostMapping("/buy/{bookId}")
    @PreAuthorize("hasRole('BUYER') or hasRole('ADMIN')")
    public ResponseEntity<?> buyBook(
            @PathVariable Long bookId,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User buyer = resolveUser(userDetails);
            PurchaseResponse response = exchangeService.createPurchaseRequest(bookId, buyer);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    /** Buyer: view their purchase history */
    @GetMapping("/my-purchases")
    @PreAuthorize("hasRole('BUYER') or hasRole('ADMIN')")
    public List<PurchaseResponse> myPurchases(@AuthenticationPrincipal UserDetails userDetails) {
        User buyer = resolveUser(userDetails);
        return exchangeService.getMyPurchases(buyer);
    }

    /** Seller: view incoming purchase orders for their books */
    @GetMapping("/my-orders")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public List<PurchaseResponse> myOrders(@AuthenticationPrincipal UserDetails userDetails) {
        User seller = resolveUser(userDetails);
        return exchangeService.getIncomingOrders(seller);
    }

    /** Seller: accept or reject a purchase request */
    @PutMapping("/{requestId}/status")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long requestId,
            @RequestParam String status,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User seller = resolveUser(userDetails);
            PurchaseResponse response = exchangeService.updateStatus(requestId, status, seller);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    /** Admin: view all requests */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<PurchaseResponse> getAll() {
        return exchangeService.getAll();
    }

    private User resolveUser(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
