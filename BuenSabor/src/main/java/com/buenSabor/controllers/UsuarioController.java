package com.buenSabor.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buenSabor.entity.Usuario;
import com.buenSabor.services.UsuarioService;
import com.commons.controllers.CommonController;

@RestController
@RequestMapping(path="api/buensabor/usuarios")
public class UsuarioController extends CommonController<Usuario, UsuarioService> {

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
		usuarioDB.setClave(usuario.getClave()); //se modifica la contraseña
		
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
}
