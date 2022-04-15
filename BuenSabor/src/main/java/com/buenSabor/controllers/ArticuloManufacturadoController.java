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

import com.buenSabor.entity.ArticuloManufacturado;
import com.buenSabor.services.ArticuloManufacturadoService;
import com.commons.controllers.CommonController;

@RestController
@RequestMapping(path="api/buensabor/articulosmanufacturados")
public class ArticuloManufacturadoController extends CommonController<ArticuloManufacturado, ArticuloManufacturadoService>{
	
	@PutMapping("/{id}")
	public ResponseEntity<?> editar (@Valid @RequestBody ArticuloManufacturado artmanufacturado, BindingResult result, @PathVariable Long id){
		
		if(result.hasErrors()) {
			return this.validar(result);
		}		
		 		
		Optional<ArticuloManufacturado> o = service.findById(id);
		if(o.isEmpty()){
			return ResponseEntity.notFound().build();
		}
		
		ArticuloManufacturado artmanufacturadoDB = o.get();
		artmanufacturadoDB.setPrecioVenta(artmanufacturado.getPrecioVenta());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(artmanufacturadoDB));
	}
	
	@GetMapping("/alta")
	public ResponseEntity<?> listarAlta(){
		return ResponseEntity.ok().body(service.findAllArticulosManufacturadosAlta());
	}
	
	
}
