package com.buenSabor.services.dto;

import com.buenSabor.entity.Cliente;

public class PedidosPorClienteDTO {
	
	private Cliente cliente;
	
	private Integer cantidadDePedidos;

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Integer getCantidadDePedidos() {
		return cantidadDePedidos;
	}

	public void setCantidadDePedidos(Integer cantidadDePedidos) {
		this.cantidadDePedidos = cantidadDePedidos;
	}
	
	
	

}
