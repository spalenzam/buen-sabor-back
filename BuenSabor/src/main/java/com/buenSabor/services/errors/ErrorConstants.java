package com.buenSabor.services.errors;

import java.net.URI;

public class ErrorConstants {
	
	public static final String ERR_BUSCAR = "Búsqueda de entidad";
	public static final String ERR_CREAR = "Creación de entidad";
	public static final String PROBLEM_BASE_URL = "httpsÑ//www.jhipster.tech/problem";
	
	public static final URI DEFAULT_TYPE = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
	
	private ErrorConstants() {}

}
