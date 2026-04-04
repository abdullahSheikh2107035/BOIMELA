package com.project.boimela;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.boimela.dto.BookRequest;
import com.project.boimela.entity.User;
import com.project.boimela.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Integration Test 1: Get all books using MockMvc
    @Test
    @WithMockUser(username = "user1")
    void shouldReturnOkWhenGettingAllBooks() throws Exception {
        mockMvc.perform(get("/api/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // Integration Test 2: Unauthenticated user gets 4xx for protected dashboard UI
    @Test
    void shouldRedirectToLoginWhenAccessingProtectedUI() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().is3xxRedirection());
    }

    // Integration Test 3: Authenticated buyer cannot POST (Create Book)
    @Test
    @WithMockUser(username = "buyer1", authorities = {"ROLE_BUYER"})
    void shouldReturnForbiddenWhenBuyerCreatesBook() throws Exception {
        BookRequest req = new BookRequest();
        req.setTitle("Test Title");
        req.setAuthor("Author");
        req.setPrice(10.0);
        
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());
    }
}
