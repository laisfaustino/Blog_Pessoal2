package com.generation.blogpessoal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;

@RestController
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostagemController {

	@Autowired //injeção de dependência: injetar a interface (1ª palavra) no objeto (2ª palavra)
	private PostagemRepository postagemRepository;
	
	@GetMapping //ResponseEntity é a resposta da requisição HTTP
	public ResponseEntity<List<Postagem>> getAll() { //Classe retorna uma lista que terá as postagens criadas
		return ResponseEntity.ok(postagemRepository.findAll());
		
	}
	
	
	
}
