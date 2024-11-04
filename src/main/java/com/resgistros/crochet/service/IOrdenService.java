package com.resgistros.crochet.service;

import java.util.List;
import java.util.Optional;

import com.resgistros.crochet.model.Orden;
import com.resgistros.crochet.model.Usuario;

public interface IOrdenService {
	List<Orden> findAll();
	Optional<Orden> findById(Integer id);
	Orden save (Orden orden);
	String generarNumeroOrden();
	List<Orden> findByUsuario (Usuario usuario);
}
