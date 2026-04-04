package com.project.boimela;

import com.project.boimela.dto.BookRequest;
import com.project.boimela.dto.BookResponse;
import com.project.boimela.dto.PurchaseResponse;
import com.project.boimela.entity.Book;
import com.project.boimela.entity.User;
import com.project.boimela.entity.ExchangeRequest;
import com.project.boimela.repository.BookRepository;
import com.project.boimela.repository.UserRepository;
import com.project.boimela.repository.ExchangeRequestRepository;
import com.project.boimela.service.BookService;
import com.project.boimela.service.UserService;
import com.project.boimela.service.ExchangeRequestService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.List;
import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
class UnitTests {

    @Mock BookRepository bookRepository;
    @Mock UserRepository userRepository;
    @Mock ExchangeRequestRepository exchangeRepo;

    @InjectMocks BookService bookService;
    @InjectMocks ExchangeRequestService exchangeService;

    private User createSeller() {
        User user = new User();
        user.setId(1L);
        user.setUsername("seller");
        return user;
    }

    private Book createBook() {
        Book book = new Book("Title", "Author", "Desc", 10.0, createSeller());
        book.setId(1L);
        return book;
    }

    @Test
    void testGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(createBook(), createBook()));
        assertEquals(2, bookService.getAllBooks().size());
    }

    @Test
    void testGetBookByIdFound() {
        Book book = createBook();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        assertEquals("Title", bookService.getBookById(1L).getTitle());
    }

    @Test
    void testGetBookByIdNotFound() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> bookService.getBookById(2L));
    }

    @Test
    void testCreateBook() {
        BookRequest req = new BookRequest();
        req.setTitle("Spring");
        req.setAuthor("Dev");
        req.setPrice(15.0);
        
        Book savedBook = new Book("Spring", "Dev", null, 15.0, createSeller());
        savedBook.setId(1L);
        
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);
        BookResponse res = bookService.createBook(req, createSeller());
        assertEquals("Spring", res.getTitle());
    }

    @Test
    void testDeleteBook() {
        doNothing().when(bookRepository).deleteById(1L);
        bookService.deleteBook(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetAllExchangeRequests() {
        ExchangeRequest req = new ExchangeRequest(createBook(), new User(), "PENDING");
        when(exchangeRepo.findAll()).thenReturn(Arrays.asList(req));
        assertEquals(1, exchangeService.getAll().size());
    }

    @Test
    void testUpdateExchangeRequestStatusNotFound() {
        when(exchangeRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> exchangeService.updateStatus(1L, "REJECTED", createSeller()));
    }

    @Test
    void testUserEntity() {
        User user = new User("admin", "admin@admin.com", "pass");
        assertEquals("admin", user.getUsername());
    }

    @Test
    void testBookEntity() {
        Book book = new Book("Harry Potter", "J.K", "desc", 10.99, createSeller());
        assertEquals("Harry Potter", book.getTitle());
        assertEquals(10.99, book.getPrice());
    }

    @Test
    void testMessageResponse() {
        com.project.boimela.dto.MessageResponse resp = new com.project.boimela.dto.MessageResponse("Ok");
        assertEquals("Ok", resp.getMessage());
    }
}
