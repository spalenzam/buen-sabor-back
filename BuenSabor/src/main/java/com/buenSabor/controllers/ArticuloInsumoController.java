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

import com.buenSabor.entity.ArticuloInsumo;
import com.buenSabor.entity.RubroArticulo;
import com.buenSabor.services.ArticuloInsumoService;
import com.buenSabor.services.RubroArticuloService;
import com.buenSabor.services.errors.BuenSaborException;
import com.buenSabor.services.errors.ErrorConstants;
import com.commons.controllers.CommonController;

@RestController
@RequestMapping(path = "api/buensabor/articuloinsumo")
public class ArticuloInsumoController extends CommonController<ArticuloInsumo, ArticuloInsumoService>{
	
	private final RubroArticuloService rubroArticuloService = null;

	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@Valid @RequestBody ArticuloInsumo articuloinsumo, BindingResult result, @PathVariable Long id){
		
		if(result.hasErrors()) {
			return this.validar(result);
		}	
		Optional<ArticuloInsumo> o = service.findById(id);
		if(o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		ArticuloInsumo articuloinsumoDB = o.get();
		articuloinsumoDB.setDenominacion(articuloinsumo.getDenominacion());
		articuloinsumoDB.setEsInsumo(articuloinsumo.isEsInsumo());
		articuloinsumoDB.setPrecioCompra(articuloinsumo.getPrecioCompra());
		articuloinsumoDB.setPrecioVenta(articuloinsumo.getPrecioVenta());
		articuloinsumoDB.setStockActual(articuloinsumo.getStockActual());
		articuloinsumoDB.setStockMinimo(articuloinsumo.getStockMinimo());
		articuloinsumoDB.setUnidadMedida(articuloinsumo.getUnidadMedida());
		articuloinsumoDB.setRubroarticulo(articuloinsumo.getRubroarticulo());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(articuloinsumoDB));
	}
	
	@GetMapping("/alta")
	public ResponseEntity<?> listarAlta(){
		return ResponseEntity.ok().body(service.findAllArticulosAlta());
	}
	
	@PutMapping("/dar-de-baja/{id}")
	public ResponseEntity<?> darDeBaja(@PathVariable Long id){
		//service.deleteByIdAndBaja(id);
		return ResponseEntity.status(HttpStatus.CREATED).body(service.deleteByIdAndBaja(id));
	}
	
	@GetMapping("/uploads/img/{id}")
	public ResponseEntity<?> verImagen(@PathVariable Long id) {

		Optional<ArticuloInsumo> o = service.findById(id);
		if (o.isEmpty() || o.get().getImagen() == null) {
			return ResponseEntity.notFound().build();
		}

		Resource imagen = new ByteArrayResource(o.get().getImagen());

		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imagen);

	}

	@PostMapping("/crear-con-imagen")
	public ResponseEntity<?> crearConImagen(@Valid ArticuloInsumo articuloInsumo, BindingResult result,
			@RequestParam("archivo") MultipartFile archivo) throws IOException {

		if (!archivo.isEmpty()) {
			articuloInsumo.setImagen(archivo.getBytes());
		}

		Optional<RubroArticulo> rubroArticuloOptional = rubroArticuloService
				.findById(articuloInsumo.getRubroarticulo().getId());
		if (rubroArticuloOptional.isPresent())
			articuloInsumo.setRubroarticulo(rubroArticuloOptional.get());
		else
			throw new BuenSaborException("No se encontró el rubro articulo ingresado", ErrorConstants.ERR_BUSCAR);

		if (result.hasErrors()) {
			return this.validar(result);
		}

		ArticuloInsumo articuloInsumoEntity = service.save(articuloInsumo);

		return ResponseEntity.status(HttpStatus.CREATED).body(articuloInsumoEntity);
	}

	@PutMapping("/editar-con-imagen/{id}")
	public ResponseEntity<?> editarConImagen(@Valid ArticuloInsumo articuloinsumo, BindingResult result,
			@PathVariable Long id, @RequestParam("archivo") MultipartFile archivo) throws IOException {

		if (result.hasErrors()) {
			return this.validar(result);
		}

		// Busco el producto
		Optional<ArticuloInsumo> o = service.findById(id);
		if (o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		// Le actualizo los datos
		ArticuloInsumo articuloinsumoDB = o.get();
		articuloinsumoDB.setDenominacion(articuloinsumo.getDenominacion());
		articuloinsumoDB.setEsInsumo(articuloinsumo.isEsInsumo());
		articuloinsumoDB.setPrecioCompra(articuloinsumo.getPrecioCompra());
		articuloinsumoDB.setPrecioVenta(articuloinsumo.getPrecioVenta());
		articuloinsumoDB.setStockActual(articuloinsumo.getStockActual());
		articuloinsumoDB.setStockMinimo(articuloinsumo.getStockMinimo());
		articuloinsumoDB.setUnidadMedida(articuloinsumo.getUnidadMedida());
		articuloinsumoDB.setRubroarticulo(articuloinsumo.getRubroarticulo());

		// Seteo la imagen
		if (!archivo.isEmpty()) {
			articuloinsumoDB.setImagen(archivo.getBytes());
		}

		// Seteo el rubro
		/*Optional<RubroGeneral> rubroGeneralOptional = rubroGeneralService
				.findById(artmanufacturado.getRubrogeneral().getId());
		if (rubroGeneralOptional.isPresent())
			artmanufacturadoDB.setRubrogeneral(rubroGeneralOptional.get());
		else
			throw new BuenSaborException("No se encontró el rubro general ingresado", ErrorConstants.ERR_BUSCAR);
*/
		// Lo guardo
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(articuloinsumoDB));
	}
	
	@GetMapping("/imagen/{id}")
	public ResponseEntity<?> imagenRubro(@PathVariable Long id){
		Resource imagen = null ;
		Iterable<ArticuloInsumo> artInsumos = service.findImagenRubro(id); 
		
		for(ArticuloInsumo artIns : artInsumos) {
			if (artIns.getImagen() != null) {
				imagen = new ByteArrayResource(artIns.getImagen());
				break;
			}
		}

		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imagen);
	}
}
