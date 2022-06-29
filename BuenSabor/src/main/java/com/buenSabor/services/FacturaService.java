package com.buenSabor.services;



import java.io.ByteArrayInputStream;

import com.buenSabor.entity.Factura;
import com.buenSabor.services.dto.FacturaDTO;
import com.commons.services.CommonService;

public interface FacturaService extends CommonService<Factura> {

	public Factura findByNumeroFactura(Integer numFactura);
	
	public FacturaDTO datosFacturaDto(Factura factura) throws Exception;
	
	public ByteArrayInputStream generarFacturaPDF(Factura factura) throws Exception;
}
