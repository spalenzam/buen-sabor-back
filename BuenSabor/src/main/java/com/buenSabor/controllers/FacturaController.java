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

import com.buenSabor.entity.Factura;
import com.buenSabor.services.FacturaService;
import com.commons.controllers.CommonController;

@RestController
@RequestMapping(path="api/buensabor/facturas")
public class FacturaController extends CommonController<Factura, FacturaService>{

	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@Valid @RequestBody Factura factura, BindingResult result, @PathVariable Long id){
		
		if(result.hasErrors()) {
			return this.validar(result);
		}	
		
		Optional <Factura> o = this.service.findById(id);
		if(!o.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		//Una vez que encontramos el id reemplazamos el Pedido
		Factura facturaDB = o.get();
		facturaDB.setMontoDescuento(factura.getMontoDescuento()); //ESTAS DOS COSAS SE PUEDEN MODIFICAR?
		facturaDB.setNroTarjeta(factura.getNroTarjeta());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(facturaDB));
		
	}
	
	@GetMapping("/buscar/{numFactura}")
	public ResponseEntity<?> buscar(@PathVariable Integer numFactura){
		return ResponseEntity.ok(service.findByNumeroFactura(numFactura));
	}
}
