package com.personalblog.user;

import com.personalblog.PersonalBlogApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PersonalBlogApplication.class)
@Transactional
class AuthControllerTest {

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;
    
    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterNewUser() {
        User user = userService.registerUser("testuser", "password123", "test@example.com", "USER");

        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("USER", user.getRole());
        assertTrue(userRepository.existsByUsername("testuser"));
    }

    @Test
    void shouldRejectDuplicateUsername() {
        userService.registerUser("duplicate", "password123", "user1@example.com", "USER");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser("duplicate", "password456", "user2@example.com", "USER");
        });

        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    void shouldRejectDuplicateEmail() {
        userService.registerUser("user1", "password123", "duplicate@example.com", "USER");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser("user2", "password456", "duplicate@example.com", "USER");
        });

        assertEquals("Email already exists", exception.getMessage());
    }

    @Test
    void shouldRegisterWithDefaultRole() {
        User user = userService.registerUser("normaluser", "password123", "normal@example.com", "USER");

        assertNotNull(user);
        assertEquals("USER", user.getRole());
    }

    @Test
    void shouldRegisterAdminUser() {
        User user = userService.registerUser("adminuser", "password123", "admin@example.com", "ADMIN");

        assertNotNull(user);
        assertEquals("ADMIN", user.getRole());
    }
}
