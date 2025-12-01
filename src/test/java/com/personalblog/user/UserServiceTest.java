package com.personalblog.user;

import com.personalblog.PersonalBlogApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PersonalBlogApplication.class)
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterUserWithEncodedPassword() {
        User user = userService.registerUser("testuser", "password123", "test@example.com", "USER");

        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("USER", user.getRole());
        assertTrue(passwordEncoder.matches("password123", user.getPassword()));
    }

    @Test
    void shouldThrowExceptionForDuplicateUsername() {
        userService.registerUser("duplicate", "password123", "user1@example.com", "USER");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser("duplicate", "password456", "user2@example.com", "USER");
        });

        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForDuplicateEmail() {
        userService.registerUser("user1", "password123", "duplicate@example.com", "USER");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser("user2", "password456", "duplicate@example.com", "USER");
        });

        assertEquals("Email already exists", exception.getMessage());
    }

    @Test
    void shouldFindUserByUsername() {
        userService.registerUser("findme", "password123", "findme@example.com", "USER");

        User found = userService.findByUsername("findme");

        assertNotNull(found);
        assertEquals("findme", found.getUsername());
    }

    @Test
    void shouldReturnNullForNonExistentUsername() {
        User found = userService.findByUsername("nonexistent");
        assertNull(found);
    }
}
