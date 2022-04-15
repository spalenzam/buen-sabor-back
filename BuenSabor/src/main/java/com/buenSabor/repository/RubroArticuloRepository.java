package com.buenSabor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.buenSabor.entity.RubroArticulo;

public interface RubroArticuloRepository extends PagingAndSortingRepository<RubroArticulo, Long>{

	@Query("select a from RubroArticulo a where a.fechaBaja is null")
	public Iterable<RubroArticulo> findAllRubroArticuloAlta();
	
	@Query("select a from RubroArticulo a where a.rubroarticuloPadre = ?1")
	public List<RubroArticulo> findHijos(Long id);
}
