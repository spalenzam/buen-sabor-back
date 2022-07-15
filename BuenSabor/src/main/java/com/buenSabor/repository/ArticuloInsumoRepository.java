package com.buenSabor.repository;

import org.springframework.data.jpa.repository.Query;
import com.buenSabor.entity.ArticuloInsumo;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ArticuloInsumoRepository extends PagingAndSortingRepository<ArticuloInsumo, Long>{

	@Query("select a from ArticuloInsumo a where a.fechaBaja is null")
	public Iterable<ArticuloInsumo> findAllAlta();
	
}
