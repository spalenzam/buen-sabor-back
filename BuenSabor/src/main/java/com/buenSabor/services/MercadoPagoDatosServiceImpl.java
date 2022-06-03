package com.buenSabor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buenSabor.entity.MercadoPagoDatos;
import com.buenSabor.repository.MercadoPagoDatosRepository;
import com.commons.services.CommonServiceImpl;


@Service
public class MercadoPagoDatosServiceImpl extends CommonServiceImpl<MercadoPagoDatos, MercadoPagoDatosRepository> implements MercadoPagoDatosService {

	@Autowired
	protected MercadoPagoDatosRepository mercadoPagoDatosRepository;
	
	
	 
}
