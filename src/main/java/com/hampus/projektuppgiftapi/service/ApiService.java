package com.hampus.projektuppgiftapi.service;

import com.hampus.projektuppgiftapi.components.EvolutionTracker;
import com.hampus.projektuppgiftapi.exceptions.PokemonCreationException;
import com.hampus.projektuppgiftapi.exceptions.PokemonUpdateException;
import com.hampus.projektuppgiftapi.model.pokemon.Pokemon;
import com.hampus.projektuppgiftapi.model.pokemon.PokemonDTO;
import com.hampus.projektuppgiftapi.repo.IPokemonRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;

@Service
public class ApiService {

    private final Logger LOGGER = LoggerFactory.getLogger(ApiService.class);
    private final PokemonService DB_SERVICE;
    private final WebClient WEB_CLIENT;

    @Autowired
    public ApiService(PokemonService dbService, WebClient.Builder webClientBuilder) {
        this.DB_SERVICE = dbService;
        this.WEB_CLIENT = webClientBuilder
                .baseUrl("https://pokeapi.co/api/v2/pokemon")
                .codecs(clientCodecConfigurer -> clientCodecConfigurer
                                .defaultCodecs()
                                .maxInMemorySize(1024 * 1024))
                .build();
    }

    public Mono<Boolean> fetchAndSaveFirst151Pokemon() {
        return DB_SERVICE.databaseIsPopulated().flatMap(isPopulated -> {
            if (isPopulated) {
                return populateDBWithPokemon()
                        .onErrorResume(error -> Mono.error(new PokemonCreationException("Could not save pokemon")))
                        .then(Mono.just(true));
            }
            return Mono.error(new PokemonUpdateException("Database is already populated"));
        });
    }

    public Mono<PokemonDTO> fetchPokemon(int id){
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
        return Flux.range(1, 151);
    }

    public Flux<Pokemon> populateDBWithPokemon(){
        return getPokemonIds()
                .flatMap(this::fetchPokemon)
                .collectList()
                .flatMapMany(pokemonList -> {
                    pokemonList.sort(Comparator.comparingInt(PokemonDTO::getId));
                    System.out.println(pokemonList);
                    EvolutionTracker evolutionContext = new EvolutionTracker();
                    return Flux.fromIterable(pokemonList)
                            .map(pokemonDTO -> {
                                int evolutionStage = evolutionContext.calculateEvolutionStage(pokemonDTO);
                                pokemonDTO.setEvolutionStage(evolutionStage);
                                return pokemonDTO;
                            })
                            .concatMap(DB_SERVICE::saveOnePokemonToDB) //Concat map to save them in correct order
                            .onErrorContinue((error, pokemon) ->
                                    LOGGER.error("Failed to save Pokémon: {}: {}", pokemon, error.getMessage())
                            );
                });

    }
}