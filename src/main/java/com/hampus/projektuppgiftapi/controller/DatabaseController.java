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
public class DatabaseController {

    private final PokemonService DATABASE_SERVICE;

    @Autowired
    public DatabaseController(PokemonService databaseService) {
        this.DATABASE_SERVICE = databaseService;
    }

    @GetMapping("/pokemon/{identifier}")
    public Mono<ResponseEntity<Pokemon>> getPokemonFromDatabase(@PathVariable String identifier) {
        return DATABASE_SERVICE.getPokemonByIdentifier(identifier).map(ResponseEntity::ok);
    }

    @GetMapping("/pokemon/all")
    public Mono<ResponseEntity<List<Pokemon>>> getAllPokemonFromDatabase(){
        return DATABASE_SERVICE.getAllPokemon().collectList().map(ResponseEntity::ok);
    }

    @PostMapping("/pokemon/add")
    public Mono<ResponseEntity<Pokemon>> addOnePokemonToDatabase(@RequestBody PokemonDTO pokemonDTO){
        return DATABASE_SERVICE.saveOnePokemonToDB(pokemonDTO).map(ResponseEntity::ok);
    }

    @DeleteMapping("/pokemon/{name}")
    public Mono<ResponseEntity<Boolean>> deleteOnePokemonFromDatabase(@PathVariable String name) {
        return DATABASE_SERVICE.deletePokemonByName(name).thenReturn(ResponseEntity.noContent().build());
    }

    @DeleteMapping("/pokemon/all")
    public Mono<ResponseEntity<Boolean>> deleteAllPokemonFromDatabase(){
        return DATABASE_SERVICE.deleteAllPokemon().thenReturn(ResponseEntity.noContent().build());
    }
}
