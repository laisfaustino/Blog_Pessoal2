package com.generation.blogpessoal.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity //Para criar tabela
@Table(name = "tb_postagens") //Equivalente a CREATE TABLE tb_postagens
public class Postagem {
	//Classe model usada para criar os campos da tabela no banco de dados
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//Usar estratégia identity é o mesmo que dizer auto increment no MySQL
	private Long id;
	
	@NotBlank(message = "O Atributo título é obrigatório!") //só utilizada com string
	@Size(min = 5, max = 100, message = "O atributo título deve ter no mínimo 05 caracteres e no máximo 100")
	private String titulo;
	//@notblank impede que a pessoa coloque somente uma tecla de espaço, é preciso digitar alguma coisa
	@NotBlank(message = "O Atributo texto é obrigatório!") //só utilizada com string
	@Size(min = 10, max = 1000, message = "O atributo texto deve ter no mínimo 10 caracteres e no máximo 1000")
	private String texto;
	
	@UpdateTimestamp
	private LocalDateTime data;

	
	/* Relacionamento   */
	
	@ManyToOne
	@JsonIgnoreProperties("postagem")
	private Tema tema; //Foi colocado um objeto da classe tema
	
	@ManyToOne
	@JsonIgnoreProperties("postagem")
	private Usuario usuario;
	

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getTitulo() {
		return titulo;
	}


	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}


	public String getTexto() {
		return texto;
	}


	public void setTexto(String texto) {
		this.texto = texto;
	}


	public LocalDateTime getData() {
		return data;
	}


	public void setData(LocalDateTime data) {
		this.data = data;
	}


	public Tema getTema() {
		return tema;
	}


	public void setTema(Tema tema) {
		this.tema = tema;
	}
	
	
	public Usuario getUsuario() {
		return usuario;
	}


	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
	
}
