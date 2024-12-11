package com.hampus.projektuppgiftapi.controller;

import com.hampus.projektuppgiftapi.exceptions.PokemonCreationException;
import com.hampus.projektuppgiftapi.exceptions.PokemonUpdateException;
import com.hampus.projektuppgiftapi.exceptions.PokemonNotFoundException;
import com.hampus.projektuppgiftapi.model.pokemon.Pokemon;
import com.hampus.projektuppgiftapi.model.pokemon.PokemonDTO;
import com.hampus.projektuppgiftapi.service.PokemonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/database/v2")
@CrossOrigin(origins = {"http://pokegame.hampuskallberg.se", "http://localhost:5173", "https://pokemongame.hampuskallberg.se"})
public class DatabaseController {

    private final PokemonService DATABASE_SERVICE;
    private final Logger LOGGER = LoggerFactory.getLogger(DatabaseController.class);

    @Autowired
    public DatabaseController(PokemonService databaseService) {
        this.DATABASE_SERVICE = databaseService;
    }

    @GetMapping("/fetch-one-pokemon/{name}")
    public Mono<ResponseEntity<Pokemon>> getPokemonFromDatabase(@PathVariable String name) {
        LOGGER.info("Fetching pokemon by name: {}", name);
        return DATABASE_SERVICE.getPokemonByName(name).map(ResponseEntity::ok);
    }

//    @GetMapping("/fetch-all-pokemon")
//    public ResponseEntity<List<Pokemon>> getAllPokemonFromDatabase(){
//        try {
//            return ResponseEntity.ok(DATABASE_SERVICE.getAllPokemon());
//        } catch (PokemonNotFoundException e){
//            LOGGER.warn("Could not fetch all pokemon from database");
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @PostMapping("/add-one-pokemon")
//    public ResponseEntity<Pokemon> addOnePokemonToDatabase(@RequestBody PokemonDTO pokemonDTO){
//        try {
//           DATABASE_SERVICE.saveOnePokemonToDB(pokemonDTO);
//           return ResponseEntity.ok(DATABASE_SERVICE.getPokemonByName(pokemonDTO.getName()));
//        }catch (PokemonCreationException e){
//            LOGGER.warn("Pokemon with name {} could not be created", pokemonDTO.getName());
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//    @PutMapping("/update-one-pokemon/{id}")
//    public ResponseEntity<Pokemon> updatePokemonFromDatabase(@RequestBody Pokemon newPokemonInformation, @PathVariable Long id){
//        try {
//            return ResponseEntity.ok(DATABASE_SERVICE.updatePokemon(id ,newPokemonInformation));
//        } catch (PokemonNotFoundException e){
//            LOGGER.warn("Pokemon with name {} could not be updated", newPokemonInformation.getName());
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @DeleteMapping("/delete-one-pokemon/{identifier}")
//    public ResponseEntity<Pokemon> deleteOnePokemonFromDatabase(@PathVariable String identifier) {
//        if (DATABASE_SERVICE.isInteger(identifier)){
//            try {
//                Long id = Long.getLong(identifier);
//                return ResponseEntity.status(204).body(DATABASE_SERVICE.deletePokemonById(id));
//            } catch (PokemonUpdateException e){
//                LOGGER.warn("Pokemon with id {} could not be deleted", identifier);
//                return ResponseEntity.notFound().build();
//            }
//        }else {
//            try {
//                return ResponseEntity.status(204).body(DATABASE_SERVICE.deletePokemonByName(identifier));
//            } catch (PokemonUpdateException e){
//                LOGGER.warn("Pokemon with name {} could not be deleted", identifier);
//                return ResponseEntity.notFound().build();
//            }
//        }
//    }
//
//    @DeleteMapping("/delete-all-pokemon")
//    public ResponseEntity<String> deleteAllPokemonFromDatabase(){
//        try {
//            return ResponseEntity.ok(DATABASE_SERVICE.deleteAllPokemon());
//        } catch (PokemonNotFoundException e){
//            return ResponseEntity.notFound().build();
//        }
//    }
}
