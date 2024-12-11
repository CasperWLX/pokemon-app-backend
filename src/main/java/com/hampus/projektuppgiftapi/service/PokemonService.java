package com.hampus.projektuppgiftapi.service;

import com.hampus.projektuppgiftapi.exceptions.PokemonCreationException;
import com.hampus.projektuppgiftapi.exceptions.PokemonNotFoundException;
import com.hampus.projektuppgiftapi.exceptions.PokemonUpdateException;
import com.hampus.projektuppgiftapi.model.pokemon.Pokemon;
import com.hampus.projektuppgiftapi.model.pokemon.PokemonDTO;
import com.hampus.projektuppgiftapi.repo.IPokemonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class PokemonService {

    private final Logger LOGGER = LoggerFactory.getLogger(PokemonService.class);
    private final IPokemonRepository POKEMON_REPO;

    @Autowired
    public PokemonService(IPokemonRepository pokemonRepository) {
        this.POKEMON_REPO = pokemonRepository;
    }

    public Mono<Pokemon> getPokemonByName(String name){
        LOGGER.info("Fetching pokemon from DB with name: {}", name);
        return POKEMON_REPO.findByName(name).switchIfEmpty(Mono.error(new PokemonNotFoundException(name)));
    }

    public Mono<Boolean> deletePokemonByName(String name) {
        LOGGER.info("Deleting pokemon with name: {}", name);
        return getPokemonByName(name).flatMap(POKEMON_REPO::delete).then(Mono.just(true));
    }

//    public Pokemon updatePokemon(String name, Pokemon newPokemonInformation){
//        Pokemon existingPokemon = getPokemonByName(name);
//        if (existingPokemon.getName().equals(newPokemonInformation.getName())){
//            existingPokemon.setName(newPokemonInformation.getName())
//                    .setHeight(newPokemonInformation.getHeight())
//                    .setWeight(newPokemonInformation.getWeight())
//                    .setFirstType(newPokemonInformation.getFirstType())
//                    .setSecondType(newPokemonInformation.getSecondType())
//                    .setImgURL(newPokemonInformation.getImgURL())
//                    .setEvolutionStage(newPokemonInformation.getEvolutionStage());
//            POKEMON_REPO.save(existingPokemon);
//            LOGGER.info("Pokemon with name: {} updated in DB", newPokemonInformation.getName());
//            return existingPokemon;
//        }
//        throw new PokemonUpdateException("Pokemon identifiers does not match");
//    }

    public Mono<Pokemon> saveOnePokemonToDB(PokemonDTO pokemonDTO){
        Pokemon pokemon = convertToPokemonModel(pokemonDTO);

        return POKEMON_REPO.save(pokemon).doOnNext(savedPokemon ->
                        LOGGER.info("Saved: {}", savedPokemon.getName()))
                .thenReturn(pokemon);
    }

    public Pokemon convertToPokemonModel(PokemonDTO pokemonDTO){
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

//    public String deleteAllPokemon(){
//        try {
//            LOGGER.info("Deleting all pokemon");
//            POKEMON_REPO.deleteAll();
//            return "All pokemon deleted";
//        }catch (RuntimeException e){
//            throw new PokemonNotFoundException("Something went wrong when deleting all pokemon");
//        }
//    }

//    public List<Pokemon> getAllPokemon(){
//        try{
//            LOGGER.info("Fetching all pokemon from database");
//            return POKEMON_REPO.findAll();
//        }catch (PokemonNotFoundException e){
//            throw new PokemonNotFoundException("Something went wrong when fetching all pokemon");
//        }
//    }

    public Mono<Boolean> databaseIsPopulated(){
        return POKEMON_REPO.count().map(count -> count == 0);
    }
}
