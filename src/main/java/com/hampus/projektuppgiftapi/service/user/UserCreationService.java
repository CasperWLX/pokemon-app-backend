package com.hampus.projektuppgiftapi.service.user;

import com.hampus.projektuppgiftapi.exceptions.user.UserAlreadyExistsException;
import com.hampus.projektuppgiftapi.model.user.AuthRequest;
import com.hampus.projektuppgiftapi.model.user.CustomUser;
import com.hampus.projektuppgiftapi.model.user.UserRoles;
import com.hampus.projektuppgiftapi.repo.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserCreationService implements IUserCreation {
    private final Logger LOGGER = LoggerFactory.getLogger(UserCreationService.class);
    private final PasswordEncoder PASSWORD_ENCODER;
    private final IUserRepository USER_DATABASE;

    public UserCreationService(PasswordEncoder passwordEncoder, IUserRepository repository) {
        this.PASSWORD_ENCODER = passwordEncoder;
        this.USER_DATABASE = repository;
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
}
