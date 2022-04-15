package com.buenSabor.services;

import com.buenSabor.entity.Usuario;
import com.commons.services.CommonService;

public interface UsuarioService extends CommonService<Usuario> {
	
	Iterable<Usuario> findAllUsuarioAlta();
	
	Usuario deleteByIdAndBaja(Long id);
}
