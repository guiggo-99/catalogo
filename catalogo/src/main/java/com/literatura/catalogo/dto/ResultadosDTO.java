package com.literatura.catalogo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ResultadosDTO {

    private int count;
    private String next;
    private String previous;

    @JsonProperty("results")
    private List<LivroDTO> results;

    public ResultadosDTO() {
        // Construtor padrão
    }

    public int getCount() {
        return count;
    }

    public String getNext() {
        return next;
    }

    public String getPrevious() {
        return previous;
    }

    public List<LivroDTO> getResults() {
        return results;
    }
}
