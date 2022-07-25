package com.buenSabor.services;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

import com.buenSabor.entity.DetallePedido;
import com.buenSabor.services.dto.RakingComidasDTO;
import com.commons.services.CommonService;

public interface DetallePedidoService extends CommonService<DetallePedido> {
	
	List<RakingComidasDTO> rankingDeComidas(Date desde, Date hasta);

	Iterable<DetallePedido> findDetPedidos(Long id);
	
	ByteArrayInputStream generarExcelRanking(List<RakingComidasDTO> rakingComidasDTOs, Date desde, Date hasta) throws Exception;
	
}
