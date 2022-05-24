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

import com.buenSabor.entity.MercadoPagoDatos;
import com.buenSabor.services.MercadoPagoDatosService;
import com.commons.controllers.CommonController;

@RestController
@RequestMapping(path="api/buensabor/mercadoPagoDatos")
public class MercadoPagoDatosController extends CommonController<MercadoPagoDatos, MercadoPagoDatosService>{
	
	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@Valid @RequestBody MercadoPagoDatos mercadoPagoDatos, BindingResult result, @PathVariable Long identificadorPago){
		
		if(result.hasErrors()) {
			return this.validar(result);
		}	
		
		Optional <MercadoPagoDatos> o = service.findById(identificadorPago);
		if(o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		//Una vez que encontramos el id reemplazamos el MercadoPagoDatos
		MercadoPagoDatos mercadoPagoDatosDB = o.get();
		mercadoPagoDatosDB.setFormaPago(mercadoPagoDatos.getFormaPago());
		mercadoPagoDatosDB.setMetodoPago(mercadoPagoDatos.getMetodoPago());
		mercadoPagoDatosDB.setEstado(mercadoPagoDatos.getEstado());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(mercadoPagoDatosDB));		
	}//El token tendría que pasarlo en los argumentos
	/*@PostMapping("/efectuarpago/{factura}")
	public ResponseEntity<?> realizarPago(@RequestBody Factura factura) throws MPException {
		MercadoPago.SDK.setAccessToken("TEST-3502556041132733-050214-ef47a9e5aa971c2965bc747986c19440-187659340");
		Preference preference = new Preference();
		Item item = new Item();
		for (DetalleFactura detalle : factura.getDetallesFacturas()) {
			if(detalle.getArticuloInsumo() == null) {
				item.setTitle(detalle.getArticuloMfact().getDenominacion())
				.setQuantity(detalle.getCantidad())
				.setUnitPrice((float)detalle.getArticuloMfact().getPrecioVenta())
				;
				preference.appendItem(item);
			} else {
				item.setTitle(detalle.getArticuloInsumo().getDenominacion())
				.setQuantity(detalle.getCantidad())
				.setUnitPrice((float) detalle.getArticuloInsumo().getPrecioVenta());
				preference.appendItem(item);
			}
			
		}
		preference.save();
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(preference);
	}*/

}