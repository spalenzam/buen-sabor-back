package com.buenSabor.services;

import com.buenSabor.entity.RubroArticulo;
import com.commons.services.CommonService;

public interface RubroArticuloService extends CommonService<RubroArticulo>{
	
	Iterable<RubroArticulo> findAllRubroArticuloAlta();
	
	RubroArticulo deleteByIdAndBaja(Long id);

}
