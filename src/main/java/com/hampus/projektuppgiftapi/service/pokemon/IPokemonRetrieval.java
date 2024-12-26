package com.hampus.projektuppgiftapi.service.pokemon;

import com.hampus.projektuppgiftapi.model.pokemon.Pokemon;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IPokemonRetrieval {
    Mono<Pokemon> getPokemonByIdentifier(String identifier);
    Mono<Pokemon> getPokemonById(int id);
    Mono<Pokemon> getPokemonByName(String name);
    Flux<Pokemon> getAllPokemon();
}
