package com.buenSabor.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="articulo_manufacturado_detalle")
public class ArticuloManufacturadoDetalle {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Min((long) 0.1)
	@NotNull
	@Column(name = "cantidad")
	private float cantidad;
	
	@NotEmpty
	@Column(name = "unidad_medida")
	private String unidadMedida;
	
	@ManyToOne()
	@JoinColumn(name = "fk_articulo_insumo")
	private ArticuloInsumo articuloinsumo;
	
	//@JsonIgnoreProperties(value = {"articulomanufacturadodetalle"})
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_articulo_manufacturado")
	private ArticuloManufacturado articulomanufacturado;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public float getCantidad() {
		return cantidad;
	}

	public void setCantidad(float cantidad) {
		this.cantidad = cantidad;
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	public ArticuloInsumo getArticuloinsumo() {
		return articuloinsumo;
	}

	public void setArticuloinsumo(ArticuloInsumo articuloinsumo) {
		this.articuloinsumo = articuloinsumo;
	}

	public ArticuloManufacturado getArticulomanufacturado() {
		return articulomanufacturado;
	}

	public void setArticulomanufacturado(ArticuloManufacturado articulomanufacturado) {
		this.articulomanufacturado = articulomanufacturado;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(!(obj instanceof ArticuloManufacturado)) {
			return false;
		}
		
		ArticuloManufacturado a = (ArticuloManufacturado) obj;
		return this.id != null && this.id.equals(a.getId());
	}
	
}
