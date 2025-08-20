package com.literatura.catalogo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AutorDTO {

    private String name;

    @JsonProperty("birth_year")
    private Integer birthYear;

    @JsonProperty("death_year")
    private Integer deathYear;

    public AutorDTO() {
        // Construtor padrão necessário para a desserialização
    }

    // Getters
    public String getName() {
        return name;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public Integer getDeathYear() {
        return deathYear;
    }

    @Override
    public String toString() {
        return "AutorDTO{" +
                "name='" + name + '\'' +
                ", birthYear=" + birthYear +
                ", deathYear=" + deathYear +
                '}';
    }
}
