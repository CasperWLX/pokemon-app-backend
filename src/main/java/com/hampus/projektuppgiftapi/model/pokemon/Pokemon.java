package com.hampus.projektuppgiftapi.model.pokemon;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Pokemon")
public class Pokemon {

    @Id
    private String id;
    private Long pokemonId;
    private String name;
    private String firstType;
    private String secondType;
    private int evolutionStage;
    private int height;
    private int weight;
    private String imgURL;

    public Pokemon() {}

    public Long getPokemonId() {
        return pokemonId;
    }

    public Pokemon setPokemonId(int pokemonId){
        this.pokemonId = (long) pokemonId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Pokemon setName(String name) {
        this.name = name;
        return this;
    }

    public String getFirstType() {
        return firstType;
    }

    public Pokemon setFirstType(String firstType) {
        this.firstType = firstType;
        return this;
    }

    public String getSecondType() {
        return secondType;
    }

    public Pokemon setSecondType(String secondType) {
        this.secondType = secondType;
        return this;
    }

    public int getEvolutionStage() {
        return evolutionStage;
    }

    public Pokemon setEvolutionStage(int evolutionNumber) {
        this.evolutionStage = evolutionNumber;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public Pokemon setHeight(int height) {
        this.height = height;
        return this;
    }

    public int getWeight() {
        return weight;
    }

    public Pokemon setWeight(int weight) {
        this.weight = weight;
        return this;
    }

    public String getImgURL() {
        return imgURL;
    }

    public Pokemon setImgURL(String imgURL) {
        this.imgURL = imgURL;
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s, types: %s and %s, with id: %d", name, firstType, secondType, pokemonId);
    }
}
