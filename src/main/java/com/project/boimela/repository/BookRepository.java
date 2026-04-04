package com.project.boimela.repository;

import com.project.boimela.entity.Book;
import com.project.boimela.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findBySeller(User seller);
    List<Book> findByAvailableTrue();
}
