package com.buenSabor.services;

import org.springframework.stereotype.Service;

import com.buenSabor.entity.Usuario;
import com.buenSabor.repository.UsuarioRepository;
import com.commons.services.CommonServiceImpl;

@Service
public class UsuarioServiceImpl extends CommonServiceImpl<Usuario, UsuarioRepository> implements UsuarioService {

}
