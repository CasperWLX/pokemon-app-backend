package com.hampus.projektuppgiftapi.repo;

import com.hampus.projektuppgiftapi.model.pokemon.Pokemon;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface IPokemonRepository extends ReactiveMongoRepository<Pokemon, String> {
    Mono<Pokemon> findByName(String name);
}
