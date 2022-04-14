package com.buenSabor.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buenSabor.entity.Cliente;
import com.buenSabor.services.ClienteService;
import com.commons.controllers.CommonController;

@RestController
@RequestMapping(path="api/buensabor/clientes")
public class ClienteController extends CommonController<Cliente, ClienteService>{

	@PutMapping("/{id}")
	public ResponseEntity<?> editar (@Valid @RequestBody Cliente cliente, BindingResult result, @PathVariable Long id){
		
		if(result.hasErrors()) {
			return this.validar(result);
		}	
			
		
		Optional<Cliente> o = service.findById(id);
		if(o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		//con el id se remplaza el cliente
		Cliente clienteDB = o.get();
		clienteDB.setNombre(clienteDB.getNombre());
		clienteDB.setApellido(cliente.getApellido());
		clienteDB.setTelefono(cliente.getTelefono());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(clienteDB));
	}
}
