package com.hampus.projektuppgiftapi.service.pokemon;

import com.hampus.projektuppgiftapi.exceptions.pokemon.PokemonCreationException;
import com.hampus.projektuppgiftapi.model.pokemon.Pokemon;
import com.hampus.projektuppgiftapi.model.pokemon.PokemonDTO;
import reactor.core.publisher.Mono;

public interface IPokemonCreation {
    public Mono<Pokemon> saveOnePokemonToDB(PokemonDTO pokemonDTO);
}
