package com.hampus.projektuppgiftapi.util.api;

import com.hampus.projektuppgiftapi.model.pokemon.PokemonDTO;
import com.hampus.projektuppgiftapi.util.pokemon.EvolutionTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ApiUtils implements IApiUtils {

    private final Logger LOGGER = LoggerFactory.getLogger(ApiUtils.class);

    public List<PokemonDTO> sortPokemonById(List<PokemonDTO> pokemonList) {
        LOGGER.info("Sorting Pokémon list");
        pokemonList.sort(Comparator.comparingInt(PokemonDTO::getId));
        return pokemonList;
    }

    public Flux<Integer> getPokemonIds(){
        LOGGER.info("Generating IDs");
        return Flux.range(1, 151);
    }

    public List<PokemonDTO> setEvolutionStage(List<PokemonDTO> pokemonList) {
        LOGGER.info("Setting Evolution Stages for Pokémon");
        EvolutionTracker evolutionTracker = new EvolutionTracker();
        return pokemonList.stream()
                .peek(pokemonDTO -> {
                    int evolutionStage = evolutionTracker.calculateEvolutionStage(pokemonDTO);
                    pokemonDTO.setEvolutionStage(evolutionStage);
                })
                .collect(Collectors.toList());
    }
}
