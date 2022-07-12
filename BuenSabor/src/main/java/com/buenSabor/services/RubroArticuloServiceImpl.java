package com.buenSabor.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buenSabor.entity.RubroArticulo;
import com.buenSabor.repository.RubroArticuloRepository;
import com.buenSabor.services.errors.BuenSaborException;
import com.buenSabor.services.errors.ErrorConstants;
import com.commons.services.CommonServiceImpl;

@Service
public class RubroArticuloServiceImpl extends CommonServiceImpl<RubroArticulo, RubroArticuloRepository>
		implements RubroArticuloService {

	@Autowired
	private RubroArticuloRepository rubroArticuloRepository;

	@Override
	public Iterable<RubroArticulo> findAllRubroArticuloAlta() {
		// TODO Auto-generated method stub
		return rubroArticuloRepository.findAllRubroArticuloAlta();
	}

	@Override
	public RubroArticulo deleteByIdAndBaja(Long id) {
		Optional<RubroArticulo> rubroArticuloOptional = findById(id);
		
		System.out.println(rubroArticuloOptional.get().getId());

		if (rubroArticuloOptional.isPresent()) {
			
			//Si es hijo le doy de baja a él nada más
			if(rubroArticuloOptional.get().getRubroarticuloPadre() != null) {
				
				rubroArticuloOptional.get().setFechaBaja(new Date());
				save(rubroArticuloOptional.get());
				
				return rubroArticuloOptional.get();
			}else {
				
				//Busco los hijos
				//List<RubroArticulo> rubroArticuloHijosList = rubroArticuloRepository.findHijos(rubroArticuloOptional.get().getId());
				List<RubroArticulo> rubroArticuloHijosList = rubroArticuloOptional.get().getRubroarticuloHijos();
				
				//recorro la lista y los voy eliminando
				for(RubroArticulo rubroArticulo : rubroArticuloHijosList) {
					rubroArticulo.setFechaBaja(new Date());
					save(rubroArticulo);
				}
				
				//Por último lo eliminó a él
				rubroArticuloOptional.get().setFechaBaja(new Date());
				save(rubroArticuloOptional.get());
				
				return rubroArticuloOptional.get();
			}

			
		} else {
			throw new BuenSaborException(ErrorConstants.ERR_BUSCAR,
					"No se encontró la entidad a la cual le quiere dar de baja");
		}
	}

}
