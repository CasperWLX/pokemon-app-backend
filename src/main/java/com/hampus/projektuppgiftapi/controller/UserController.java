package com.hampus.projektuppgiftapi.controller;

import com.hampus.projektuppgiftapi.model.user.AuthRequestDTO;
import com.hampus.projektuppgiftapi.model.user.CustomUser;
import com.hampus.projektuppgiftapi.service.UserService;
import com.hampus.projektuppgiftapi.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/user/v1")
@CrossOrigin(origins = {"http://localhost:5173", "https://gamestation.hampuskallberg.se"})
public class UserController {

    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService USER_SERVICE;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtils) {
        this.USER_SERVICE = userService;
        this.jwtUtil = jwtUtils;
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<Void>> createNewUser(@RequestBody AuthRequestDTO authRequest) {
        LOGGER.info("Creating new user with name: {}", authRequest.getUsername());
        return USER_SERVICE.saveUserToDB(authRequest)
                .then(Mono.just(ResponseEntity.status(HttpStatus.CREATED).build()));
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<Void>> login(@RequestBody AuthRequestDTO authRequest) {
        LOGGER.info("Trying to log in user: {}", authRequest.getUsername());

        return USER_SERVICE.authenticate(authRequest.getUsername(), authRequest.getPassword())
                .flatMap(authentication -> USER_SERVICE.getUser(authRequest.getUsername())
                        .flatMap(user -> {
                            String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

                            ResponseCookie jwtCookie = ResponseCookie.from("jwtToken", token)
                                    .httpOnly(true)
                                    .secure(true)
                                    .sameSite("None")
                                    .path("/")
                                    .maxAge(60 * 60 * 24) // 24 hours
                                    .build();

                            LOGGER.info("Successfully authenticated user: {}", authRequest.getUsername());

                            return Mono.just(ResponseEntity.ok()
                                    .header("Set-Cookie", jwtCookie.toString())
                                    .build()
                            );
                        })
                );
    }

    @DeleteMapping("/delete/{username}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable String username) {
        LOGGER.info("Trying to delete user");
        return USER_SERVICE.deleteUser(username).then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).build()));
    }

    @GetMapping("/info")
    public Mono<ResponseEntity<CustomUser>> getUserInfo(@AuthenticationPrincipal String username) {
        LOGGER.info("Trying to retrieve user info");
        return USER_SERVICE.getUser(username).map(ResponseEntity::ok);
    }

}

