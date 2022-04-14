package com.buenSabor.services;

import org.springframework.stereotype.Service;

import com.buenSabor.entity.ArticuloInsumo;
import com.buenSabor.repository.ArticuloInsumoRepository;
import com.commons.services.CommonServiceImpl;

@Service
public class ArticuloInsumoServiceImpl extends CommonServiceImpl<ArticuloInsumo, ArticuloInsumoRepository> implements ArticuloInsumoService{

}
