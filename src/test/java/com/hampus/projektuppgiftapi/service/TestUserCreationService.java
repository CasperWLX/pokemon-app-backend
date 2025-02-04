package com.hampus.projektuppgiftapi.service;

import com.hampus.projektuppgiftapi.exceptions.user.UserAlreadyExistsException;
import com.hampus.projektuppgiftapi.model.user.AuthRequest;
import com.hampus.projektuppgiftapi.model.user.CustomUser;
import com.hampus.projektuppgiftapi.model.user.UserRoles;
import com.hampus.projektuppgiftapi.repo.IUserRepository;
import com.hampus.projektuppgiftapi.service.user.UserCreationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class TestUserCreationService {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserCreationService USER_CREATION;

    private AuthRequest authRequest;

    @BeforeEach
    public void setUp() {
        authRequest = new AuthRequest();
        authRequest.setUsername("tester");
        authRequest.setPassword("password");
    }

    @Test
    @DisplayName("Test: DTO converter works correctly")
    void testConvertAuthDTOtoCustomUserModel(){
        Mockito.when(passwordEncoder.encode(authRequest.getPassword())).thenReturn("encodedPassword");
        Mono<CustomUser> result = USER_CREATION.convertUserToDBUser(authRequest);

        StepVerifier.create(result)
                .expectNextMatches(customUser ->
                        customUser.getUsername().equals(authRequest.getUsername()) &&
                                customUser.getPassword().equals("encodedPassword") &&
                                customUser.getRole().equals(UserRoles.USER))
                .verifyComplete();
    }

    @Test
    @DisplayName("Test: Register when username is already taken")
    void testUsernameIsNotAvailable() {
        Mockito.when(userRepository.existsByUsernameIgnoreCase(authRequest.getUsername()))
                .thenReturn(Mono.just(true));

        Mono<Boolean> result = USER_CREATION.usernameIsAlreadyTaken(authRequest.getUsername());
        StepVerifier.create(result).expectNext(true).verifyComplete();

        Mono<Void> saveUserResult = USER_CREATION.saveUserToDB(authRequest);
        StepVerifier.create(saveUserResult).expectError(UserAlreadyExistsException.class).verify();
    }

    @Test
    @DisplayName("Test: Successfully register new user")
    void testRegisterNewUserWhenUsernameDoesNotExist() {

        Mockito.when(userRepository.existsByUsernameIgnoreCase(authRequest.getUsername()))
                .thenReturn(Mono.just(false)); // false means name is available

        Mockito.when(passwordEncoder.encode(authRequest.getPassword()))
                .thenReturn("encodedPassword");

        Mockito.when(userRepository.save(Mockito.any(CustomUser.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<Void> result = USER_CREATION.saveUserToDB(authRequest);

        StepVerifier.create(result)
                .expectComplete()
                .verify();
    }
}
