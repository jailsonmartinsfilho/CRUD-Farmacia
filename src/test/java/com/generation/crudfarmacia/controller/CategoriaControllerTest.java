package com.generation.crudfarmacia.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.crudfarmacia.model.Categoria;
import com.generation.crudfarmacia.repository.CategoriaRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CategoriaControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@BeforeAll
	void start() {
		categoriaRepository.deleteAll();
	}

	@Test
	@DisplayName("Cadastrar uma categoria")
	public void deveCriarUmaCategoria() {

		HttpEntity<Categoria> corpoRequisicao = new HttpEntity<Categoria>(new Categoria(null, "Remédio"));

		ResponseEntity<Categoria> corpoResposta = testRestTemplate.exchange("/categorias", HttpMethod.POST,
				corpoRequisicao, Categoria.class);

		assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
	}

	@Test
	@DisplayName("Não deve permitir duplicação de categoria")
	public void naoDeveDuplicarCategoria() {
	    Categoria categoriaInicial = new Categoria(null, "Remédio");
	    HttpEntity<Categoria> corpoCriacao = new HttpEntity<>(categoriaInicial);

	    ResponseEntity<Categoria> respostaCriacao = testRestTemplate
	            .exchange("/categorias", HttpMethod.POST, corpoCriacao, Categoria.class);
	    assertEquals(HttpStatus.CREATED, respostaCriacao.getStatusCode());

	    Categoria categoriaDuplicada = new Categoria(null, "Remédio");
	    HttpEntity<Categoria> corpoDuplicacao = new HttpEntity<>(categoriaDuplicada);

	    ResponseEntity<Categoria> respostaDuplicacao = testRestTemplate
	            .exchange("/categorias", HttpMethod.POST, corpoDuplicacao, Categoria.class);

	    assertEquals(HttpStatus.BAD_REQUEST, respostaDuplicacao.getStatusCode());
	}

	@Test
	@DisplayName("Atualizar uma categoria")
	public void deveAtualizarUmaCategoria() {

		Categoria categoriaCriada = new Categoria(null, "Remédio2");
		HttpEntity<Categoria> corpoCriacao = new HttpEntity<>(categoriaCriada);

		ResponseEntity<Categoria> respostaCriacao = testRestTemplate.exchange("/categorias", HttpMethod.POST,
				corpoCriacao, Categoria.class);

		  assertEquals(HttpStatus.CREATED, respostaCriacao.getStatusCode());
		
		
		Categoria categoriaCadastrada = respostaCriacao.getBody();

		Categoria categoriaUpdate = new Categoria(categoriaCadastrada.getId(), "Remédio Atualizado");

		HttpEntity<Categoria> corpoAtualizacao = new HttpEntity<Categoria>(categoriaUpdate);

		ResponseEntity<Categoria> respostaAtualizacao = testRestTemplate.exchange("/categorias", HttpMethod.PUT,
				corpoAtualizacao, Categoria.class);
		assertEquals(HttpStatus.OK, respostaAtualizacao.getStatusCode());
	}

	@Test
	@DisplayName("Listar todas as Categorias")
	public void deveMostrarTodosUsuarios() {
		ResponseEntity<String> resposta = testRestTemplate.withBasicAuth("root@root.com", "rootroot")
				.exchange("/categorias", HttpMethod.GET, null, String.class);

		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}
}