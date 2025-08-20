package com.literatura.catalogo.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.literatura.catalogo.dto.AutorDTO;
import com.literatura.catalogo.dto.LivroDTO;
import com.literatura.catalogo.dto.ResultadosDTO;
import com.literatura.catalogo.model.Autor;
import com.literatura.catalogo.model.Livro;
import com.literatura.catalogo.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.stream.Collectors;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class ConsumoApiService {

    @Autowired
    private LivroRepository livroRepository;

    public ResultadosDTO buscarLivroPorTitulo(String titulo) {
        try {
            String tituloFormatado = titulo.replace(" ", "%20");
            URI uri = URI.create("https://gutendex.com/books/?search=" + tituloFormatado);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            return mapper.readValue(response.body(), ResultadosDTO.class);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erro ao buscar livro: " + e.getMessage());
        }
    }

    public List<Livro> buscarLivrosSalvos() {
        return livroRepository.findAll();
    }

    public void salvarLivroNoBanco(LivroDTO livroDTO) {
        if (livroDTO.getAuthors() == null || livroDTO.getAuthors().isEmpty()) return;

        AutorDTO autorDTO = livroDTO.getAuthors().get(0);
        Autor autor = new Autor(
                autorDTO.getName(),
                autorDTO.getBirthYear(),
                autorDTO.getDeathYear()
        );

        String idioma = (livroDTO.getLanguages() != null && !livroDTO.getLanguages().isEmpty())
                ? livroDTO.getLanguages().get(0)
                : "desconhecido";

        Livro livro = new Livro(
                livroDTO.getTitle(),
                idioma,
                livroDTO.getDownloadCount(),
                autor
        );

        livroRepository.save(livro);
    }

    public Map<String, Long> contarLivrosPorIdioma() {
        return livroRepository.findAll().stream()
                .collect(Collectors.groupingBy(Livro::getIdioma, Collectors.counting()));
    }

    public List<Autor> buscarAutoresVivosNoAno(int ano) {
        return livroRepository.findAll().stream()
                .map(Livro::getAutor)
                .distinct()
                .filter(autor -> autor.getAnoNascimento() != null && autor.getAnoNascimento() <= ano)
                .filter(autor -> autor.getAnoFalecimento() == null || autor.getAnoFalecimento() > ano)
                .toList();
    }


}
