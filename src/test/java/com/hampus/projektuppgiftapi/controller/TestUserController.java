package com.hampus.projektuppgiftapi.controller;

import com.hampus.projektuppgiftapi.config.SecurityConfig;
import com.hampus.projektuppgiftapi.model.user.AuthRequest;
import com.hampus.projektuppgiftapi.service.UserService;
import com.hampus.projektuppgiftapi.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(UserController.class)
@Import(SecurityConfig.class)
public class TestUserController {

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("Test: Username is too short")
    public void testInvalidUsername() {
        // Creating a test payload with invalid data
        AuthRequest invalidRequest = new AuthRequest();
        invalidRequest.setUsername(""); // Invalid username
        invalidRequest.setPassword("validPassword");

        // Sending POST request and asserting validation error response
        webTestClient.post()
                .uri("/user/v2/register")
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Test: Password is too short")
    public void testInvalidPassword() {
        AuthRequest invalidRequest = new AuthRequest();
        invalidRequest.setUsername("validUsername");
        invalidRequest.setPassword("abc"); // Invalid password (too short)

        webTestClient.post()
                .uri("/user/v2/register")
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }
}
