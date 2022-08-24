package com.buenSabor.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.buenSabor.entity.ArticuloInsumo;
import com.buenSabor.entity.ArticuloManufacturadoDetalle;
import com.buenSabor.repository.ArticuloInsumoRepository;
import com.buenSabor.services.errors.BuenSaborException;
import com.buenSabor.services.errors.ErrorConstants;
import com.commons.services.CommonServiceImpl;

@Service
@Transactional
public class ArticuloInsumoServiceImpl extends CommonServiceImpl<ArticuloInsumo, ArticuloInsumoRepository> implements ArticuloInsumoService{

	@Autowired
	private ArticuloInsumoRepository articuloInsumoRepository;
	
	private final ArticuloManufacturadoDetalleService articuloManufacturadoDetalleService;

	
	public ArticuloInsumoServiceImpl(ArticuloManufacturadoDetalleService articuloManufacturadoDetalleService) {
		super();
		this.articuloManufacturadoDetalleService = articuloManufacturadoDetalleService;
	}
	

	@Override
	public Iterable<ArticuloInsumo> findAllArticulosAlta() {
		return articuloInsumoRepository.findAllAlta();
	}
	
	@Override
	public ArticuloInsumo deleteByIdAndBaja(Long id) {
		
		Optional<ArticuloInsumo> articuloInsumoOptional = findById(id);
		
		if(articuloInsumoOptional.isPresent()){
			
			List<ArticuloManufacturadoDetalle> detalles = articuloManufacturadoDetalleService.findByArticuloInsumo(articuloInsumoOptional.get());
			
			for(ArticuloManufacturadoDetalle a : detalles) {
				articuloManufacturadoDetalleService.deleteById(a.getId());
			}
			
			articuloInsumoOptional.get().setFechaBaja(new Date());
			save(articuloInsumoOptional.get()); 
			return articuloInsumoOptional.get();
		}else {
			throw new BuenSaborException(ErrorConstants.ERR_BUSCAR,"No se encontró la entidad a la cual le quiere dar de baja");
		}
	}


	@Override
	public Iterable<ArticuloInsumo> findImagenRubro(Long id) {
		return articuloInsumoRepository.findByfkRubro(id);
	}

}
