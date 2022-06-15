package com.buenSabor.services;

import java.util.Optional;

import com.buenSabor.entity.Cliente;
import com.commons.services.CommonService;

public interface ClienteService extends CommonService<Cliente>{
	
	Cliente updateCliente(Cliente cliente, Long id);
	
	Optional<Cliente>findByEmail(String email);
}
