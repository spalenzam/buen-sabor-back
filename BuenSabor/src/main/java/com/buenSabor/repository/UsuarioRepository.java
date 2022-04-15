package com.buenSabor.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.buenSabor.entity.Usuario;

public interface UsuarioRepository extends PagingAndSortingRepository<Usuario, Long>{

	@Query("select a from Usuario a where a.fechaBaja is null")
	public Iterable<Usuario> findAllUsuarioAlta();
}
