package com.buenSabor.services;

import com.buenSabor.entity.Cliente;
import com.commons.services.CommonService;

public interface ClienteService extends CommonService<Cliente>{
	
	Cliente updateCliente(Cliente cliente, Long id);
}
