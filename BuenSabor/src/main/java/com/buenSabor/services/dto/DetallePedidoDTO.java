package com.buenSabor.services.dto;


public class DetallePedidoDTO {

	String descripcion;
	
	int cantidad;
	
    int totalPagar;

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public int getTotalPagar() {
		return totalPagar;
	}

	public void setTotalPagar(int totalPagar) {
		this.totalPagar = totalPagar;
	}
    
    
}
