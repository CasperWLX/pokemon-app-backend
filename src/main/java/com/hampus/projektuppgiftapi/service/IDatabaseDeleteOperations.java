package com.hampus.projektuppgiftapi.service;

import reactor.core.publisher.Mono;

public interface IDatabaseDeleteOperations {
    Mono<Boolean> deleteAll();
    Mono<Boolean> deleteByName(String name);
}
