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
import com.buenSabor.services.ArticuloManufacturadoService;
import com.commons.controllers.CommonController;

@RestController
@RequestMapping(path="api/buensabor/articulosmanufacturados")
public class ArticuloManufacturadoController extends CommonController<ArticuloManufacturado, ArticuloManufacturadoService>{
	
	@GetMapping("/uploads/img/{id}")
	public ResponseEntity<?> verImagen(@PathVariable Long id){
		
		Optional<ArticuloManufacturado> o = service.findById(id);
		if(o.isEmpty() || o.get().getImagen() == null ){
			return ResponseEntity.notFound().build();
		}
		
		Resource imagen = new ByteArrayResource(o.get().getImagen());
		
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imagen);
		
	}
	
	@PostMapping("/crear-con-imagen")
	public ResponseEntity<?> crearConImagen(@Valid ArticuloManufacturado articuloManufacturado, BindingResult result, @RequestParam MultipartFile archivo) throws IOException {
		
		if(!archivo.isEmpty()){
			articuloManufacturado.setImagen(archivo.getBytes());
		}
		
		return super.crear(articuloManufacturado, result);
	}
	
	@PutMapping("/editar-con-imagen/{id}")
	public ResponseEntity<?> editarConImagen (@Valid ArticuloManufacturado artmanufacturado, BindingResult result, @PathVariable Long id, @RequestParam MultipartFile archivo) throws IOException{
		
		if(result.hasErrors()) {
			return this.validar(result);
		}		
		 		
		Optional<ArticuloManufacturado> o = service.findById(id);
		if(o.isEmpty()){
			return ResponseEntity.notFound().build();
		}
		
		ArticuloManufacturado artmanufacturadoDB = o.get();
		artmanufacturadoDB.setPrecioVenta(artmanufacturado.getPrecioVenta());
		if(!archivo.isEmpty()){
			artmanufacturadoDB.setImagen(archivo.getBytes());
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(artmanufacturadoDB));
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> editar (@Valid @RequestBody ArticuloManufacturado artmanufacturado, BindingResult result, @PathVariable Long id){
		
		if(result.hasErrors()) {
			return this.validar(result);
		}		
		 		
		Optional<ArticuloManufacturado> o = service.findById(id);
		if(o.isEmpty()){
			return ResponseEntity.notFound().build();
		}
		
		ArticuloManufacturado artmanufacturadoDB = o.get();
		artmanufacturadoDB.setPrecioVenta(artmanufacturado.getPrecioVenta());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(artmanufacturadoDB));
	}
	
	@GetMapping("/alta")
	public ResponseEntity<?> listarAlta(){
		return ResponseEntity.ok().body(service.findAllArticulosManufacturadosAlta());
	}
	
	@PutMapping("/dar-de-baja/{id}")
	public ResponseEntity<?> darDeBaja(@PathVariable Long id){
		//service.deleteByIdAndBaja(id);
		return ResponseEntity.status(HttpStatus.CREATED).body(service.deleteByIdAndBaja(id));
	}
	
	@GetMapping("/cantidad-disponible")
	public ResponseEntity<?> findCantidadDisponible(){
		return ResponseEntity.ok().body(service.findCantidadDisponible());
	}
}
