package com.project.boimela.controller;

import com.project.boimela.dto.BookRequest;
import com.project.boimela.dto.BookResponse;
import com.project.boimela.entity.User;
import com.project.boimela.repository.UserRepository;
import com.project.boimela.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final UserRepository userRepository;

    public BookController(BookService bookService, UserRepository userRepository) {
        this.bookService = bookService;
        this.userRepository = userRepository;
    }

    /** Public: all available books for the marketplace */
    @GetMapping
    public List<BookResponse> getAvailableBooks() {
        return bookService.getAllAvailableBooks();
    }

    /** Public: single book detail */
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookResponseById(id));
    }

    /** Seller: list their own books (all statuses) */
    @GetMapping("/mine")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public List<BookResponse> getMyBooks(@AuthenticationPrincipal UserDetails userDetails) {
        User seller = resolveUser(userDetails);
        return bookService.getBooksBySeller(seller);
    }

    /** Seller: list a new book for sale */
    @PostMapping
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<BookResponse> listBook(
            @RequestBody BookRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User seller = resolveUser(userDetails);
        return ResponseEntity.ok(bookService.createBook(request, seller));
    }

    /** Seller/Admin: remove a book */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteBook(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }

    private User resolveUser(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
