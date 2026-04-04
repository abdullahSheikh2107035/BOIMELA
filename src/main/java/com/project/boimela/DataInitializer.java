package com.project.boimela;

import com.project.boimela.entity.Book;
import com.project.boimela.entity.Role;
import com.project.boimela.entity.RoleName;
import com.project.boimela.entity.User;
import com.project.boimela.repository.BookRepository;
import com.project.boimela.repository.RoleRepository;
import com.project.boimela.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           BookRepository bookRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // 1. Seed roles
        for (RoleName roleName : RoleName.values()) {
            if (roleRepository.findByName(roleName).isEmpty()) {
                roleRepository.save(new Role(roleName));
            }
        }

        // 2. Seed a demo seller account
        if (!userRepository.existsByUsername("bookseller")) {
            Role sellerRole = roleRepository.findByName(RoleName.ROLE_SELLER).orElseThrow();
            User seller = new User("bookseller", "seller@boimela.com",
                    passwordEncoder.encode("seller123"));
            seller.setRoles(Set.of(sellerRole));
            userRepository.save(seller);

            // 3. Seed demo books under that seller
            if (bookRepository.count() == 0) {
                bookRepository.save(new Book("The Great Gatsby", "F. Scott Fitzgerald",
                        "A classic American novel set in the Jazz Age.", 12.99, seller));
                bookRepository.save(new Book("To Kill a Mockingbird", "Harper Lee",
                        "A powerful story about racial injustice and childhood.", 9.99, seller));
                bookRepository.save(new Book("1984", "George Orwell",
                        "A dystopian novel about totalitarianism and surveillance.", 11.50, seller));
                bookRepository.save(new Book("The Alchemist", "Paulo Coelho",
                        "A philosophical novel about following your dreams.", 10.00, seller));
                bookRepository.save(new Book("Dune", "Frank Herbert",
                        "An epic science fiction saga set on the desert planet Arrakis.", 14.99, seller));
                bookRepository.save(new Book("Harry Potter and the Sorcerer's Stone", "J.K. Rowling",
                        "The beginning of the magical journey of a young wizard.", 13.00, seller));
            }
        }

        // 4. Seed a demo buyer account
        if (!userRepository.existsByUsername("bookbuyer")) {
            Role buyerRole = roleRepository.findByName(RoleName.ROLE_BUYER).orElseThrow();
            User buyer = new User("bookbuyer", "buyer@boimela.com",
                    passwordEncoder.encode("buyer123"));
            buyer.setRoles(Set.of(buyerRole));
            userRepository.save(buyer);
        }
    }
}
