package com.buenSabor.services.dto;

import com.buenSabor.entity.ArticuloManufacturado;

public class ArticuloManufacturadoDTO {
	
	ArticuloManufacturado articuloManufacturado;
	
	Double cantidadDisponible;

	public ArticuloManufacturado getArticuloManufacturado() {
		return articuloManufacturado;
	}

	public void setArticuloManufacturado(ArticuloManufacturado articuloManufacturado) {
		this.articuloManufacturado = articuloManufacturado;
	}

	public Double getCantidadDisponible() {
		return cantidadDisponible;
	}

	public void setCantidadDisponible(Double cantidadDisponible) {
		this.cantidadDisponible = cantidadDisponible;
	}
	
	

}
