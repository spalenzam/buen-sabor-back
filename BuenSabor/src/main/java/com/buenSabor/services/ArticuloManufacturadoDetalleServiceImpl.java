package com.buenSabor.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buenSabor.entity.ArticuloInsumo;
import com.buenSabor.entity.ArticuloManufacturado;
import com.buenSabor.entity.ArticuloManufacturadoDetalle;
import com.buenSabor.repository.ArticuloManufacturadoDetalleRepository;
import com.commons.services.CommonServiceImpl;

@Service
public class ArticuloManufacturadoDetalleServiceImpl extends CommonServiceImpl<ArticuloManufacturadoDetalle, ArticuloManufacturadoDetalleRepository> implements ArticuloManufacturadoDetalleService{
	
	@Autowired
	ArticuloManufacturadoDetalleRepository articuloManufacturadoDetalleRepository;


	@Override
	public List<ArticuloManufacturadoDetalle> findByArticuloManufacturado(ArticuloManufacturado a) {
		// TODO Auto-generated method stub
		return articuloManufacturadoDetalleRepository.findByArticuloManufacturado(a);
	}

	@Override
	public ArticuloManufacturadoDetalle updateDetalle(ArticuloManufacturadoDetalle artmanufacturadodetalle, Long id) {
		
		Optional<ArticuloManufacturadoDetalle> o = findById(id);
		if(o.isEmpty()) {
			return o.get();
		}
		
		ArticuloManufacturadoDetalle artmanufacturadodetalleDB = o.get();
		artmanufacturadodetalleDB.setCantidad(artmanufacturadodetalle.getCantidad());
		artmanufacturadodetalleDB.setUnidadMedida(artmanufacturadodetalle.getUnidadMedida());
		
		
		
		
		save(artmanufacturadodetalleDB);
		
		
		return artmanufacturadodetalleDB;
	}
	
	@Override
	public List<ArticuloManufacturadoDetalle> findByArticuloInsumo(ArticuloInsumo a) {
		// TODO Auto-generated method stub
		return articuloManufacturadoDetalleRepository.findByArticuloInsumo(a);
	}

}
