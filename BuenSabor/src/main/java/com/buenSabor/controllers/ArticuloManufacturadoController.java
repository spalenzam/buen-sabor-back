package com.buenSabor.controllers;

import java.io.IOException;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
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
import org.springframework.web.multipart.MultipartFile;

import com.buenSabor.entity.ArticuloManufacturado;
import com.buenSabor.entity.ArticuloManufacturadoDetalle;
import com.buenSabor.entity.RubroGeneral;
import com.buenSabor.services.ArticuloManufacturadoDetalleService;
import com.buenSabor.services.ArticuloManufacturadoService;
import com.buenSabor.services.RubroGeneralService;
import com.buenSabor.services.errors.BuenSaborException;
import com.buenSabor.services.errors.ErrorConstants;
import com.commons.controllers.CommonController;

@RestController
@RequestMapping(path = "api/buensabor/articulosmanufacturados")
public class ArticuloManufacturadoController
		extends CommonController<ArticuloManufacturado, ArticuloManufacturadoService> {

	private final RubroGeneralService rubroGeneralService;

	private final ArticuloManufacturadoDetalleService articuloManufacturadoDetalleService;

	public ArticuloManufacturadoController(RubroGeneralService rubroGeneralService,
			ArticuloManufacturadoDetalleService articuloManufacturadoDetalleService) {
		super();
		this.rubroGeneralService = rubroGeneralService;
		this.articuloManufacturadoDetalleService = articuloManufacturadoDetalleService;
	}

	@GetMapping("/uploads/img/{id}")
	public ResponseEntity<?> verImagen(@PathVariable Long id) {

		Optional<ArticuloManufacturado> o = service.findById(id);
		if (o.isEmpty() || o.get().getImagen() == null) {
			return ResponseEntity.notFound().build();
		}

		Resource imagen = new ByteArrayResource(o.get().getImagen());

		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imagen);

	}

	@PostMapping("/crear-con-imagen")
	public ResponseEntity<?> crearConImagen(@Valid ArticuloManufacturado articuloManufacturado, BindingResult result,
			@RequestParam("archivo") MultipartFile archivo) throws IOException {

		if (!archivo.isEmpty()) {
			articuloManufacturado.setImagen(archivo.getBytes());
		}

		Optional<RubroGeneral> rubroGeneralOptional = rubroGeneralService
				.findById(articuloManufacturado.getRubrogeneral().getId());
		if (rubroGeneralOptional.isPresent())
			articuloManufacturado.setRubrogeneral(rubroGeneralOptional.get());
		else
			throw new BuenSaborException("No se encontró el rubro general ingresado", ErrorConstants.ERR_BUSCAR);

		if (result.hasErrors()) {
			return this.validar(result);
		}

		ArticuloManufacturado articuloManufacturadoEntity = service.save(articuloManufacturado);

		return ResponseEntity.status(HttpStatus.CREATED).body(articuloManufacturadoEntity);
	}

	@PostMapping("/crear-con-rubro")
	public ResponseEntity<?> crearConRubro(@Valid @RequestBody ArticuloManufacturado articuloManufacturado,
			BindingResult result) throws IOException {
		System.out.println();

		Optional<RubroGeneral> rubroGeneralOptional = rubroGeneralService
				.findById(articuloManufacturado.getRubrogeneral().getId());
		if (rubroGeneralOptional.isPresent())
			articuloManufacturado.setRubrogeneral(rubroGeneralOptional.get());
		else
			throw new BuenSaborException("No se encontró el rubro general ingresado", ErrorConstants.ERR_BUSCAR);

		if (result.hasErrors()) {
			return this.validar(result);
		}

		ArticuloManufacturado articuloManufacturadoEntity = service.save(articuloManufacturado);

		return ResponseEntity.status(HttpStatus.CREATED).body(articuloManufacturadoEntity);
	}

	@PutMapping("/editar-con-imagen/{id}")
	public ResponseEntity<?> editarConImagen(@Valid ArticuloManufacturado artmanufacturado, BindingResult result,
			@PathVariable Long id, @RequestParam("archivo") MultipartFile archivo) throws IOException {

		if (result.hasErrors()) {
			return this.validar(result);
		}

		// Busco el producto
		Optional<ArticuloManufacturado> o = service.findById(id);
		if (o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		/*
		 * ArticuloManufacturado artmanufacturadoDB = o.get();
		 * artmanufacturadoDB.setPrecioVenta(artmanufacturado.getPrecioVenta());
		 * if(!archivo.isEmpty()){ artmanufacturadoDB.setImagen(archivo.getBytes()); }
		 */

		// Le actualizo los datos
		ArticuloManufacturado artmanufacturadoDB = o.get();
		artmanufacturadoDB.setDenominacion(artmanufacturado.getDenominacion());
		artmanufacturadoDB.setPrecioVenta(artmanufacturado.getPrecioVenta());
		artmanufacturadoDB.setTiempoEstimadoCocina(artmanufacturado.getTiempoEstimadoCocina());

		// Seteo la imagen
		if (!archivo.isEmpty()) {
			artmanufacturadoDB.setImagen(archivo.getBytes());
		}

		// Seteo el rubro
		Optional<RubroGeneral> rubroGeneralOptional = rubroGeneralService
				.findById(artmanufacturado.getRubrogeneral().getId());
		if (rubroGeneralOptional.isPresent())
			artmanufacturadoDB.setRubrogeneral(rubroGeneralOptional.get());
		else
			throw new BuenSaborException("No se encontró el rubro general ingresado", ErrorConstants.ERR_BUSCAR);

		// Lo guardo
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(artmanufacturadoDB));
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@Valid @RequestBody ArticuloManufacturado artmanufacturado, BindingResult result,
			@PathVariable Long id) {

		if (result.hasErrors()) {
			return this.validar(result);
		}

		Optional<ArticuloManufacturado> o = service.findById(id);
		if (o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		ArticuloManufacturado artmanufacturadoDB = o.get();
		artmanufacturadoDB.setDenominacion(artmanufacturado.getDenominacion());
		artmanufacturadoDB.setPrecioVenta(artmanufacturado.getPrecioVenta());
		artmanufacturadoDB.setTiempoEstimadoCocina(artmanufacturado.getTiempoEstimadoCocina());

		// Seteo los detalles si es que tiene
		if (artmanufacturado.getArticulomanufacturadodetalles().size() > 0) {
			for (ArticuloManufacturadoDetalle detalle : artmanufacturado.getArticulomanufacturadodetalles()) {

				articuloManufacturadoDetalleService.save(detalle);

				artmanufacturadoDB.addArticulomanufacturadodetalle(detalle);
			}
		}

		Optional<RubroGeneral> rubroGeneralOptional = rubroGeneralService
				.findById(artmanufacturado.getRubrogeneral().getId());
		if (rubroGeneralOptional.isPresent())
			artmanufacturadoDB.setRubrogeneral(rubroGeneralOptional.get());
		else
			throw new BuenSaborException("No se encontró el rubro general ingresado", ErrorConstants.ERR_BUSCAR);

		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(artmanufacturadoDB));
	}

	@GetMapping("/alta")
	public ResponseEntity<?> listarAlta() {
		return ResponseEntity.ok().body(service.findAllArticulosManufacturadosAlta());
	}

	@PutMapping("/dar-de-baja/{id}")
	public ResponseEntity<?> darDeBaja(@PathVariable Long id) {
		// service.deleteByIdAndBaja(id);
		return ResponseEntity.status(HttpStatus.CREATED).body(service.deleteByIdAndBaja(id));
	}

	@GetMapping("/cantidad-disponible")
	public ResponseEntity<?> findCantidadDisponible() {
		return ResponseEntity.ok().body(service.findCantidadDisponible());
	}
	
	@GetMapping("/imagen/{id}")
	public ResponseEntity<?> imagenRubro(@PathVariable Long id){
		Resource imagen = null ;
		Iterable<ArticuloManufacturado> artManufacturados = service.findImagenRubro(id); 
		
		for(ArticuloManufacturado artManuf : artManufacturados) {
			if (artManuf.getImagen() != null) {
				imagen = new ByteArrayResource(artManuf.getImagen());
				break;
			}
		}

		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imagen);
	}
}
