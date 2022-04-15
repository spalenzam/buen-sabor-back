package com.buenSabor.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buenSabor.entity.RubroGeneral;
import com.buenSabor.repository.RubroGeneralRepository;
import com.buenSabor.services.errors.BuenSaborException;
import com.buenSabor.services.errors.ErrorConstants;
import com.commons.services.CommonServiceImpl;

@Service
public class RubroGeneralServiceImpl extends CommonServiceImpl<RubroGeneral, RubroGeneralRepository>
		implements RubroGeneralService {

	@Autowired
	private RubroGeneralRepository rubroGeneralRepository;

	@Override
	public Iterable<RubroGeneral> findAllRubroGeneralAlta() {
		// TODO Auto-generated method stub
		return rubroGeneralRepository.findAllRubroGeneralAlta();
	}

	@Override
	public RubroGeneral deleteByIdAndBaja(Long id) {
		Optional<RubroGeneral> rubroGeneralOptional = findById(id);

		if (rubroGeneralOptional.isPresent()) {

			rubroGeneralOptional.get().setFechaBaja(new Date());
			save(rubroGeneralOptional.get());
			return rubroGeneralOptional.get();
		} else {
			throw new BuenSaborException(ErrorConstants.ERR_BUSCAR,
					"No se encontró la entidad a la cual le quiere dar de baja");
		}
	}

}
