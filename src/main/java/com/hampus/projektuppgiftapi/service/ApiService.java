package com.hampus.projektuppgiftapi.service;

import com.hampus.projektuppgiftapi.components.EvolutionTracker;
import com.hampus.projektuppgiftapi.exceptions.PokemonCreationException;
import com.hampus.projektuppgiftapi.exceptions.PokemonUpdateException;
import com.hampus.projektuppgiftapi.model.pokemon.Pokemon;
import com.hampus.projektuppgiftapi.model.pokemon.PokemonDTO;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApiService {

    private final Logger LOGGER = LoggerFactory.getLogger(ApiService.class);
    private final PokemonService POKEMON_SERVICE;
    private final WebClient WEB_CLIENT;

    @Autowired
    public ApiService(PokemonService dbService, WebClient.Builder webClientBuilder) {
        this.POKEMON_SERVICE = dbService;
        this.WEB_CLIENT = webClientBuilder
                .baseUrl("https://pokeapi.co/api/v2/pokemon")
                .codecs(clientCodecConfigurer -> clientCodecConfigurer
                                .defaultCodecs()
                                .maxInMemorySize(1024 * 1024))
                .build();
    }

    public Mono<Boolean> fetchAndSaveFirst151Pokemon() {
        LOGGER.info("Fetching and saving first gen");
        return POKEMON_SERVICE.databaseIsPopulated().flatMap(isPopulated -> {
            if (isPopulated) {
                return populateDBWithPokemon()
                        .onErrorResume(error -> Mono.error(new PokemonCreationException("Could not save pokemon")))
                        .then(Mono.just(true));
            }
            return Mono.error(new PokemonUpdateException("Database is already populated"));
        });
    }

    public Mono<PokemonDTO> fetchPokemon(int id){
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

    public Flux<Integer> getPokemonIds(){
        LOGGER.info("Generating IDs");
        return Flux.range(1, 151);
    }

    public Flux<Pokemon> populateDBWithPokemon() {
        LOGGER.info("Populating DB with Pokemon");

        return getPokemonIds()
                .flatMap(this::fetchPokemon)
                .collectList()
                .flatMapMany(this::processAndSavePokemon);
    }

    private Flux<Pokemon> processAndSavePokemon(List<PokemonDTO> pokemonList) {
        LOGGER.info("Processing Pokémon list before saving to DB");
        List<PokemonDTO> sortedPokemon = sortPokemonById(pokemonList);
        List<PokemonDTO> updatedPokemon = setEvolutionStage(sortedPokemon);
        return savePokemonListToDB(updatedPokemon);
    }

    private List<PokemonDTO> sortPokemonById(List<PokemonDTO> pokemonList) {
        LOGGER.info("Sorting Pokémon list");
        pokemonList.sort(Comparator.comparingInt(PokemonDTO::getId));
        return pokemonList;
    }

    private List<PokemonDTO> setEvolutionStage(List<PokemonDTO> pokemonList) {
        LOGGER.info("Setting Evolution Stages for Pokémon");
        EvolutionTracker evolutionTracker = new EvolutionTracker();
        return pokemonList.stream()
                .peek(pokemonDTO -> {
                    int evolutionStage = evolutionTracker.calculateEvolutionStage(pokemonDTO);
                    pokemonDTO.setEvolutionStage(evolutionStage);
                })
                .collect(Collectors.toList());
    }

    private Flux<Pokemon> savePokemonListToDB(List<PokemonDTO> pokemonList) {
        LOGGER.info("Saving Pokémon list to DB");

        return Flux.fromIterable(pokemonList)
                .concatMap(POKEMON_SERVICE::saveOnePokemonToDB) // Ensure order is preserved
                .onErrorContinue((error, pokemon) ->
                        LOGGER.error("Failed to save Pokémon: {}: {}", pokemon, error.getMessage())
                );
    }
}