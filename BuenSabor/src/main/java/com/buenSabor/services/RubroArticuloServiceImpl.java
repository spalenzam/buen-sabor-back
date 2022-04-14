package com.buenSabor.services;

import org.springframework.stereotype.Service;

import com.buenSabor.entity.RubroArticulo;
import com.buenSabor.repository.RubroArticuloRepository;
import com.commons.services.CommonServiceImpl;

@Service
public class RubroArticuloServiceImpl extends CommonServiceImpl<RubroArticulo, RubroArticuloRepository> implements RubroArticuloService{

}
