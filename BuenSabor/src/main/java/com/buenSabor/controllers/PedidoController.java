package com.buenSabor.controllers;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.buenSabor.entity.Pedido;
import com.buenSabor.services.PedidoService;
import com.buenSabor.services.dto.IngresosDiarioYMensualDTO;
import com.buenSabor.services.dto.PedidosPorClienteDTO;
import com.buenSabor.services.dto.RakingComidasDTO;
import com.commons.controllers.CommonController;

@RestController
@RequestMapping(path="api/buensabor/pedidos")
public class PedidoController extends CommonController<Pedido, PedidoService>{
		
	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@Valid @RequestBody Pedido pedido, BindingResult result, @PathVariable Long id){
		
		if(result.hasErrors()) {
			return this.validar(result);
		}	
		
		Optional<Pedido> o = service.findById(id);
		if(o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		//Una vez que encontramos el id reemplazamos el Pedido
		Pedido pedidoDB = o.get();
		pedidoDB.setNumeroPedido(pedido.getNumeroPedido()); 
		pedidoDB.setTipoEnvioPedido(pedido.getTipoEnvioPedido());
		pedidoDB.setEstado(pedido.getEstado());
		pedidoDB.setEstadoInterno(pedido.getEstadoInterno());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(pedidoDB));		
	}

	@GetMapping("/alta")
	public ResponseEntity<?> listarAlta(){
		return ResponseEntity.ok().body(service.findAllPedidosAlta());
	}
	
	@PutMapping("/dar-de-baja/{id}")
	public ResponseEntity<?> darDeBaja(@PathVariable Long id){
		//service.deleteByIdAndBaja(id);
		return ResponseEntity.status(HttpStatus.CREATED).body(service.deleteByIdAndBaja(id));
	}
	
	@GetMapping("/estados")
	public ResponseEntity<?> listaEstado(){
		List estadosList = service.getAllEstados();
		return ResponseEntity.ok().body(estadosList);
	}
	
	@GetMapping("/estados-internos")
	public ResponseEntity<?> listaEstadosInternos(){
		List estadosList = service.getAllEstadosInternos();
		return ResponseEntity.ok().body(estadosList);
	}
	
	@PutMapping("/cambiar-estados/{id}")
	public ResponseEntity<?> editarEstados(@Valid @RequestBody Pedido pedido, BindingResult result, @PathVariable Long id){	
		
		Optional<Pedido> o = service.findById(id);
		if(o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		//Una vez que encontramos el id reemplazamos el Pedido
		Pedido pedidoDB = o.get();
		pedidoDB.setEstado(pedido.getEstado() != null ? pedido.getEstado() : pedidoDB.getEstado());
		pedidoDB.setEstadoInterno(pedido.getEstadoInterno() != null ? pedido.getEstadoInterno() : pedidoDB.getEstadoInterno());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(pedidoDB));		
	}
	
	@GetMapping("/pedidos-por-cliente")
	public ResponseEntity<?> listarPedidosPorCliente(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date desde, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date hasta) {
		
		//System.out.println(desde +""+ hasta);
		List<PedidosPorClienteDTO> listaPedidos = service.listarPedidosPorCliente(desde, hasta);
		
		return ResponseEntity.ok().body(listaPedidos);
	}
	
	@GetMapping("/pedidos/generar-excel")
    public ResponseEntity<Resource> pedidosPorClienteExcel(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date desde, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date hasta) throws Exception {
        
		List<PedidosPorClienteDTO> listaPedidos = service.listarPedidosPorCliente(desde, hasta);

        String filename = "Pedidos-por-cliente-" + LocalDate.now() + ".xlsx";

        InputStreamResource file = new InputStreamResource(service.generarExcelPedidosPorCliente(listaPedidos, desde, hasta));

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
            .body(file);
    }
	
	@GetMapping("/ultimoPedido")
	public ResponseEntity<?> ultimoPedido(){
		return ResponseEntity.ok().body(service.ultimoPedido());
	}

}
