package com.buenSabor.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
