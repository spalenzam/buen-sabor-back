package com.buenSabor.services;

import org.springframework.stereotype.Service;

import com.buenSabor.entity.DetalleFactura;
import com.buenSabor.repository.DetalleFacturaRepository;
import com.commons.services.CommonServiceImpl;

@Service
public class DetalleFacturaServiceImpl extends CommonServiceImpl<DetalleFactura, DetalleFacturaRepository> implements DetalleFacturaService{

}
