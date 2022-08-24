package com.buenSabor.services;

import com.buenSabor.entity.ArticuloInsumo;
import com.commons.services.CommonService;

public interface ArticuloInsumoService extends CommonService<ArticuloInsumo>{
	
	Iterable<ArticuloInsumo> findAllArticulosAlta();
	
	ArticuloInsumo deleteByIdAndBaja(Long id);

	Iterable<ArticuloInsumo> findImagenRubro(Long id);
}
