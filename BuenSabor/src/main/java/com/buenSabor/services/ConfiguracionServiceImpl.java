package com.buenSabor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.buenSabor.entity.Configuracion;
import com.buenSabor.repository.ConfiguracionRepository;
import com.commons.services.CommonServiceImpl;

@Service
@Transactional
public class ConfiguracionServiceImpl extends CommonServiceImpl<Configuracion, ConfiguracionRepository> implements ConfiguracionService{
	
	@Autowired
	private ConfiguracionRepository configuracionRepository;

}
