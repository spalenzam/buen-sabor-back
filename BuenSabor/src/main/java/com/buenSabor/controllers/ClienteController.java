package com.buenSabor.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.buenSabor.entity.Cliente;
import com.buenSabor.entity.Usuario;
import com.buenSabor.services.ClienteService;
import com.buenSabor.services.UsuarioService;
import com.commons.controllers.CommonController;

@RestController
@RequestMapping(path = "api/buensabor/clientes")
public class ClienteController extends CommonController<Cliente, ClienteService> {

	private final UsuarioService usuarioService;

	public ClienteController(UsuarioService usuarioService) {
		super();
		this.usuarioService = usuarioService;
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@Valid @RequestBody Cliente cliente, BindingResult result, @PathVariable Long id) {

		if (result.hasErrors()) {
			return this.validar(result);
		}

		//Optional<Cliente> o = service.findById(id);
		Optional<Cliente> o = service.findById(id);
		if (o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		// con el id se reemplaza el cliente
		//Cliente clienteDB = o.get();
		//clienteDB.setNombre(clienteDB.getNombre());
		//clienteDB.setApellido(cliente.getApellido());
		//clienteDB.setTelefono(cliente.getTelefono());

		Cliente clienteDB = service.updateCliente(cliente, id);

		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(clienteDB));
	}

	@PostMapping("/crearUsuarioYCliente")
	public ResponseEntity<?> crearUsuarioYCliente(@Valid Cliente cliente, BindingResult result,
			@RequestParam String contrasena) { // Binding.. -> A través del resultado obtenemos los msj de error, y
												// tiene que ir justo dsp del request body

		Optional<Cliente> clienteOptional = service.findByEmail(cliente.getEmail());

		if (!clienteOptional.isPresent()) {

			if (result.hasErrors()) {
				return this.validar(result);
			}
			
			cliente.setDomicilio(cliente.getDomicilio());
			
			Cliente entityDB = service.save(cliente);

			Usuario usuario = new Usuario();
			usuario.setClave(contrasena);
			usuario.setCliente(cliente);
			usuario.setRol("Cliente");
			usuario.setUsuario(cliente.getEmail());

			usuarioService.save(usuario);

			return ResponseEntity.status(HttpStatus.CREATED).body(entityDB);

		} else {

			return ResponseEntity.status(HttpStatus.CREATED).body(clienteOptional.get());

		}
	}
	
	@GetMapping("/cliente/{email}")
	public ResponseEntity<?> obtenerCliente (@PathVariable String email){
		Optional<Cliente> o = service.findByEmail(email);
		if(o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(o.get());
	}
}
