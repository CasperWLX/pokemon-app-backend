package com.hampus.projektuppgiftapi.model.pokemon;

import java.util.List;

public class PokemonDTO {

    private int id;
    private String name;
    private int height;
    private int weight;
    private Sprites sprites;
    private List<Types> types;
    private int evolutionStage = 1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getEvolutionStage() {
        return evolutionStage;
    }

    public void setEvolutionStage(int evolutionStage) {
        this.evolutionStage = evolutionStage;
    }

    public Sprites getSprites() {
        return sprites;
    }

    public void setSprites(Sprites sprites) {
        this.sprites = sprites;
    }

    public List<Types> getTypes() {
        return types;
    }

    public void setTypesList(List<Types> types) {
        this.types = types;
    }

    public static class Sprites{
        private String front_default;

        public String getFront_default() {
            return front_default;
        }

        public void setFront_default(String front_default) {
            this.front_default = front_default;
        }
    }

    public static class Types{
        private Type type;

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public static class Type{
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
