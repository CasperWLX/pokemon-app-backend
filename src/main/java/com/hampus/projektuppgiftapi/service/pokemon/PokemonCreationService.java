package com.hampus.projektuppgiftapi.service.pokemon;

import com.hampus.projektuppgiftapi.exceptions.pokemon.PokemonCreationException;
import com.hampus.projektuppgiftapi.model.pokemon.Pokemon;
import com.hampus.projektuppgiftapi.model.pokemon.PokemonDTO;
import com.hampus.projektuppgiftapi.repo.IPokemonRepository;
import com.hampus.projektuppgiftapi.util.pokemon.IPokemonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PokemonCreationService implements IPokemonCreation {

    private final Logger LOGGER = LoggerFactory.getLogger(PokemonCreationService.class);

    private final IPokemonUtils POKEMON_UTILS;
    private final IPokemonRepository POKEMON_DATABASE;

    public PokemonCreationService(IPokemonUtils utils, IPokemonRepository pokemonRepository) {
        this.POKEMON_UTILS = utils;
        this.POKEMON_DATABASE = pokemonRepository;
    }

    public Mono<Pokemon> saveOnePokemonToDB(PokemonDTO pokemonDTO) {
        Pokemon pokemon = POKEMON_UTILS.convertToPokemonModel(pokemonDTO);

        LOGGER.info("Attempting to save Pokemon: {}", pokemon.getName());
        return POKEMON_DATABASE.save(pokemon)
                .doOnSuccess(savedPokemon -> LOGGER.info("Successfully saved Pokemon: {}", savedPokemon.getName()))
                .doOnError(error -> LOGGER.error("Error occurred while saving Pokemon: {}", error.getMessage()))
                .onErrorMap(error ->
                        new PokemonCreationException(
                                String.format("Could not save %s: %s",
                                        pokemon.getName(), error.getMessage())));
    }
}
