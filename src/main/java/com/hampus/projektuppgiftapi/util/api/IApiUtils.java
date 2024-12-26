package com.hampus.projektuppgiftapi.util.api;

import com.hampus.projektuppgiftapi.model.pokemon.PokemonDTO;
import reactor.core.publisher.Flux;

import java.util.List;

public interface IApiUtils {
    List<PokemonDTO> setEvolutionStage(List<PokemonDTO> pokemonList);
    List<PokemonDTO> sortPokemonById(List<PokemonDTO> pokemonList);
    Flux<Integer> getPokemonIds();
}
