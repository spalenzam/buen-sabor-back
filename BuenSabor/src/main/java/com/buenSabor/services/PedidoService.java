package com.buenSabor.services;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import com.buenSabor.entity.Pedido;
import com.buenSabor.services.dto.PedidosPorClienteDTO;
import com.commons.services.CommonService;

public interface PedidoService extends CommonService<Pedido>{
	
	Iterable<Pedido> findAllPedidosAlta();
	
	Pedido deleteByIdAndBaja(Long id);
	
	List getAllEstados();
	
	List getAllEstadosInternos();

	List<PedidosPorClienteDTO> listarPedidosPorCliente(Date desde, Date hasta);

	InputStream generarExcelPedidosPorCliente(List<PedidosPorClienteDTO> listaPedidos, Date desde, Date hasta);

	Pedido ultimoPedido();
}
