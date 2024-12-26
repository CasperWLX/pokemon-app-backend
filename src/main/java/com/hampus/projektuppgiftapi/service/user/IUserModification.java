package com.hampus.projektuppgiftapi.service.user;

import com.hampus.projektuppgiftapi.model.user.CustomUser;
import com.hampus.projektuppgiftapi.service.IDatabaseDeleteOperations;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IUserModification extends IDatabaseDeleteOperations {
    Mono<CustomUser> updateUser(String username, List<String> guessedPokemon);

}
