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

import com.buenSabor.entity.ArticuloInsumo;
import com.buenSabor.services.ArticuloInsumoService;
import com.commons.controllers.CommonController;

@RestController
@RequestMapping(path = "api/buensabor/articuloinsumo")
public class ArticuloInsumoController extends CommonController<ArticuloInsumo, ArticuloInsumoService>{

	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@Valid @RequestBody ArticuloInsumo articuloinsumo, BindingResult result, @PathVariable Long id){
		
		if(result.hasErrors()) {
			return this.validar(result);
		}	
		Optional<ArticuloInsumo> o = service.findById(id);
		if(o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		ArticuloInsumo articuloinsumoDB = o.get();
		articuloinsumoDB.setPrecioCompra(articuloinsumo.getPrecioCompra());
		articuloinsumoDB.setPrecioVenta(articuloinsumo.getPrecioVenta());
		articuloinsumoDB.setStockActual(articuloinsumo.getStockActual());
		articuloinsumoDB.setStockMinimo(articuloinsumo.getStockMinimo());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(articuloinsumoDB));
	}
	
	@GetMapping("/alta")
	public ResponseEntity<?> listarAlta(){
		return ResponseEntity.ok().body(service.findAllArticulosAlta());
	}
	
	@PutMapping("/dar-de-baja/{id}")
	public ResponseEntity<?> darDeBaja(@PathVariable Long id){
		//service.deleteByIdAndBaja(id);
		return ResponseEntity.status(HttpStatus.CREATED).body(service.deleteByIdAndBaja(id));
	}

}
