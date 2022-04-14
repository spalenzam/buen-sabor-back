package com.buenSabor.services;

import org.springframework.stereotype.Service;

import com.buenSabor.entity.Domicilio;
import com.buenSabor.repository.DomicilioRepository;
import com.commons.services.CommonServiceImpl;

@Service
public class DomicilioServiceImpl extends CommonServiceImpl<Domicilio, DomicilioRepository> implements DomicilioService{

}
