package com.hampus.projektuppgiftapi.service.api;

import com.hampus.projektuppgiftapi.model.pokemon.PokemonDTO;
import reactor.core.publisher.Mono;

public interface IApiRetrieval {
    Mono<PokemonDTO> fetchPokemonFromApiById(int id);
    Mono<Boolean> fetchAndSaveFirst151Pokemon();
}
