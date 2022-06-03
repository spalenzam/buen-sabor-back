package com.buenSabor.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buenSabor.entity.DetalleFactura;
import com.buenSabor.entity.MercadoPagoDatos;
import com.buenSabor.services.MercadoPagoDatosService;
import com.buenSabor.services.MercadoPagoDatosServiceImpl;
import com.commons.controllers.CommonController;


import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;

@RestController
@RequestMapping(path = "api/buensabor/mercadoPagoDatos")
public class MercadoPagoDatosController extends CommonController<MercadoPagoDatos, MercadoPagoDatosService> {

	@Autowired
	protected MercadoPagoDatosServiceImpl mercadoPagoDatosService;
	
	@PostMapping("/payment")
	public ResponseEntity<?> processPayment(@RequestBody DetalleFactura detfactura) throws MPException, MPApiException {
		MercadoPagoConfig.setAccessToken("TEST-5308942090062149-050414-517f77a9e02222eef1cd89f17966b93f-447851281");
		
		// Crea un objeto de preferencia
		PreferenceClient client = new PreferenceClient();
		
		List<PreferenceItemRequest> items = new ArrayList<>();
		
			PreferenceItemRequest item =
			   PreferenceItemRequest.builder()
			       .title(detfactura.getArtmanufacturado().getDenominacion())
			       .quantity(detfactura.getCantidad())
			       .unitPrice(new BigDecimal(detfactura.getArtmanufacturado().getPrecioVenta()))
			       .build();
			items.add(item);
		
		PreferenceRequest request = PreferenceRequest.builder().items(items).build();
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(client.create(request));
	} 
}