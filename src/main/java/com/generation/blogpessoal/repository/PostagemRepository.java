package com.generation.blogpessoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.generation.blogpessoal.model.Postagem;

public interface PostagemRepository extends JpaRepository<Postagem, Long>{
	//
	//Não esquecer: extends = herdar
	//Não implementa nada, é uma interface
	//Qualquer objeto criado aqui, herdará todos os métodos que estão em JPA repository
	//Associar essa interface com a classe model
	List<Postagem> findAllByTituloContainingIgnoreCase(@Param("titulo")String titulo);
	//Consultas do tipo Like exigem o @param para mapear no banco de dados
	//SELECT * FROM tb_postagens WHERE titulo LIKE "%xxxx%"
	//Interface de consulta para criar consultas personalizadas
}
