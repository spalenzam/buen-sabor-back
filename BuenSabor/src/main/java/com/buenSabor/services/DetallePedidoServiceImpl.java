package com.buenSabor.services;

import org.springframework.stereotype.Service;

import com.buenSabor.entity.DetallePedido;
import com.buenSabor.repository.DetallePedidoRepository;
import com.commons.services.CommonServiceImpl;

@Service
public class DetallePedidoServiceImpl extends CommonServiceImpl<DetallePedido, DetallePedidoRepository> implements DetallePedidoService{

}
