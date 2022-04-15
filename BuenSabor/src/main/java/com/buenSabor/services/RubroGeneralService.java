package com.buenSabor.services;

import com.buenSabor.entity.RubroGeneral;
import com.commons.services.CommonService;

public interface RubroGeneralService extends CommonService<RubroGeneral>{
	public Iterable<RubroGeneral> findAllRubroGeneralAlta();
}
