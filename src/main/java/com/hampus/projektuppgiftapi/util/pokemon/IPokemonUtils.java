package com.hampus.projektuppgiftapi.util.pokemon;

import com.hampus.projektuppgiftapi.model.pokemon.Pokemon;
import com.hampus.projektuppgiftapi.model.pokemon.PokemonDTO;
import reactor.core.publisher.Mono;

public interface IPokemonUtils {
    Pokemon convertToPokemonModel(PokemonDTO pokemonDTO);
    boolean isInteger(String input);
    String capitalizeFirstLetter(String input);
    Mono<Boolean> isDatabaseEmpty();
}
