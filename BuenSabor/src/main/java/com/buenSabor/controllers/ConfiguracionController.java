package com.buenSabor.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buenSabor.entity.Configuracion;
import com.buenSabor.services.ConfiguracionService;
import com.commons.controllers.CommonController;

@RestController
@RequestMapping(path = "api/buensabor/configuracion")
public class ConfiguracionController extends CommonController<Configuracion, ConfiguracionService>{
	
	
}
