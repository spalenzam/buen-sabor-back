package com.buenSabor.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buenSabor.entity.Usuario;
import com.buenSabor.repository.UsuarioRepository;
import com.buenSabor.services.errors.BuenSaborException;
import com.buenSabor.services.errors.ErrorConstants;
import com.commons.services.CommonServiceImpl;

@Service
public class UsuarioServiceImpl extends CommonServiceImpl<Usuario, UsuarioRepository> implements UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public Iterable<Usuario> findAllUsuarioAlta() {
		// TODO Auto-generated method stub
		return usuarioRepository.findAllUsuarioAlta();
	}

	@Override
	public Usuario deleteByIdAndBaja(Long id) {
		Optional<Usuario> usuarioOptional = findById(id);

		if (usuarioOptional.isPresent()) {

			usuarioOptional.get().setFechaBaja(new Date());
			save(usuarioOptional.get());
			return usuarioOptional.get();
		} else {
			throw new BuenSaborException(ErrorConstants.ERR_BUSCAR,
					"No se encontró la entidad a la cual le quiere dar de baja");
		}
	}

	@Override
	public Optional<Usuario> findByUsuario(String usuario) {
		// TODO Auto-generated method stub
		return usuarioRepository.findByUsuario(usuario);
	}

}
