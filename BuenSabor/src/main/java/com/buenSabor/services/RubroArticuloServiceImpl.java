package com.buenSabor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buenSabor.entity.RubroArticulo;
import com.buenSabor.repository.RubroArticuloRepository;
import com.commons.services.CommonServiceImpl;

@Service
public class RubroArticuloServiceImpl extends CommonServiceImpl<RubroArticulo, RubroArticuloRepository> implements RubroArticuloService{

	@Autowired
	private RubroArticuloRepository rubroArticuloRepository;
	
	@Override
	public Iterable<RubroArticulo> findAllRubroArticuloAlta() {
		// TODO Auto-generated method stub
		return rubroArticuloRepository.findAllRubroArticuloAlta();
	}

}
