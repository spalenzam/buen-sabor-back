package com.buenSabor.services;

import java.util.List;

import com.buenSabor.entity.ArticuloManufacturado;
import com.buenSabor.entity.ArticuloManufacturadoDetalle;
import com.commons.services.CommonService;

public interface ArticuloManufacturadoDetalleService extends CommonService<ArticuloManufacturadoDetalle>{

	
	List<ArticuloManufacturadoDetalle> findByArticuloManufacturado(ArticuloManufacturado a);

}
