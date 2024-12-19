package com.hampus.projektuppgiftapi.service;

import com.hampus.projektuppgiftapi.exceptions.UserAlreadyExistsException;
import com.hampus.projektuppgiftapi.exceptions.UserNotFoundException;
import com.hampus.projektuppgiftapi.model.user.AuthRequest;
import com.hampus.projektuppgiftapi.model.user.CustomUser;
import com.hampus.projektuppgiftapi.model.user.UserRoles;
import com.hampus.projektuppgiftapi.repo.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
        LOGGER.info("Trying to retrieve info about: {}", username);
        return USER_DATABASE.findByUsername(username)
                .switchIfEmpty(Mono.error(new UserNotFoundException(username)))
                .doOnSuccess(_ -> LOGGER.info("Fetched {} info from database", username));
    }

    public Mono<Void> saveUserToDB(AuthRequest authRequest) {
        LOGGER.info("Creating new user with name: {}", authRequest.getUsername());
        return USER_DATABASE.existsByUsernameIgnoreCase(authRequest.getUsername()).flatMap(exists -> {
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

    public Mono<CustomUser> convertUserToDBUser(AuthRequest authRequest) {
        CustomUser customUser = new CustomUser()
                .setName(authRequest.getUsername())
                .setPassword(PASSWORD_ENCODER.encode(authRequest.getPassword()))
                .setRole(UserRoles.USER);
        return Mono.just(customUser);
    }

    public Mono<Authentication> authenticate(String username, String password) {
        LOGGER.info("Trying to log in user: {}", username);
        return getUser(username).filter(user -> PASSWORD_ENCODER.matches(password, user.getPassword()))
                .map(user -> new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities()));
    }

    public Mono<Void> deleteUser(String username) {
        LOGGER.info("Trying to delete user: {}", username);
        return getUser(username).flatMap(USER_DATABASE::delete).doOnSuccess(_ -> LOGGER.info("Deleted user {}", username));
    }

    public Mono<CustomUser> updateUser(String username, int noOfAttempts) {
        return getUser(username).flatMap(user -> {
            user.setBestAttempt(noOfAttempts);
            user.setNumberOfAttempts(noOfAttempts);
            return USER_DATABASE.save(user);
        });
    }
}
