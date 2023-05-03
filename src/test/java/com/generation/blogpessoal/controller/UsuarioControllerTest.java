package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

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

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.model.UsuarioLogin;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioControllerTest {

	/*  anotação @SpringBootTest indica que a Classe UsuarioControllerTest é uma Classe Spring Boot Testing. 
	 * A Opção environment indica que caso a porta principal (8080 para uso local) esteja ocupada,
	 *  o Spring irá atribuir uma outra porta automaticamente.
	 *a anotação @TestInstance indica que o Ciclo de vida da Classe de Teste será por Classe.
	 */
	
	
	@Autowired
	private UsuarioService usuarioService;
	
	/* foi injetado (@Autowired), um objeto da Classe UsuarioService 
	 * para persistir os objetos no Banco de dados de testes com a senha criptografada. 
	 */
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	/* foi injetado (@Autowired), um objeto da Interface UsuarioRepository
	 *  para limpar o Banco de dados de testes.
	 */
	
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	/* foi injetado (@Autowired), um objeto da Classe TestRestTemplate para enviar as requisições para 
	 * a nossa aplicação.
	  */
	
	
	
	@BeforeAll 				//Método para preparar o ambiente para realizar os teste
	void start() {
		//Exemplo para ter um cadastro para autenticar
		usuarioRepository.deleteAll();
		
		usuarioService.cadastrarUsuario(new Usuario(0L, 
				"Root", "root@root.com", "rootroot", "-"));
		
	}
	
			/* Usário root é pré definido, ou seja, criado antes de todos os outros testes
			 * Método start(), anotado com a anotação @BeforeAll, apaga todos os dados da tabela e 
			 * cria o usuário root@root.com para testar os Métodos protegidos por autenticação.  */
	
	
	@Test
	@DisplayName("😃 Deve cadastrar um novo Usuário")
	public void deveCriarUmUsuario() {
		
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L, 
				"Paulo José da Silva", "pauloj_silva@email.com.br", "12345678", "-"));
		
		ResponseEntity<Usuario> corpoResposta = testRestTemplate
				.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);
		
		assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
		
	}
	
			/* Método deveCriarUmUsuario() foi anotado com a anotação @Test que indica que este Método 
			 * executará um teste. A anotação @DisplayName configura uma mensagem que será exibida ao 
			 * invés do nome do Método */
	
			/* foi criado um objeto da Classe HttpEntity chamado corpoRequisicao,
			 *  recebendo um objeto da Classe Usuario. */
	
	
			/*  a Requisição HTTP será enviada através do Método exchange()
			 *  da Classe TestRestTemplate e a Resposta da Requisição (Response)
			 * será recebida pelo objeto corpoResposta do tipo ResponseEntity  */
	
	
			/* através do Método de asserção AssertEquals(), checaremos se a resposta da requisição (Response)
			 * Para obter o status da resposta vamos utilizar o Método getStatusCode() da Classe ResponseEntity.  */
	
	
	@Test
	@DisplayName("😃 Não deve permitir a duplicação do cadastro de um Usuário")
	public void naoDeveCriarUmUsuario() {
		
		usuarioService.cadastrarUsuario(new Usuario(0L, 
				"Maria da Silva Santos", "mariasilva_santos@email.com.br", "12345678", "-"));
		
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L, 
				"Maria da Silva Santos", "mariasilva_santos@email.com.br", "12345678", "-"));
		
		ResponseEntity<Usuario> corpoResposta = testRestTemplate
				.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());
		
	}
	
			//Letra L depois do cadastro de "new Usuario" significa Long
	
	
	@Test
	@DisplayName("😃 Deve atualizar os dados do Usuário")
	public void deveAtualizarUmUsuario() {
		
		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(new Usuario(0L, 
				"Joaquim da Silva Santos", "joaquimsilva_santos@email.com.br", "12345678", "-"));
		
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario
				(usuarioCadastrado.get().getId(), 
				"Joaquim Santos da Silva", "joaquimsantos_silva@email.com.br", "12345678", "-"));
		
		ResponseEntity<Usuario> corpoResposta = testRestTemplate
				.withBasicAuth("root@root.com", "rootroot")
				.exchange("/usuarios/atualizar", HttpMethod.PUT, corpoRequisicao, Usuario.class);
		
		assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
		
	}
	
	
	@Test
	@DisplayName("😃 Deve listar todos os Usuários")
	public void deveListarTodosOsUsuarios() {
		
		usuarioService.cadastrarUsuario(new Usuario(0L, 
				"Etevaldo da Silva Santos", "etevaldosilva_santos@email.com.br", "12345678", "-"));
		
		usuarioService.cadastrarUsuario(new Usuario(0L, 
				"Etelvina da Silva Santos", "etelvinasilva_santos@email.com.br", "12345678", "-"));
		
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root@root.com", "rootroot")
				.exchange("/usuarios/all", HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		
	}
	
	
	
	// <> representa uma caixa que contém
	
	
	/* @Test
	@DisplayName("😃 Deve logar no Usuario")
	public void deveLogarUsuario() {
				
		usuarioService.cadastrarUsuario(new Usuario(0L, 
				"Etevaldo da Silva Santos", "etevaldosilva_santos@email.com.br", "12345678", "-"));
				
		usuarioService.cadastrarUsuario(new Usuario(0L, 
				"Etelvina da Silva Santos", "etelvinasilva_santos@email.com.br", "12345678", "-"));
				
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root@root.com", "rootroot")
				.exchange("/usuarios/all", HttpMethod.GET, null, String.class);
				
			assertEquals(HttpStatus.OK, resposta.getStatusCode());
				
		} */
	
	@Test
	@DisplayName("😃 Deve procurar um usuário por ID")
	public void deveProcurarUsuarioPorId() {
				
		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(new Usuario(0L, 
				"Luisinho da Silva Santos", "luisinhosilva_santos@email.com.br", "12345678", "-"));
		
		ResponseEntity<Usuario> resposta = testRestTemplate
				.withBasicAuth("root@root.com", "rootroot")
				.exchange("/usuarios/" + usuarioCadastrado.get().getId().toString(), 
						HttpMethod.GET, null, Usuario.class);
				
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
				
	}
	 
	// long usuario = usuarioCadastrado.get().getId();
	//+ usuarioCadastrado.get().getId().toString()
	
	@Test
	@DisplayName("😃 Login do Usuário")
	public void deveAutenticarUsuario() {

		usuarioService.cadastrarUsuario(new Usuario(0L, 
				"Luisinho da Silva Santos", "luisinhosilva_santos@email.com.br", "12345678", "-"));

		HttpEntity<UsuarioLogin> corpoRequisicao = new HttpEntity<UsuarioLogin>(new UsuarioLogin(0L, 
			"", "luisinhosilva_santos@email.com.br", "13465278", "", ""));

		ResponseEntity<UsuarioLogin> corpoResposta = testRestTemplate
			.exchange("/usuarios/logar", HttpMethod.POST, corpoRequisicao, UsuarioLogin.class);

		assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());

	}
	

	
}
