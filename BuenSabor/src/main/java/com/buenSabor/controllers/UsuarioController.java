package com.buenSabor.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buenSabor.entity.Configuracion;
import com.buenSabor.entity.Domicilio;
import com.buenSabor.entity.Usuario;
import com.buenSabor.services.ClienteService;
import com.buenSabor.services.DomicilioService;
import com.buenSabor.services.UsuarioService;
import com.commons.controllers.CommonController;

@RestController
@RequestMapping(path="api/buensabor/usuarios")
public class UsuarioController extends CommonController<Usuario, UsuarioService> {
	
	private final ClienteService clienteService;
	
	private final DomicilioService domicilioService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public UsuarioController(ClienteService clienteService, DomicilioService domicilioService) {
		super();
		this.clienteService = clienteService;
		this.domicilioService = domicilioService;
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> editar (@Valid @RequestBody Usuario usuario, BindingResult result, @PathVariable Long id){
		
		if(result.hasErrors()) {
			return this.validar(result);
		}			
		
		Optional<Usuario> o = service.findById(id);
		if(o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		//Una vez que encontramos el id reemplazamos el Usuario
		Usuario usuarioDB = o.get();
		usuarioDB.setUsuario(usuario.getUsuario()); //se modifica la contraseña
		usuarioDB.setClave(usuario.getClave()); //se modifica la contraseña
		
		if(usuarioDB.getCliente() != null){
			clienteService.updateCliente(usuario.getCliente(), usuarioDB.getCliente().getId());
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(usuarioDB));
	}
	
	@GetMapping("/alta")
	public ResponseEntity<?> listarAlta(){
		return ResponseEntity.ok().body(service.findAllUsuarioAlta());
	}
	
	@PutMapping("/dar-de-baja/{id}")
	public ResponseEntity<?> darDeBaja(@PathVariable Long id){
		//service.deleteByIdAndBaja(id);
		return ResponseEntity.status(HttpStatus.CREATED).body(service.deleteByIdAndBaja(id));
	}
	
	@PostMapping("/crear")
	public ResponseEntity<?> crearUsuarioYCliente(@Valid @RequestBody Usuario usuario, BindingResult result) { // Binding.. -> A través del resultado obtenemos los msj de error, y
												// tiene que ir justo dsp del request body
		if (result.hasErrors()) {
			return this.validar(result);
		}
		
		Optional<Usuario> usuarioOptional = service.findByUsuario(usuario.getUsuario());

		if (!usuarioOptional.isPresent()) {

			if(usuario.getCliente() !=null && usuario.getCliente().getDomicilio() !=null) {
				
				Domicilio domicilio = new Domicilio();
				
				domicilio.setCalle(usuario.getCliente().getDomicilio().getCalle());
				domicilio.setLocalidad(usuario.getCliente().getDomicilio().getLocalidad());
				domicilio.setNumero(usuario.getCliente().getDomicilio().getNumero());
				
				domicilioService.save(domicilio);
				
				usuario.getCliente().setDomicilio(domicilio);
			}
			

			
			usuario.setClave(passwordEncoder.encode(usuario.getClave()));

			return ResponseEntity.status(HttpStatus.CREATED).body(service.save(usuario));

		} else {
			
			usuarioOptional.get().setClave(passwordEncoder.encode(usuario.getClave()));

			return ResponseEntity.status(HttpStatus.CREATED).body(usuarioOptional.get());

		}
	}
	
	@GetMapping("/usuario/{email}")
	public ResponseEntity<?> obtenerUsuario (@PathVariable String email){
		Optional<Usuario> o = service.findByUsuario(email);
		if(o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(o.get());
	}
	
	@PostMapping("/conCocinero")
	public ResponseEntity<?> crearConCocinero(@Valid @RequestBody Usuario entity, BindingResult result){ //Binding.. -> A través del resultado obtenemos los msj de error, y tiene que ir justo dsp del request body 
		
		if(result.hasErrors()) {
			return this.validar(result);
		}		
		
		entity.setClave(passwordEncoder.encode(entity.getClave()));
		
		Usuario entityDB = service.save(entity);
		
		service.findConfiguracionAndUpdate();
		
		return ResponseEntity.status(HttpStatus.CREATED).body(entityDB);
	}
	
	@PostMapping("/pass")
	public ResponseEntity<?> crearConPassEncriptada(@Valid @RequestBody Usuario entity, BindingResult result){ //Binding.. -> A través del resultado obtenemos los msj de error, y tiene que ir justo dsp del request body 
		
		if(result.hasErrors()) {
			return this.validar(result);
		}		
		
		entity.setClave(passwordEncoder.encode(entity.getClave()));
		
		Usuario entityDB = service.save(entity);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(entityDB);
	}
}
