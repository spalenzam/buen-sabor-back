package com.buenSabor.services;



import java.io.ByteArrayInputStream;
import java.util.Date;

import com.buenSabor.entity.Factura;
import com.buenSabor.services.dto.FacturaDTO;
import com.buenSabor.services.dto.IngresosDiarioYMensualDTO;
import com.commons.services.CommonService;

public interface FacturaService extends CommonService<Factura> {

	public Factura findByNumeroFactura(Integer numFactura);
	
	public FacturaDTO datosFacturaDto(Factura factura) throws Exception;
	
	public ByteArrayInputStream generarFacturaPDF(Factura factura) throws Exception;

	public IngresosDiarioYMensualDTO ingresoMensual(Date date);
	
	ByteArrayInputStream generarExcelIngresoMensual(IngresosDiarioYMensualDTO ingresosDiarioYMensualDTO , Date date) throws Exception;

	public IngresosDiarioYMensualDTO ingresoDiario(Date fecha);
	
	ByteArrayInputStream generarExcelIngresoDiario(IngresosDiarioYMensualDTO ingresosDiarioYMensualDTO , Date date) throws Exception;

	public IngresosDiarioYMensualDTO gananciasPorFecha(Date desde, Date hasta);
	
	ByteArrayInputStream generarExcelGananciasPorFecha(IngresosDiarioYMensualDTO ingresosDiarioYMensualDTO, Date desde, Date hasta) throws Exception;
}
