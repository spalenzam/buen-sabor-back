package com.buenSabor.services;

import org.springframework.stereotype.Service;

import com.buenSabor.entity.Pedido;
import com.buenSabor.repository.PedidoRepository;
import com.commons.services.CommonServiceImpl;

@Service
public class PedidoServiceImpl extends CommonServiceImpl<Pedido, PedidoRepository> implements PedidoService {


}
