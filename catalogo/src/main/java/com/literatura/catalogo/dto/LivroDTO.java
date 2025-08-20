package com.literatura.catalogo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LivroDTO {

    private String title;

    private List<AutorDTO> authors;

    private List<String> languages;

    @JsonProperty("download_count")
    private int downloadCount;

    public LivroDTO() {
        // Construtor padrão necessário para a desserialização
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public List<AutorDTO> getAuthors() {
        return authors;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    @Override
    public String toString() {
        return "LivroDTO{" +
                "title='" + title + '\'' +
                ", authors=" + authors +
                ", languages=" + languages +
                ", downloadCount=" + downloadCount +
                '}';
    }
}
