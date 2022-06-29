package com.buenSabor.services.dto;

public class DetalleFacturaDTO {

	private String cantidad;

	private String subtotal;

	private String denominacionProducto;

	public String getCantidad() {
		return cantidad;
	}

	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

	public String getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(String subtotal) {
		this.subtotal = subtotal;
	}

	public String getDenominacionProducto() {
		return denominacionProducto;
	}

	public void setDenominacionProducto(String denominacionProducto) {
		this.denominacionProducto = denominacionProducto;
	}
	
	
	
}
