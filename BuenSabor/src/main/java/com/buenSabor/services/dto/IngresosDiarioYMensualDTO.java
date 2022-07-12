package com.buenSabor.services.dto;

import java.util.List;

import com.buenSabor.entity.Factura;

public class IngresosDiarioYMensualDTO {
	
	
	private List<Factura> factura;
	
	private Double ingreso;

	
	
	public List<Factura> getFactura() {
		return factura;
	}

	public void setFactura(List<Factura> factura) {
		this.factura = factura;
	}

	public Double getIngreso() {
		return ingreso;
	}

	public void setIngreso(Double ingreso) {
		this.ingreso = ingreso;
	}

}
