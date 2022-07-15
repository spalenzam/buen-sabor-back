package com.buenSabor.services;



import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.buenSabor.entity.ArticuloManufacturado;
import com.buenSabor.entity.DetallePedido;
import com.buenSabor.repository.DetallePedidoRepository;
import com.buenSabor.services.dto.RakingComidasDTO;
import com.commons.services.CommonServiceImpl;

@Service
public class DetallePedidoServiceImpl extends CommonServiceImpl<DetallePedido, DetallePedidoRepository> implements DetallePedidoService{
	
	private final ArticuloInsumoService articuloInsumoService;
		
	@Autowired
	private DetallePedidoRepository detPedidoRepository;
	
	public DetallePedidoServiceImpl(ArticuloInsumoService articuloInsumoService) {
		super();
		this.articuloInsumoService = articuloInsumoService;
	}
	
	@Override
	public Iterable<DetallePedido> findDetPedidos(Long id) {
			
		return detPedidoRepository.findByfkPedido(id);
	}


	@Override
	public List<RakingComidasDTO> rankingDeComidas(Date desde, Date hasta) {
		
		List<DetallePedido> detallePedidos = repository.rankingDeComidas(desde, hasta);
		
		System.out.println("Acá"+detallePedidos);
		
		List<RakingComidasDTO> rakingComidasDTOs = new ArrayList<>();
		
        
        Map<ArticuloManufacturado, Long> requirementCountMap = detallePedidos.stream().filter(a -> a.getArticulomanufacturado() != null).collect(Collectors.groupingBy(DetallePedido::getArticulomanufacturado, Collectors.counting()));

        requirementCountMap.entrySet()
        .forEach(e -> {
        		
        		RakingComidasDTO rankingComidas = new RakingComidasDTO();
        
    			rankingComidas.setDenominacion(e.getKey().getDenominacion());
    			rankingComidas.setCanti(e.getValue().doubleValue());
    			
    			Double cantidadVendida = 0.0;
    			for(DetallePedido detallePedido : detallePedidos) {
    				
    				if(detallePedido.getArticulomanufacturado() != null && detallePedido.getArticulomanufacturado().getId() == e.getKey().getId()) {
    					cantidadVendida += (detallePedido.getCantidad());
    				}
    			}
    			
    			System.out.println(cantidadVendida);
    			System.out.println(e.getValue().doubleValue());
    			
    			rankingComidas.setCantidadTotal(cantidadVendida);
    			rankingComidas.setManufacturadoId(e.getKey().getId());
    			
    			rakingComidasDTOs.add(rankingComidas);
    	
    			
        });
        
        //System.out.println("Hola" +requirementCountMap);
		
		
		
		return rakingComidasDTOs.stream().sorted(Comparator.comparingDouble(RakingComidasDTO::getCantidadTotal).reversed()).collect(Collectors.toList());
	}

}
