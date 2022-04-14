package com.buenSabor.services;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.buenSabor.entity.Factura;
import com.buenSabor.repository.FacturaRepository;
import com.commons.services.CommonServiceImpl;

@Service
public class FacturaServiceImpl extends CommonServiceImpl<Factura, FacturaRepository> implements FacturaService {

	@Override
	@Transactional(readOnly = true)
	public Factura findByNumeroFactura(Integer numFactura) {
		return repository.findByNumeroFactura(numFactura);
	}


}
