package com.hampus.projektuppgiftapi.service.api;

import com.hampus.projektuppgiftapi.exceptions.pokemon.PokemonCreationException;
import com.hampus.projektuppgiftapi.exceptions.pokemon.PokemonUpdateException;
import com.hampus.projektuppgiftapi.model.pokemon.Pokemon;
import com.hampus.projektuppgiftapi.model.pokemon.PokemonDTO;
import com.hampus.projektuppgiftapi.service.pokemon.IPokemonCreation;
import com.hampus.projektuppgiftapi.util.api.IApiUtils;
import com.hampus.projektuppgiftapi.util.pokemon.IPokemonUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ApiService implements IApiRetrieval {

    private final Logger LOGGER = LoggerFactory.getLogger(ApiService.class);
    private final IPokemonCreation POKEMON_CREATION;
    private final WebClient WEB_CLIENT;
    private final IPokemonUtils DATABASE_UTIL;
    private final IApiUtils API_UTIL;

    @Autowired
    public ApiService(IPokemonCreation creation, WebClient.Builder webClientBuilder, IPokemonUtils dbUtil, IApiUtils apiUtil) {
        this.POKEMON_CREATION = creation;
        this.DATABASE_UTIL = dbUtil;
        this.API_UTIL = apiUtil;
        this.WEB_CLIENT = webClientBuilder
                .baseUrl("https://pokeapi.co/api/v2/pokemon")
                .codecs(clientCodecConfigurer -> clientCodecConfigurer
                                .defaultCodecs()
                                .maxInMemorySize(1024 * 1024))
                .build();
    }

    public Mono<Boolean> fetchAndSaveFirst151Pokemon() {
        LOGGER.info("Fetching and saving first gen");
        return DATABASE_UTIL.isDatabaseEmpty().flatMap(isEmpty -> {
            if (isEmpty) {
                return API_UTIL.getPokemonIds()
                        .flatMap(this::fetchPokemonFromApiById)
                        .collectList()
                        .flatMapMany(this::processPokemonList)
                        .onErrorResume(_ -> Mono.error(new PokemonCreationException("Could not save pokemon")))
                        .then(Mono.just(true));
            }
            return Mono.error(new PokemonUpdateException("Database is already populated"));
        });
    }

    public Mono<PokemonDTO> fetchPokemonFromApiById(int id){
        LOGGER.info("Fetching one pokemon with id {}", id);
        return WEB_CLIENT.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(PokemonDTO.class)
                .onErrorResume(error -> {
                    LOGGER.info("Failed to fetch pokemon with id {}: {}", id, error.getMessage());
                    return Mono.empty();
                });
    }

    private Flux<Pokemon> processPokemonList(List<PokemonDTO> pokemonList) {
        LOGGER.info("Processing Pokémon list");
        List<PokemonDTO> sortedPokemon = API_UTIL.sortPokemonById(pokemonList);
        List<PokemonDTO> updatedPokemon = API_UTIL.setEvolutionStage(sortedPokemon);
        return savePokemonToDatabaseInOrder(updatedPokemon);
    }

    private Flux<Pokemon> savePokemonToDatabaseInOrder(List<PokemonDTO> pokemonList) {
        LOGGER.info("Saving Pokémon list to DB");

        return Flux.fromIterable(pokemonList)
                .concatMap(POKEMON_CREATION::saveOnePokemonToDB) // Ensure order is preserved
                .onErrorContinue((error, pokemon) ->
                        LOGGER.error("Failed to save Pokémon: {}: {}", pokemon, error.getMessage())
                );
    }
}