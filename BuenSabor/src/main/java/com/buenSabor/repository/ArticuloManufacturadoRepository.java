package com.buenSabor.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.buenSabor.entity.ArticuloManufacturado;

public interface ArticuloManufacturadoRepository extends PagingAndSortingRepository<ArticuloManufacturado, Long>{
	
	@Query("select a from ArticuloManufacturado a where a.fechaBaja is null")
	public Iterable<ArticuloManufacturado> findAllArticulosManufacturadosAlta();

	@Query("select a from ArticuloManufacturado a where fk_rubro_general = ?1")
	public Iterable<ArticuloManufacturado> findByfkRubro(Long id);
}
