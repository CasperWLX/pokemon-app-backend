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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/database/v2")
@CrossOrigin(origins = {"http://pokegame.hampuskallberg.se", "http://localhost:5173", "https://pokemongame.hampuskallberg.se"})
public class DatabaseController {

    private final PokemonService DATABASE_SERVICE;

    @Autowired
    public DatabaseController(PokemonService databaseService) {
        this.DATABASE_SERVICE = databaseService;
    }

    @GetMapping("/fetch-one-pokemon/{name}")
    public Mono<ResponseEntity<Pokemon>> getPokemonFromDatabase(@PathVariable String name) {
        return DATABASE_SERVICE.getPokemonByName(name).map(ResponseEntity::ok);
    }

    @GetMapping("/fetch-all-pokemon")
    public Mono<ResponseEntity<List<Pokemon>>> getAllPokemonFromDatabase(){
        return DATABASE_SERVICE.getAllPokemon().collectList().map(ResponseEntity::ok);
    }

    @PostMapping("/add-one-pokemon")
    public Mono<ResponseEntity<Pokemon>> addOnePokemonToDatabase(@RequestBody PokemonDTO pokemonDTO){
        return DATABASE_SERVICE.saveOnePokemonToDB(pokemonDTO).map(ResponseEntity::ok);
    }

    @DeleteMapping("/delete-one-pokemon/{name}")
    public Mono<ResponseEntity<Boolean>> deleteOnePokemonFromDatabase(@PathVariable String name) {
        return DATABASE_SERVICE.deletePokemonByName(name).thenReturn(ResponseEntity.noContent().build());
    }

    @DeleteMapping("/delete-all-pokemon")
    public Mono<ResponseEntity<Boolean>> deleteAllPokemonFromDatabase(){
        return DATABASE_SERVICE.deleteAllPokemon().thenReturn(ResponseEntity.noContent().build());
    }
}
