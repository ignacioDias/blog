package com.personalblog.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            String role = request.getRole() != null ? request.getRole() : "USER";
            User user = userService.registerUser(
                request.getUsername(), 
                request.getPassword(), 
                request.getEmail(),
                role
            );
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse("User registered successfully", user.getUsername()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new AuthResponse(e.getMessage(), null));
        }
    }

    // DTOs
    public static class RegisterRequest {
        private String username;
        private String password;
        private String email;
        private String role;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }

    public static class AuthResponse {
        private String message;
        private String username;

        public AuthResponse(String message, String username) {
            this.message = message;
            this.username = username;
        }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
    }
}
