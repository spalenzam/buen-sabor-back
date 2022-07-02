package com.buenSabor.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;

import com.buenSabor.entity.ArticuloInsumo;
import com.buenSabor.entity.ArticuloManufacturado;
import com.buenSabor.entity.ArticuloManufacturadoDetalle;

public interface ArticuloManufacturadoDetalleRepository extends JpaRepository<ArticuloManufacturadoDetalle, Long> {
	
	@Query("select a from ArticuloManufacturadoDetalle a where a.articulomanufacturado = ?1")
	List<ArticuloManufacturadoDetalle> findByArticuloManufacturado(ArticuloManufacturado articuloManufacturado);

	@Query("select a from ArticuloManufacturadoDetalle a where a.articuloinsumo = ?1")
	List<ArticuloManufacturadoDetalle> findByArticuloInsumo(ArticuloInsumo a);

}
