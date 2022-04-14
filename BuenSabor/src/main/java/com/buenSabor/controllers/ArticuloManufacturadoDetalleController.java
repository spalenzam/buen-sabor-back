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

import com.buenSabor.entity.ArticuloManufacturadoDetalle;
import com.buenSabor.services.ArticuloManufacturadoDetalleService;
import com.commons.controllers.CommonController;

@RestController
@RequestMapping(path = "api/buensabor/artmanufacturadodetalle")
public class ArticuloManufacturadoDetalleController extends CommonController<ArticuloManufacturadoDetalle, ArticuloManufacturadoDetalleService>{

	@PutMapping("/{id}")
	public ResponseEntity<?> editar (@Valid @RequestBody ArticuloManufacturadoDetalle artmanufacturadodetalle, BindingResult result, @PathVariable Long id){
		
		if(result.hasErrors()) {
			return this.validar(result);
		}		
		
		Optional<ArticuloManufacturadoDetalle> o = service.findById(id);
		if(o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		ArticuloManufacturadoDetalle artmanufacturadodetalleDB = o.get();
		artmanufacturadodetalleDB.setCantidad(artmanufacturadodetalle.getCantidad());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(artmanufacturadodetalleDB));
	}
}
