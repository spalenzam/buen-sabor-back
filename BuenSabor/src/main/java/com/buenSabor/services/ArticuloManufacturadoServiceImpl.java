package com.buenSabor.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buenSabor.entity.ArticuloManufacturado;
import com.buenSabor.repository.ArticuloManufacturadoRepository;
import com.buenSabor.services.errors.BuenSaborException;
import com.buenSabor.services.errors.ErrorConstants;
import com.commons.services.CommonServiceImpl;

@Service
public class ArticuloManufacturadoServiceImpl extends CommonServiceImpl<ArticuloManufacturado, ArticuloManufacturadoRepository> implements ArticuloManufacturadoService{

	@Autowired
	private ArticuloManufacturadoRepository articuloManufacturadoRepository;

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

}
