package com.hampus.projektuppgiftapi.service.pokemon;

import com.hampus.projektuppgiftapi.repo.IPokemonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PokemonModificationService implements IPokemonModification {

    private final Logger LOGGER = LoggerFactory.getLogger(PokemonModificationService.class);
    private final IPokemonRepository POKEMON_REPO;
    private final IPokemonRetrieval POKEMON_RETRIEVAL;

    @Autowired
    public PokemonModificationService(IPokemonRepository pokemonRepository, IPokemonRetrieval pokemonRetrieval) {
        this.POKEMON_REPO = pokemonRepository;
        this.POKEMON_RETRIEVAL = pokemonRetrieval;
    }

    public Mono<Boolean> deleteByName(String name) {
        LOGGER.info("Deleting pokemon with name: {}", name);
        return POKEMON_RETRIEVAL.getPokemonByName(name).flatMap(POKEMON_REPO::delete).then(Mono.just(true));
    }

    public Mono<Boolean> deleteAll(){
        LOGGER.info("Deleting all Pokemon");
        return POKEMON_REPO.deleteAll().then(Mono.just(true));
    }
}
