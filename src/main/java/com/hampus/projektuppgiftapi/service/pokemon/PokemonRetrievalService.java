package com.hampus.projektuppgiftapi.service.pokemon;

import com.hampus.projektuppgiftapi.exceptions.pokemon.PokemonNotFoundException;
import com.hampus.projektuppgiftapi.model.pokemon.Pokemon;
import com.hampus.projektuppgiftapi.repo.IPokemonRepository;
import com.hampus.projektuppgiftapi.util.pokemon.IPokemonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PokemonRetrievalService implements IPokemonRetrieval{

    private final Logger LOGGER = LoggerFactory.getLogger(PokemonRetrievalService.class);
    private final IPokemonRepository POKEMON_REPO;
    private final IPokemonUtils POKEMON_UTILS;

    @Autowired
    public PokemonRetrievalService(IPokemonRepository pokemonRepository, IPokemonUtils pokemonUtils) {
        this.POKEMON_REPO = pokemonRepository;
        this.POKEMON_UTILS = pokemonUtils;
    }

    public Mono<Pokemon> getPokemonByIdentifier(String identifier){
        LOGGER.info("Fetching pokemon from DB with identifier: {}", identifier );
        if (POKEMON_UTILS.isInteger(identifier)) {
            return getPokemonById(Integer.parseInt(identifier));
        }
        return getPokemonByName(identifier);
    }

    public Mono<Pokemon> getPokemonById(int id){
        return POKEMON_REPO.findByPokemonId(id).switchIfEmpty(Mono.error(new PokemonNotFoundException(String.valueOf(id))));
    }

    public Mono<Pokemon> getPokemonByName(String name){
        return POKEMON_REPO.findByName(name).switchIfEmpty(Mono.error(new PokemonNotFoundException(name)));
    }

    public Flux<Pokemon> getAllPokemon(){
        LOGGER.info("Fetching all Pokemon");
        return POKEMON_REPO.findAll()
                .doOnError(error -> LOGGER.error("Error fetching all games: {}", error.getMessage()))
                .switchIfEmpty(Mono.error(new PokemonNotFoundException("Database is empty")));
    }
}
