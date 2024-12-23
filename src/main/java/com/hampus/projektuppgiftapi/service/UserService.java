package com.hampus.projektuppgiftapi.service;

import com.hampus.projektuppgiftapi.exceptions.LogInException;
import com.hampus.projektuppgiftapi.exceptions.UserAlreadyExistsException;
import com.hampus.projektuppgiftapi.exceptions.UserNotFoundException;
import com.hampus.projektuppgiftapi.model.user.AuthRequest;
import com.hampus.projektuppgiftapi.model.user.CustomUser;
import com.hampus.projektuppgiftapi.model.user.UserRoles;
import com.hampus.projektuppgiftapi.repo.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserService {

    private final PasswordEncoder PASSWORD_ENCODER;
    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final IUserRepository USER_DATABASE;

    public UserService(IUserRepository databaseRepository, PasswordEncoder passwordEncoder) {
        this.USER_DATABASE = databaseRepository;
        this.PASSWORD_ENCODER = passwordEncoder;
    }

    public Mono<CustomUser> getUser(String username) {
        LOGGER.info("Fetching user: {} from database", username);
        return USER_DATABASE.findByUsername(username)
                .switchIfEmpty(Mono.error(new UserNotFoundException(username)));
    }

    public Mono<Void> saveUserToDB(AuthRequest authRequest) {
        LOGGER.info("Creating new user with name: {}", authRequest.getUsername());
        return usernameIsAlreadyTaken(authRequest.getUsername()).flatMap(exists -> {
            if (exists){
                LOGGER.error("A user with the name: {} already exists", authRequest.getUsername());
                return Mono.error(new UserAlreadyExistsException(authRequest.getUsername()));
            }
            return convertUserToDBUser(authRequest)
                    .flatMap(USER_DATABASE::save)
                    .doOnSuccess(user -> LOGGER.info("Fetched and saved : {}", user.getUsername()))
                    .then();
        });
    }

    public Mono<Boolean> usernameIsAlreadyTaken(String username) {
        return USER_DATABASE.existsByUsernameIgnoreCase(username);
    }

    public Mono<CustomUser> convertUserToDBUser(AuthRequest authRequest) {
        CustomUser customUser = new CustomUser()
                .setName(authRequest.getUsername())
                .setPassword(PASSWORD_ENCODER.encode(authRequest.getPassword()))
                .setRole(UserRoles.USER);
        return Mono.just(customUser);
    }

    public Mono<UsernamePasswordAuthenticationToken> authenticate(String username, String password) {
        LOGGER.info("Authenticating: {}", username);

        return getUser(username)
                .flatMap(user -> {
                    if (PASSWORD_ENCODER.matches(password, user.getPassword())) {
                        return Mono.just(new UsernamePasswordAuthenticationToken(
                                user.getUsername(), user.getPassword(), user.getAuthorities()));
                    } else {
                        return Mono.error(new LogInException(username));
                    }
                });
    }


    public Mono<Void> deleteUser(String username) {
        LOGGER.info("Trying to delete user: {}", username);
        return getUser(username).flatMap(USER_DATABASE::delete).doOnSuccess(_ -> LOGGER.info("Deleted user {}", username));
    }

    public Mono<CustomUser> updateUser(String username, List<String> guessedPokemon) {
        LOGGER.info("Trying to update user: {}", username);
        return getUser(username).flatMap(user -> {
            user.setBestAttempt(guessedPokemon.size());
            user.setNumberOfAttempts(guessedPokemon.size());
            for (String pokemon : guessedPokemon) {
                user.addGuess(pokemon);
            }
            return USER_DATABASE.save(user);
        });
    }
}
