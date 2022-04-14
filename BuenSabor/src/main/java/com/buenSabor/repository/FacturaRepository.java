package com.buenSabor.repository;



import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.buenSabor.entity.Factura;


public interface FacturaRepository extends CrudRepository<Factura, Long>{
	
	@Query("select f from Factura f where f.numeroFactura =?1")
	Factura findByNumeroFactura(Integer numFactura);

}
