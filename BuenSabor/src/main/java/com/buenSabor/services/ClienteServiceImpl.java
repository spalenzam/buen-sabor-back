package com.buenSabor.services;

import org.springframework.stereotype.Service;

import com.buenSabor.entity.Cliente;
import com.buenSabor.repository.ClienteRepository;
import com.commons.services.CommonServiceImpl;

@Service
public class ClienteServiceImpl extends CommonServiceImpl<Cliente, ClienteRepository> implements ClienteService {

}
