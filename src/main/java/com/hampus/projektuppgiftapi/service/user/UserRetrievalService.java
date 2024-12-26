package com.hampus.projektuppgiftapi.service.user;

import com.hampus.projektuppgiftapi.exceptions.GlobalExceptionHandler;
import com.hampus.projektuppgiftapi.model.user.CustomUser;
import com.hampus.projektuppgiftapi.repo.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserRetrievalService implements IUserRetrieval {
    private final Logger LOGGER = LoggerFactory.getLogger(UserRetrievalService.class);
    private final IUserRepository USER_DATABASE;

    public UserRetrievalService(IUserRepository userRepository) {
        this.USER_DATABASE = userRepository;
    }

    public Mono<CustomUser> getUser(String username) {
        LOGGER.info("Fetching user: {} from database", username);
        return USER_DATABASE.findByUsername(username)
                .switchIfEmpty(Mono.error(new GlobalExceptionHandler.UserNotFoundException(username)));
    }
}
