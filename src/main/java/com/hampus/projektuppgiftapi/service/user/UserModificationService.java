package com.hampus.projektuppgiftapi.service.user;

import com.hampus.projektuppgiftapi.model.user.CustomUser;
import com.hampus.projektuppgiftapi.repo.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserModificationService implements IUserModification {

    private final Logger LOGGER = LoggerFactory.getLogger(UserModificationService.class);
    private final IUserRetrieval USER_RETRIEVAL;
    private final IUserRepository USER_DATABASE;

    public UserModificationService(IUserRetrieval userRetrieval, IUserRepository userRepository) {
        this.USER_RETRIEVAL = userRetrieval;
        this.USER_DATABASE = userRepository;
    }

    public Mono<CustomUser> updateUser(String username, List<String> guessedPokemon) {
        LOGGER.info("Trying to update user: {}", username);
        return USER_RETRIEVAL.getUser(username).flatMap(user -> {
            user.setBestAttempt(guessedPokemon.size());
            user.setNumberOfAttempts(guessedPokemon.size());
            for (String pokemon : guessedPokemon) {
                user.addGuess(pokemon);
            }
            return USER_DATABASE.save(user);
        });
    }

    public Mono<Boolean> deleteByName(String username) {
        LOGGER.info("Trying to delete user: {}", username);
        return USER_RETRIEVAL.getUser(username).flatMap(USER_DATABASE::delete).doOnSuccess(_ -> LOGGER.info("Deleted user {}", username)).then(Mono.just(true));
    }

    public Mono<Boolean> deleteAll(){
        LOGGER.info("Trying to delete all users");
        return USER_DATABASE.deleteAll().doOnSuccess(_ -> LOGGER.info("Deleted all users")).then(Mono.just(true));
    }
}
