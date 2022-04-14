package com.buenSabor.services;

import org.springframework.stereotype.Service;

import com.buenSabor.entity.RubroGeneral;
import com.buenSabor.repository.RubroGeneralRepository;
import com.commons.services.CommonServiceImpl;

@Service
public class RubroGeneralServiceImpl extends CommonServiceImpl<RubroGeneral, RubroGeneralRepository> implements RubroGeneralService{

}
