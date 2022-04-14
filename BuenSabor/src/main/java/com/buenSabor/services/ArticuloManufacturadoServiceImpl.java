package com.buenSabor.services;

import org.springframework.stereotype.Service;

import com.buenSabor.entity.ArticuloManufacturado;
import com.buenSabor.repository.ArticuloManufacturadoRepository;
import com.commons.services.CommonServiceImpl;

@Service
public class ArticuloManufacturadoServiceImpl extends CommonServiceImpl<ArticuloManufacturado, ArticuloManufacturadoRepository> implements ArticuloManufacturadoService{

}
