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

import com.buenSabor.entity.RubroArticulo;
import com.buenSabor.services.RubroArticuloService;
import com.commons.controllers.CommonController;

@RestController
@RequestMapping(path = "api/buensabor/rubroarticulo")
public class RubroArticuloController extends CommonController<RubroArticulo, RubroArticuloService> {

	@PutMapping("/{id}")
	public ResponseEntity<?> editar (@Valid @RequestBody RubroArticulo rubroarticulo, BindingResult result, @PathVariable Long id){
		
		if(result.hasErrors()) {
			return this.validar(result);
		}	
		
		Optional<RubroArticulo> o = service.findById(id);
		if(o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		RubroArticulo rubroarticuloDB = o.get();
		rubroarticuloDB.setDenominacion(rubroarticulo.getDenominacion());
		if(rubroarticulo.getRubroarticuloPadre() != null) {
			rubroarticuloDB.setRubroarticuloPadre(rubroarticulo.getRubroarticuloPadre());
		}
		
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(rubroarticuloDB));
	}
	
	@GetMapping("/alta")
	public ResponseEntity<?> listarAlta(){
		return ResponseEntity.ok().body(service.findAllRubroArticuloAlta());
	}
	
	@PutMapping("/dar-de-baja/{id}")
	public ResponseEntity<?> darDeBaja(@PathVariable Long id){
		//service.deleteByIdAndBaja(id);
		return ResponseEntity.status(HttpStatus.CREATED).body(service.deleteByIdAndBaja(id));
	}
}
