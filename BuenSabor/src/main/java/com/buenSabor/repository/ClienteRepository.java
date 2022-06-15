package com.buenSabor.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.buenSabor.entity.Cliente;

public interface ClienteRepository extends CrudRepository<Cliente, Long>{
	
	@Query("select a from Cliente a where a.email = ?1")
	public Optional<Cliente>findByEmail(String email);
}
