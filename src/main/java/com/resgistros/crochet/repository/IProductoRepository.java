package com.resgistros.crochet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.resgistros.crochet.model.Producto;



@Repository
public interface IProductoRepository extends JpaRepository<Producto, Integer> {

}