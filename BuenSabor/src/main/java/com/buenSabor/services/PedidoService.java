package com.buenSabor.services;

import java.util.List;

import com.buenSabor.entity.Pedido;
import com.commons.services.CommonService;

public interface PedidoService extends CommonService<Pedido>{
	
	Iterable<Pedido> findAllPedidosAlta();
	
	Pedido deleteByIdAndBaja(Long id);
	
	List getAllEstados();
	
	List getAllEstadosInternos();
	
}
