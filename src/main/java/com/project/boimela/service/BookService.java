package com.project.boimela.service;

import com.project.boimela.dto.BookRequest;
import com.project.boimela.dto.BookResponse;
import com.project.boimela.entity.Book;
import com.project.boimela.entity.User;
import com.project.boimela.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookResponse> getAllAvailableBooks() {
        return bookRepository.findByAvailableTrue().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<BookResponse> getBooksBySeller(User seller) {
        return bookRepository.findBySeller(seller).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public BookResponse getBookResponseById(Long id) {
        return toResponse(getBookById(id));
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    }

    public BookResponse createBook(BookRequest request, User seller) {
        Book book = new Book(
                request.getTitle(),
                request.getAuthor(),
                request.getDescription(),
                request.getPrice() != null ? request.getPrice() : 0.0,
                seller
        );
        return toResponse(bookRepository.save(book));
    }

    public void markUnavailable(Long bookId) {
        Book book = getBookById(bookId);
        book.setAvailable(false);
        bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public BookResponse toResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getDescription(),
                book.getPrice(),
                book.isAvailable(),
                book.getSeller().getUsername()
        );
    }
}
