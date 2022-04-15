package com.buenSabor.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.buenSabor.entity.RubroGeneral;

public interface RubroGeneralRepository extends PagingAndSortingRepository<RubroGeneral, Long>{

	@Query("select a from RubroGeneral a where a.fechaBaja is null")
	public Iterable<RubroGeneral> findAllRubroGeneralAlta();
}
