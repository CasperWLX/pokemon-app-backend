package com.hampus.projektuppgiftapi.util.pokemon;

import com.hampus.projektuppgiftapi.model.pokemon.Pokemon;
import com.hampus.projektuppgiftapi.model.pokemon.PokemonDTO;
import com.hampus.projektuppgiftapi.repo.IPokemonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PokemonUtil implements IPokemonUtils {

    private final Logger LOGGER = LoggerFactory.getLogger(PokemonUtil.class);
    private final IPokemonRepository POKEMON_REPO;

    public PokemonUtil(IPokemonRepository pokemonRepo) {
        this.POKEMON_REPO = pokemonRepo;
    }

    public Mono<Boolean> isDatabaseEmpty(){
        LOGGER.info("Checking if DB is populated");
        return POKEMON_REPO.count().map(count -> count == 0);
    }

    public String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    public boolean isInteger(String input){
        try{
            Integer.parseInt(input);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    public Pokemon convertToPokemonModel(PokemonDTO pokemonDTO){
        LOGGER.info("Converting PokemonDTO to Pokemon: {}", pokemonDTO.getName());
        Pokemon pokemon = new Pokemon().setPokemonId(pokemonDTO.getId())
                .setName(capitalizeFirstLetter(pokemonDTO.getName()))
                .setHeight(pokemonDTO.getHeight())
                .setWeight(pokemonDTO.getWeight())
                .setEvolutionStage(pokemonDTO.getEvolutionStage())
                .setFirstType(capitalizeFirstLetter(pokemonDTO.getTypes().getFirst().getType().getName()))
                .setImgURL(pokemonDTO.getSprites().getFront_default());
        if (pokemonDTO.getTypes().size() > 1){
            pokemon.setSecondType(capitalizeFirstLetter(pokemonDTO.getTypes().getLast().getType().getName()));
        }else{
            pokemon.setSecondType("None");
        }
        return pokemon;
    }
}
