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

import com.buenSabor.entity.ArticuloInsumo;
import com.buenSabor.entity.ArticuloManufacturadoDetalle;
import com.buenSabor.entity.DetallePedido;
import com.buenSabor.services.ArticuloInsumoService;
import com.buenSabor.services.DetallePedidoService;
import com.buenSabor.services.dto.RakingComidasDTO;
import com.commons.controllers.CommonController;

@RestController
@RequestMapping(path = "api/buensabor/detallepedido")
public class DetallePedidoController extends CommonController<DetallePedido, DetallePedidoService>{
	private final ArticuloInsumoService articuloInsumoService;
	
	public DetallePedidoController(ArticuloInsumoService articuloInsumoService) {
		super();
		this.articuloInsumoService = articuloInsumoService;
	}

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

	@PutMapping("/actualizar/{id}")
	public ResponseEntity<?> listarInsumos(@PathVariable Long id){
		
		Iterable<DetallePedido> detallespedidos = service.findDetPedidos(id); 
		
		for(DetallePedido detallepedido : detallespedidos) {
			if(detallepedido.getArticulomanufacturado() != null) {
				
				for(ArticuloManufacturadoDetalle artdetalles : detallepedido.getArticulomanufacturado().getArticulomanufacturadodetalles()) {
						
					Optional<ArticuloInsumo> o = articuloInsumoService.findById(artdetalles.getArticuloinsumo().getId());
					
					ArticuloInsumo artInsumoDB = o.get();
					artInsumoDB.setStockActual(Math.round((artdetalles.getArticuloinsumo().getStockActual() - (artdetalles.getCantidad()*detallepedido.getCantidad()))*100.0)/100.0);
	
					articuloInsumoService.save(artInsumoDB);
				}
			} else {
					Optional<ArticuloInsumo> o = articuloInsumoService.findById(detallepedido.getArticuloinsumo().getId());
					
					ArticuloInsumo artInsumoDB = o.get();
					artInsumoDB.setStockActual(Math.round((detallepedido.getArticuloinsumo().getStockActual() - (detallepedido.getCantidad()))*100.0)/100.0);
	
					articuloInsumoService.save(artInsumoDB);
			}
		}
		return ResponseEntity.ok().body(service.findDetPedidos(id));
	}
	
	
	@GetMapping("/ranking/generar-excel")
    public ResponseEntity<Resource> rankingExcel(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date desde, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date hasta) throws Exception {
        
        List<RakingComidasDTO> listaComidasDTOs = service.rankingDeComidas(desde, hasta);

        String filename = "Ranking-Comidas-" + LocalDate.now() + ".xlsx";

        InputStreamResource file = new InputStreamResource(service.generarExcelRanking(listaComidasDTOs, desde, hasta));

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
            .body(file);
    }


}
