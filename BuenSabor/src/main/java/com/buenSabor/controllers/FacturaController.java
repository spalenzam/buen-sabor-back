package com.buenSabor.controllers;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.buenSabor.entity.Factura;
import com.buenSabor.entity.Pedido;
import com.buenSabor.services.FacturaService;
import com.buenSabor.services.PedidoService;
import com.buenSabor.services.dto.IngresosDiarioYMensualDTO;
import com.buenSabor.services.dto.RakingComidasDTO;
import com.buenSabor.services.errors.BuenSaborException;
import com.buenSabor.services.errors.ErrorConstants;
import com.commons.controllers.CommonController;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;

@RestController
@RequestMapping(path = "api/buensabor/facturas")
public class FacturaController extends CommonController<Factura, FacturaService> {

	private final PedidoService pedidoService;

	public FacturaController(PedidoService pedidoService) {
		super();
		this.pedidoService = pedidoService;
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@Valid @RequestBody Factura factura, BindingResult result, @PathVariable Long id) {

		if (result.hasErrors()) {
			return this.validar(result);
		}

		Optional<Factura> o = this.service.findById(id);
		if (!o.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		// Una vez que encontramos el id reemplazamos el Pedido
		Factura facturaDB = o.get();
		facturaDB.setMontoDescuento(factura.getMontoDescuento()); // ESTAS DOS COSAS SE PUEDEN MODIFICAR?
		facturaDB.setNroTarjeta(factura.getNroTarjeta());
		facturaDB.setDetallefacturas(factura.getDetallefacturas());

		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(facturaDB));

	}

	@GetMapping("/buscar/{numFactura}")
	public ResponseEntity<?> buscar(@PathVariable Integer numFactura) {
		return ResponseEntity.ok(service.findByNumeroFactura(numFactura));
	}

	@PostMapping("/crear")
	public ResponseEntity<?> crearFactura(@Valid @RequestBody Factura factura, BindingResult result) { // Binding.. -> A través del resultado obtenemos los msj de error, y tiene que ir justo dsp del request body

		if (result.hasErrors()) {
			return this.validar(result);
		}

		Optional<Pedido> pedidoOptional = pedidoService.findById(factura.getPedido().getId());

		if (pedidoOptional.isPresent())
			factura.setPedido(pedidoOptional.get());
		else
			throw new BuenSaborException("No se encontró el pedido ingresado", ErrorConstants.ERR_BUSCAR);

		Factura facturaEntity = service.save(factura);

		return ResponseEntity.status(HttpStatus.CREATED).body(facturaEntity);
	}

	@GetMapping("/generarPDF/{factura}")
	public ResponseEntity<Resource> generarPDF(@PathVariable Factura factura) throws Exception {
		
		System.out.println("Factura maca "+factura);

		ByteArrayInputStream facturaPDF;

		facturaPDF = service.generarFacturaPDF(factura);

		// Acá lo convierto para que se descargué en formato pdf
		InputStreamResource file = new InputStreamResource(facturaPDF);

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "Maca")
				.contentType(MediaType.parseMediaType("application/pdf")).body(file);

		// Esto lo envía en byte, en el ResponseEntity<Resource> debería quedar así
		// ResponseEntity<InputStreamResource>
		/*
		 * HttpHeaders headers = new HttpHeaders(); headers.add("Content-Disposition",
		 * "inline; filename=" + factura.nombre + ".pdf");
		 * 
		 * return ResponseEntity .ok() .headers(headers)
		 * .contentType(MediaType.APPLICATION_PDF) .body(new
		 * InputStreamResource(facturaPDF));
		 */
	}
	
	@GetMapping("/ingreso-mensual")
	public ResponseEntity<?> ingresoMensual(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fecha) {
		
		System.out.println(fecha);
		
		//System.out.println(desde +""+ hasta);
		IngresosDiarioYMensualDTO ingresoMensual = service.ingresoMensual(fecha);
		
		return ResponseEntity.ok().body(ingresoMensual);
	}
	
	@GetMapping("/ingreso-mensual/generar-excel")
    public ResponseEntity<Resource> ingresoMensualExcel(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fecha) throws Exception {
        
		IngresosDiarioYMensualDTO ingresoMensual = service.ingresoMensual(fecha);

        String filename = "Ingreso-Mensual-" + LocalDate.now() + ".xlsx";

        InputStreamResource file = new InputStreamResource(service.generarExcelIngresoMensual(ingresoMensual, fecha));

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
            .body(file);
    }
	
	@GetMapping("/ingreso-diario")
	public ResponseEntity<?> ingresoDiario(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fecha) {
		
		//System.out.println(desde +""+ hasta);
		IngresosDiarioYMensualDTO ingresoDiario = service.ingresoDiario(fecha);
		
		return ResponseEntity.ok().body(ingresoDiario);
	}
	
	@GetMapping("/ingreso-diario/generar-excel")
    public ResponseEntity<Resource> ingresoDiarioExcel(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fecha) throws Exception {
        
		IngresosDiarioYMensualDTO ingresoDiario = service.ingresoDiario(fecha);

        String filename = "Ingreso-Diario-" + LocalDate.now() + ".xlsx";

        InputStreamResource file = new InputStreamResource(service.generarExcelIngresoDiario(ingresoDiario, fecha));

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
            .body(file);
    }
	
	@GetMapping("/ganancias")
	public ResponseEntity<?> ganancias(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date desde, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date hasta) {
		
		//System.out.println(desde +""+ hasta);
		IngresosDiarioYMensualDTO ingresoDiario = service.gananciasPorFecha(desde, hasta);
		
		return ResponseEntity.ok().body(ingresoDiario);
	}
	
	@GetMapping("/ganancias/generar-excel")
    public ResponseEntity<Resource> gananciasExcel(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date desde, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date hasta) throws Exception {
        
		IngresosDiarioYMensualDTO ingresoDiario = service.gananciasPorFecha(desde, hasta);

        String filename = "Ganancias-" + LocalDate.now() + ".xlsx";

        InputStreamResource file = new InputStreamResource(service.generarExcelGananciasPorFecha(ingresoDiario, desde, hasta));

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
            .body(file);
    }
}
