package com.buenSabor.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buenSabor.entity.Cliente;
import com.buenSabor.entity.Domicilio;
import com.buenSabor.repository.ClienteRepository;
import com.buenSabor.services.errors.BuenSaborException;
import com.commons.services.CommonServiceImpl;

@Service
public class ClienteServiceImpl extends CommonServiceImpl<Cliente, ClienteRepository> implements ClienteService {
	
	private final DomicilioService domicilioService;
	
	@Autowired
	private ClienteRepository clienteRepository;

	public ClienteServiceImpl(DomicilioService domicilioService) {
		super();
		this.domicilioService = domicilioService;
	}

	@Override
	public Cliente updateCliente(Cliente cliente, Long id) {
		
		Optional<Cliente> o = findById(id);
		if(o.isEmpty()) {
			throw new BuenSaborException("Cliente", "Cliente no encontrado");
 		}
		
		//con el id se reemplaza el cliente
		Cliente clienteDB = o.get();
		clienteDB.setNombre(cliente.getNombre());
		clienteDB.setApellido(cliente.getApellido());
		clienteDB.setTelefono(cliente.getTelefono());
		clienteDB.setEmail(cliente.getEmail());
		
		if(clienteDB.getDomicilio() != null) {
			
			Optional<Domicilio> domicilioOptional = domicilioService.findById(clienteDB.getDomicilio().getId());
			//Actualizo el domicilio si lo tiene
			if(domicilioOptional.isPresent()) {
				domicilioOptional.get().setCalle(cliente.getDomicilio().getCalle());
				domicilioOptional.get().setLocalidad(cliente.getDomicilio().getLocalidad());
				domicilioOptional.get().setNumero(cliente.getDomicilio().getNumero());
			}else {
				throw new BuenSaborException("Dirección", "No se encuentra la dirección");
			}
			
		//Me fijo si el cliente que me están enviando tiene domicilio cargado, y si no lo tiene se lo agrego
		}else {
			Domicilio nuevoDomicilio = new Domicilio();
			nuevoDomicilio.setCalle(cliente.getDomicilio().getCalle());
			nuevoDomicilio.setLocalidad(cliente.getDomicilio().getLocalidad());
			nuevoDomicilio.setNumero(cliente.getDomicilio().getNumero());
			domicilioService.save(nuevoDomicilio);
			
			clienteDB.setDomicilio(nuevoDomicilio);
		}
		
		save(clienteDB);
		return clienteDB;
	}

	@Override
	public Optional<Cliente> findByEmail(String email) {
		
		return clienteRepository.findByEmail(email);
	}
	


}
