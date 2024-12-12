package com.hampus.projektuppgiftapi.repo;

import com.hampus.projektuppgiftapi.model.user.CustomUser;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface IUserRepository extends ReactiveMongoRepository<CustomUser, String> {
    Mono<CustomUser> findByUsername(String username);
    Mono<Boolean> existsByUsernameIgnoreCase(String username);
}