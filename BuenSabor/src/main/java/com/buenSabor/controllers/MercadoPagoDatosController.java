package com.buenSabor.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buenSabor.entity.MercadoPagoDatos;
import com.buenSabor.services.MercadoPagoDatosService;
import com.buenSabor.services.MercadoPagoDatosServiceImpl;
import com.buenSabor.services.dto.DetallePedidoDTO;
import com.commons.controllers.CommonController;


import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentItemRequest;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePaymentMethodsRequest;
import com.mercadopago.client.preference.PreferencePaymentTypeRequest;
import com.mercadopago.client.preference.PreferenceRequest;

@RestController
@RequestMapping(path = "api/buensabor/mercadoPagoDatos")
public class MercadoPagoDatosController extends CommonController<MercadoPagoDatos, MercadoPagoDatosService> {

	@Autowired
	protected MercadoPagoDatosServiceImpl mercadoPagoDatosService; 
	
	@PostMapping("/preference")
	public ResponseEntity<?> preference(@RequestBody DetallePedidoDTO detpedido) throws MPException, MPApiException {
		MercadoPagoConfig.setAccessToken("TEST-5308942090062149-050414-517f77a9e02222eef1cd89f17966b93f-447851281");
		
		// Crea un objeto de preferencia
		PreferenceClient client = new PreferenceClient();
		
		List<PreferenceItemRequest> items = new ArrayList<>();
			PreferenceItemRequest item =
			   PreferenceItemRequest.builder()
			       .title(detpedido.getDescripcion())
			       .quantity(detpedido.getCantidad())
			       .unitPrice(new BigDecimal(detpedido.getTotalPagar()))
			       .build();
			items.add(item);
		
		PreferenceBackUrlsRequest backUrls = 
				PreferenceBackUrlsRequest.builder()
				.success("http://localhost:3000/compra")
				.failure("http://localhost:3000/compra")
				.pending("http://localhost:3000/compra")
				.build();
		
		
		List<PreferencePaymentTypeRequest> excludedPaymentTypes = new ArrayList<>();
			excludedPaymentTypes.add(PreferencePaymentTypeRequest.builder()
					.id("ticket")
					.build());
		
		PreferencePaymentMethodsRequest paymentMethods =
		   PreferencePaymentMethodsRequest.builder()
		       .installments(3)
		       .excludedPaymentTypes(excludedPaymentTypes)
		       .build();
		
		PreferenceRequest request = 
				PreferenceRequest.builder()
				.items(items)
				.backUrls(backUrls)
				.autoReturn("approved")
				.binaryMode(true)
				.paymentMethods(paymentMethods)
				//.notificationUrl("https://webhook.site/cf43d7eb-b66c-4bc1-9385-342a65202e76")
				//.notificationUrl("https://hookb.in/QJyMDDQmN7T8G218WN2j")
				.build();	
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(client.create(request));
	} 
	
	@PutMapping("/{identificadorPago}")
	public ResponseEntity<?> editar(@Valid @RequestBody MercadoPagoDatos mercadopago, BindingResult result, @PathVariable Long identificadorPago){
		
		if(result.hasErrors()) {
			return this.validar(result);
		}	
		
		Optional<MercadoPagoDatos> o = service.findById(identificadorPago);
		if(o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		MercadoPagoDatos mercadopagoDB = o.get();
		mercadopagoDB.setEstado(mercadopago.getEstado());
		mercadopagoDB.setFechaAprobacion(mercadopago.getFechaAprobacion());
		mercadopagoDB.setFormaPago(mercadopago.getFormaPago());
		mercadopagoDB.setMetodoPago(mercadopago.getMetodoPago());
		mercadopagoDB.setNroTarjeta(mercadopago.getNroTarjeta());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(mercadopagoDB));		
	}
}