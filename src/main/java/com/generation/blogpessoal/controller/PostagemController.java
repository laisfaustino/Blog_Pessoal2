package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;

import jakarta.validation.Valid;

@RestController //Significa que essa classe responde a toda requisição que chegar em /postagens
@RequestMapping("/postagens") //Destino das requisições
@CrossOrigin(origins = "*", allowedHeaders = "*") //Habilitar requests vindas de outras origens
public class PostagemController {

	@Autowired 
	private PostagemRepository postagemRepository;
	  
	//Próximo passo é criar métodos do CRUD
	
	@GetMapping 
	public ResponseEntity<List<Postagem>> getAll() { //Classe retorna uma lista que terá as postagens criadas lá da tabela
		return ResponseEntity.ok(postagemRepository.findAll());
		//SELECT * from tb_postagens;
	}
	
	
			/* Injeção de dependência: injetar a interface (1ª palavra) no objeto (2ª palavra)
			É preciso criar um objeto para puxar os métodos padrões do JPA que estão dentro da interface Repository
			ResponseEntity é a resposta da requisição HTTP */
	
	
	@GetMapping("/{id}") //variável tem que estar entre chaves
	public ResponseEntity<Postagem> getById(@PathVariable Long id) { 

		return postagemRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
		
		//SELECT * FROM tb_postagens WHERE id = ?;
		// Precisa de optional, porque se o id não for encontrado, ele será nulo, e não é possível isso acontecer, se não acontece o erro NullPointer
	}
	
			/* Optional
			 * PathVariable pega o valor do Id enviado no endereço endpoint na Variável de Caminho {id} e joga no parâmetro do Método getById( Long id )*/
	
	
	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo) { 
			 
			return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
			//SELECT * FROM tb_postagens WHERE titulo LIKE "%xxxx%";	
		}
	
	
			/* Collection nunca será nula, ela somente pode ser vazia */
	
	
	@PostMapping
	public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(postagemRepository.save(postagem));
		
	}
			//@Valid valida o objeto
			// pegar um objeto da classe postagem
			/* INSERT INTO tb_postagens (titulo, texto)
			 * VALUES ("Postagem 03","Texto da postagem 03")
			 * Usa-se o método status porque estou mexendo no corpo da requisição */
	
	@PutMapping
	public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem) {
		
			return postagemRepository.findById(postagem.getId())
					
					.map(resposta -> ResponseEntity.status(HttpStatus.OK)
						.body(postagemRepository.save(postagem)))
						.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
		
	}
	
			/* UPDATE tb_postagens SET titulo = ?   */
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		
		Optional<Postagem> postagem = postagemRepository.findById(id);

        if(postagem.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
        postagemRepository.deleteById(id);
		
		
	}
			
			/* DELETE FROM tb_postagens WHERE id = ?  */
	
}
