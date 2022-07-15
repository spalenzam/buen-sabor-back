package com.buenSabor.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buenSabor.entity.ArticuloManufacturado;
import com.buenSabor.entity.ArticuloManufacturadoDetalle;
import com.buenSabor.repository.ArticuloManufacturadoRepository;
import com.buenSabor.services.dto.ArticuloManufacturadoDTO;
import com.buenSabor.services.errors.BuenSaborException;
import com.buenSabor.services.errors.ErrorConstants;
import com.commons.services.CommonServiceImpl;

@Service
public class ArticuloManufacturadoServiceImpl extends CommonServiceImpl<ArticuloManufacturado, ArticuloManufacturadoRepository> implements ArticuloManufacturadoService{

	@Autowired
	private ArticuloManufacturadoRepository articuloManufacturadoRepository;
	
	private final ArticuloManufacturadoDetalleService articuloManufacturadoDetalleService;
	

	public ArticuloManufacturadoServiceImpl(ArticuloManufacturadoRepository articuloManufacturadoRepository,
			ArticuloManufacturadoDetalleService articuloManufacturadoDetalleService) {
		super();
		this.articuloManufacturadoRepository = articuloManufacturadoRepository;
		this.articuloManufacturadoDetalleService = articuloManufacturadoDetalleService;
	}

	@Override
	public Iterable<ArticuloManufacturado> findAllArticulosManufacturadosAlta() {
		// TODO Auto-generated method stub
		return articuloManufacturadoRepository.findAllArticulosManufacturadosAlta();
	}
	
	@Override
	public ArticuloManufacturado deleteByIdAndBaja(Long id) {
		
		Optional<ArticuloManufacturado> articuloManufacturadoOptional = findById(id);
		
		if(articuloManufacturadoOptional.isPresent()){
			
			articuloManufacturadoOptional.get().setFechaBaja(new Date());
			save(articuloManufacturadoOptional.get()); 
			return articuloManufacturadoOptional.get();
		}else {
			throw new BuenSaborException(ErrorConstants.ERR_BUSCAR,"No se encontró la entidad a la cual le quiere dar de baja");
		}
	}
	
	@Override
	public List<ArticuloManufacturadoDTO> findCantidadDisponible(){
		
		List<ArticuloManufacturadoDTO> articulosArticuloManufacturadoDTOList = new ArrayList<>();
		
		//Traigo todos los articulos dados de alta
		List<ArticuloManufacturado> articulosManufacturados = (List<ArticuloManufacturado>) findAllArticulosManufacturadosAlta();
		
		//Los recorro
		for(ArticuloManufacturado a : articulosManufacturados) {
			
			//Busco de cada manufacturado el detalla
			List<ArticuloManufacturadoDetalle> articulosManufacturadosDetalles = articuloManufacturadoDetalleService.findByArticuloManufacturado(a);
			
			Double cantidad = null;
			
			for(ArticuloManufacturadoDetalle articuloManufacturadoDetalle : articulosManufacturadosDetalles) {
				
				Double cantidadDisponible = articuloManufacturadoDetalle.getArticuloinsumo().getStockActual() - articuloManufacturadoDetalle.getArticuloinsumo().getStockMinimo();
				
				Double cantidadReal = cantidadDisponible/articuloManufacturadoDetalle.getCantidad();
				
				if(cantidad == null) {
					cantidad = cantidadReal;
				}else if(cantidadReal < cantidad ) {
					cantidad = cantidadReal;
				}
				
			}
			
			ArticuloManufacturadoDTO articuloManufacturadoDTO = new ArticuloManufacturadoDTO();
			articuloManufacturadoDTO.setArticuloManufacturado(a);
			articuloManufacturadoDTO.setCantidadDisponible(cantidad);
			
			articulosArticuloManufacturadoDTOList.add(articuloManufacturadoDTO);
		}
		
		return articulosArticuloManufacturadoDTOList;
	}
	
	@Override
	public Iterable<ArticuloManufacturado> findImagenRubro(Long id) {
		
		return articuloManufacturadoRepository.findByfkRubro(id);
	}

}
























