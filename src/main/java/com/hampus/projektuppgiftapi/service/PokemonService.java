package com.hampus.projektuppgiftapi.service;

import com.hampus.projektuppgiftapi.exceptions.PokemonCreationException;
import com.hampus.projektuppgiftapi.exceptions.PokemonNotFoundException;
import com.hampus.projektuppgiftapi.model.pokemon.Pokemon;
import com.hampus.projektuppgiftapi.model.pokemon.PokemonDTO;
import com.hampus.projektuppgiftapi.repo.IPokemonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PokemonService {

    private final Logger LOGGER = LoggerFactory.getLogger(PokemonService.class);
    private final IPokemonRepository POKEMON_REPO;

    @Autowired
    public PokemonService(IPokemonRepository pokemonRepository) {
        this.POKEMON_REPO = pokemonRepository;
    }

    public Mono<Pokemon> getPokemonByIdentifier(String identifier){
        LOGGER.info("Fetching pokemon from DB with identifier: {}", identifier );
        if (isInteger(identifier)) {
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

    public Mono<Boolean> deletePokemonByName(String name) {
        LOGGER.info("Deleting pokemon with name: {}", name);
        return getPokemonByName(name).flatMap(POKEMON_REPO::delete).then(Mono.just(true));
    }

    public Mono<Pokemon> saveOnePokemonToDB(PokemonDTO pokemonDTO) {
        Pokemon pokemon = convertToPokemonModel(pokemonDTO);

        LOGGER.info("Attempting to save Pokemon: {}", pokemon.getName());
        return POKEMON_REPO.save(pokemon)
                .doOnSuccess(savedPokemon -> LOGGER.info("Successfully saved Pokemon: {}", savedPokemon.getName()))
                .doOnError(error -> LOGGER.error("Error occurred while saving Pokemon: {}", error.getMessage()))
                .onErrorMap(error ->
                        new PokemonCreationException(
                                String.format("Could not save %s: %s",
                                        pokemon.getName(), error.getMessage())));
    }

    public Pokemon convertToPokemonModel(PokemonDTO pokemonDTO){
        LOGGER.info("Converting PokemonDTO to Pokemon: {}", pokemonDTO.getName());
        Pokemon pokemon = new Pokemon().setPokemonId(pokemonDTO.getId())
                .setName(pokemonDTO.getName())
                .setHeight(pokemonDTO.getHeight())
                .setWeight(pokemonDTO.getWeight())
                .setEvolutionStage(pokemonDTO.getEvolutionStage())
                .setFirstType(pokemonDTO.getTypes().getFirst().getType().getName())
                .setImgURL(pokemonDTO.getSprites().getFront_default());
        if (pokemonDTO.getTypes().size() > 1){
            pokemon.setSecondType(pokemonDTO.getTypes().getLast().getType().getName());
        }else{
            pokemon.setSecondType("none");
        }
        return pokemon;
    }

    public Mono<Boolean> deleteAllPokemon(){
        LOGGER.info("Deleting all Pokemon");
        return POKEMON_REPO.deleteAll().then(Mono.just(true));
    }

    public Flux<Pokemon> getAllPokemon(){
        LOGGER.info("Fetching all Pokemon");
        return POKEMON_REPO.findAll()
                .doOnError(error -> LOGGER.error("Error fetching all games: {}", error.getMessage()))
                .switchIfEmpty(Mono.error(new PokemonNotFoundException("Database is empty")));
    }

    public Mono<Boolean> databaseIsPopulated(){
        LOGGER.info("Checking if DB is populated");
        return POKEMON_REPO.count().map(count -> count == 0);
    }

    public boolean isInteger(String input){
        try{
            Integer.parseInt(input);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
}
