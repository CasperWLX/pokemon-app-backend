package com.hampus.projektuppgiftapi.controller;

import com.hampus.projektuppgiftapi.exceptions.PokemonNotFoundException;
import com.hampus.projektuppgiftapi.service.ApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v2")
@CrossOrigin(origins = {"http://localhost:5173", "https://pokemongame.hampuskallberg.se"})
public class PokemonAPIController {

    private final ApiService POKEMON_SERVICE;
    private final Logger LOGGER = LoggerFactory.getLogger(PokemonAPIController.class);
    @Autowired
    public PokemonAPIController(ApiService POKEMON_SERVICE) {
        this.POKEMON_SERVICE = POKEMON_SERVICE;
    }

    @GetMapping("/fetch-and-save-pokemon")
    public Mono<ResponseEntity<Boolean>> fetchAndSavePokemon() {
        return POKEMON_SERVICE.fetchAndSaveFirst151Pokemon().map(ResponseEntity::ok);
    }

//    @GetMapping("/fetch-one-pokemon/{identifier}")
//    public void fetchOnePokemon(@PathVariable String identifier, @RequestParam int evolution_stage){
//        try{
//            try {
//                int pokemonId = Integer.parseInt(identifier);
//                POKEMON_SERVICE.fetchOnePokemonById(pokemonId, evolution_stage);
//            } catch (NumberFormatException e){
//                POKEMON_SERVICE.fetchOnePokemonByName(identifier, evolution_stage);
//            }
//        } catch(PokemonNotFoundException e){
//            LOGGER.info("Error while fetching pokemon");
//        }
//
//    }
}
