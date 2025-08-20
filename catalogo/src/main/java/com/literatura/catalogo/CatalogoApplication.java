package com.literatura.catalogo;

import com.literatura.catalogo.dto.AutorDTO;
import com.literatura.catalogo.dto.LivroDTO;
import com.literatura.catalogo.dto.ResultadosDTO;
import com.literatura.catalogo.model.Autor;
import com.literatura.catalogo.model.Livro;
import com.literatura.catalogo.service.ConsumoApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;
import java.util.Map;


@SpringBootApplication
public class CatalogoApplication implements CommandLineRunner {

	@Autowired
	private ConsumoApiService apiService;

	public static void main(String[] args) {
		SpringApplication.run(CatalogoApplication.class, args);
	}

	@Override
	public void run(String... args) {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("===========================");
			System.out.println("📚 CATÁLOGO DE LIVROS 📚");
			System.out.println("===========================");
			System.out.println("Escolha uma opção:");
			System.out.println("1 - Buscar livro por título");
			System.out.println("2 - Listar todos os livros salvos");
			System.out.println("3 - Estatística de livros por idioma (futuro)");
			System.out.println("4 - Listar autores vivos em um ano (futuro)");
			System.out.println("5 - Sair");
			System.out.print("Digite o número da opção: ");

			int opcao = scanner.nextInt();
			scanner.nextLine(); // limpar o buffer

			switch (opcao) {
				case 1:
					System.out.print("Digite o título do livro: ");
					String titulo = scanner.nextLine();

					ResultadosDTO resultados = apiService.buscarLivroPorTitulo(titulo);
					List<LivroDTO> livros = resultados.getResults();

					if (livros == null || livros.isEmpty()) {
						System.out.println("Nenhum livro encontrado com esse título.");
						break;
					}

					System.out.println("📚 Livros encontrados:");
					for (LivroDTO livroDTO : livros) {
						System.out.println("📘 Título: " + livroDTO.getTitle());
						System.out.println("🧠  Autores:");
						for (AutorDTO autor : livroDTO.getAuthors()) {
							System.out.println("  Nome: " + autor.getName());
							System.out.println("  Ano nascimento: " + autor.getBirthYear());
							System.out.println("  Ano falecimento: " + autor.getDeathYear());
						}

						System.out.println("🗣️  Idiomas: " + livroDTO.getLanguages());
						System.out.println("⬇️  Downloads: " + livroDTO.getDownloadCount());
						System.out.println("----------------------------------");

						// Salva no banco de dados
						apiService.salvarLivroNoBanco(livroDTO);
					}
					break;

				case 2:
					List<Livro> livrosSalvos = apiService.buscarLivrosSalvos();

					if (livrosSalvos.isEmpty()) {
						System.out.println("📫 Nenhum livro salvo no banco ainda.");
					} else {
						System.out.println("\n📚 Livros salvos no banco:");
						for (Livro livro : livrosSalvos) {
							System.out.println("📖 Título: " + livro.getTitulo());
							System.out.println("✍️ Autor: " + livro.getAutor().getNome());
							System.out.println("🌍 Idioma: " + livro.getIdioma());
							System.out.println("📉 Downloads: " + livro.getNumeroDeDownloads());
							System.out.println("-----------------------------------");
						}
					}
					break;

				case 3:
					Map<String, Long> estatisticas = apiService.contarLivrosPorIdioma();

					if (estatisticas.isEmpty()) {
						System.out.println("📭 Nenhum dado de idioma disponível.");
					} else {
						System.out.println("📊 Estatística de livros por idioma:");
						estatisticas.forEach((idioma, quantidade) ->
								System.out.println(" - " + idioma + ": " + quantidade + " livros"));
					}
					break;

				case 4:
					System.out.print("Digite o ano para verificar autores vivos: ");
					int anoConsulta = scanner.nextInt();
					scanner.nextLine(); // limpar buffer

					List<Autor> autoresVivos = apiService.buscarAutoresVivosNoAno(anoConsulta);

					if (autoresVivos.isEmpty()) {
						System.out.println("🙁 Nenhum autor vivo encontrado nesse ano.");
					} else {
						System.out.println("\n👴 Autores vivos no ano " + anoConsulta + ":");
						for (Autor autor : autoresVivos) {
							System.out.println("📚 Nome: " + autor.getNome());
							System.out.println("🎂 Nascimento: " + autor.getAnoNascimento());
							System.out.println("☠️ Falecimento: " + (autor.getAnoFalecimento() == null ? "Vivo" : autor.getAnoFalecimento()));
							System.out.println("-----------------------------------");
						}
					}


				case 5:
					System.out.println("👋 Saindo do programa...");
					System.exit(0);
					break;

				default:
					System.out.println("❌ Opção inválida. Tente novamente.");
			}
		}
	}
}
