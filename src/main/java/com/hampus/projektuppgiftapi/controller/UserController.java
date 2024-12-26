package com.hampus.projektuppgiftapi.controller;

import com.hampus.projektuppgiftapi.model.token.RefreshTokenRequest;
import com.hampus.projektuppgiftapi.model.token.TokenResponse;
import com.hampus.projektuppgiftapi.model.user.AuthRequest;
import com.hampus.projektuppgiftapi.model.user.CustomUser;
import com.hampus.projektuppgiftapi.model.user.UpdateRequest;
import com.hampus.projektuppgiftapi.service.user.*;
import com.hampus.projektuppgiftapi.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/user/v2")
@Tag(name = "User Database", description = "Endpoints for User and Database interaction")
public class UserController {

    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final IUserAuth USER_AUTH;
    private final IUserCreation USER_CREATION;
    private final IUserModification USER_MODIFICATION;
    private final IUserRetrieval USER_RETRIEVAL;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(JwtUtil jwtUtils, IUserAuth auth, IUserCreation creation, IUserModification modification, IUserRetrieval retrieval) {
        this.jwtUtil = jwtUtils;
        USER_AUTH = auth;
        USER_CREATION = creation;
        USER_MODIFICATION = modification;
        USER_RETRIEVAL = retrieval;
    }

    @PostMapping("/register")
    @Operation(summary = "Register new User", description = "Post a new User to the Database")
    public Mono<ResponseEntity<Void>> createNewUser(@Valid @RequestBody AuthRequest authRequest) {
        return USER_CREATION.saveUserToDB(authRequest)
                .then(Mono.just(ResponseEntity.status(HttpStatus.CREATED).build()));
    }

    @PostMapping("/login")
    @Operation(summary = "Logs in a user", description = "Authenticates user and returns access + refresh token if successfully completed")
    public Mono<ResponseEntity<TokenResponse>> login(@RequestBody AuthRequest authRequest) {
        return USER_AUTH.authenticate(authRequest.getUsername(), authRequest.getPassword())
                .flatMap(_ -> USER_RETRIEVAL.getUser(authRequest.getUsername())
                                .flatMap(user -> {
                                    String accessToken = jwtUtil.generateToken(user.getUsername(), user.getRole());
                                    String refreshToken = jwtUtil.generateRefreshToken(user.getUsername(), user.getRole());

                                    return Mono.just(ResponseEntity.ok(new TokenResponse(accessToken, refreshToken)));
                                })
                );
    }


    @DeleteMapping("/delete/{username}")
    @Operation(summary = "Delete a User", description = "Deletes a User by username")
    public Mono<ResponseEntity<Boolean>> deleteUser(@PathVariable String username) {
        return USER_MODIFICATION.deleteByName(username).then(Mono.just(ResponseEntity.noContent().build()));
    }

    @DeleteMapping("/delete/all")
    @Operation(summary = "Deletes all users", description = "Deletes all users from the user database")
    public Mono<ResponseEntity<Boolean>> deleteAllUsers() {
        return USER_MODIFICATION.deleteAll().then(Mono.just(ResponseEntity.noContent().build()));
    }

    @GetMapping("/info")
    @Operation(summary = "Get User info", description = "Fetches all info about a User")
    public Mono<ResponseEntity<CustomUser>> getUserInfo(@AuthenticationPrincipal String username) {
        return USER_RETRIEVAL.getUser(username).map(ResponseEntity::ok);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refreshes JWT token", description = "Generates a new JWT token for the user")
    public Mono<ResponseEntity<TokenResponse>> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();

        if (!jwtUtil.validateToken(refreshToken)) {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }

        String newJwtToken = jwtUtil.generateToken(jwtUtil.getUsername(refreshToken), jwtUtil.extractRoles(refreshToken));
        String newRefreshToken = jwtUtil.generateRefreshToken(jwtUtil.getUsername(refreshToken), jwtUtil.extractRoles(refreshToken));
        LOGGER.info("Issued new jwt and refresh token for user: {}", jwtUtil.getUsername(refreshToken));
        return Mono.just(ResponseEntity.ok(new TokenResponse(newJwtToken, newRefreshToken)));
    }

    @PutMapping("/update")
    @Operation(summary = "Updates the user", description = "Updates a user with new information")
    public Mono<ResponseEntity<CustomUser>> updateUser(@AuthenticationPrincipal String username, @RequestBody UpdateRequest updateRequest) {
        return USER_MODIFICATION.updateUser(username, updateRequest.getGuessedPokemon()).map(ResponseEntity::ok);
    }
}

