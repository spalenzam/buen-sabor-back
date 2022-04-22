package com.buenSabor.services;

import java.util.List;

import com.buenSabor.entity.ArticuloManufacturado;
import com.buenSabor.services.dto.ArticuloManufacturadoDTO;
import com.commons.services.CommonService;

public interface ArticuloManufacturadoService extends CommonService<ArticuloManufacturado>{
	
	Iterable<ArticuloManufacturado> findAllArticulosManufacturadosAlta();
	
	ArticuloManufacturado deleteByIdAndBaja(Long id);

	List<ArticuloManufacturadoDTO> findCantidadDisponible();
}
