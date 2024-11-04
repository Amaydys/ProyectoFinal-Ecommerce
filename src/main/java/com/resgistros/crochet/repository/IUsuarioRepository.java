package com.resgistros.crochet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.resgistros.crochet.model.Usuario;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {
	// tengo que tener el nombre del campo para filtrar con email y siempre empieza con findByEmail
	Optional<Usuario> findByEmail(String email);


}

