package com.buenSabor.services;

import com.buenSabor.entity.ArticuloManufacturado;
import com.commons.services.CommonService;

public interface ArticuloManufacturadoService extends CommonService<ArticuloManufacturado>{
	
	public Iterable<ArticuloManufacturado> findAllArticulosManufacturadosAlta();

}
