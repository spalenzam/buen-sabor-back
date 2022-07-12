package com.buenSabor.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.buenSabor.entity.DetallePedido;
import com.buenSabor.services.DetallePedidoService;
import com.buenSabor.services.dto.RakingComidasDTO;
import com.commons.controllers.CommonController;

@RestController
@RequestMapping(path = "api/buensabor/detallepedido")
public class DetallePedidoController extends CommonController<DetallePedido, DetallePedidoService>{

	@PutMapping("/{id}")
	public ResponseEntity<?> editar (@Valid @RequestBody DetallePedido detallepedido, BindingResult result, @PathVariable Long id){
		
		if(result.hasErrors()) {
			return this.validar(result);
		}			
		
		Optional<DetallePedido> o = service.findById(id);
		if(o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		DetallePedido detallepedidoDB = o.get();
		detallepedidoDB.setCantidad(detallepedido.getCantidad());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(detallepedidoDB));
	}
	
	@GetMapping("/ranking")
	public ResponseEntity<?> listarRanking(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date desde, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date hasta) {
		
		//System.out.println(desde +""+ hasta);
		List<RakingComidasDTO> listaComidasDTOs = service.rankingDeComidas(desde, hasta);
		
		return ResponseEntity.ok().body(listaComidasDTOs);
	}
}
