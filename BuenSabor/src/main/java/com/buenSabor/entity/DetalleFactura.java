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
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "detalle_factura")
public class DetalleFactura {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Min(1)
	@NotNull
	@Column(name = "cantidad")
	private int cantidad;
	
	@Min(1)
	@NotNull
	@Column(name = "subtotal")
	private Double subtotal;
	
	@ManyToOne()
	@JoinColumn(name = "fk_articulo_manufacturado")
	private ArticuloManufacturado articulomanufacturado;
	
	@ManyToOne()
	@JoinColumn(name = "fk_articulo_insumo")
	private ArticuloInsumo articuloinsumo;

	@JsonIgnoreProperties(value = {"detallefacturas"})
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_factura")
	private Factura factura;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	public ArticuloManufacturado getArtmanufacturado() {
		return articulomanufacturado;
	}

	public void setArtmanufacturado(ArticuloManufacturado artmanufacturado) {
		this.articulomanufacturado = artmanufacturado;
	}

	public ArticuloInsumo getArtinsumo() {
		return articuloinsumo;
	}

	public void setArtinsumo(ArticuloInsumo artinsumo) {
		this.articuloinsumo = artinsumo;
	}

	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(!(obj instanceof DetalleFactura)) {
			return false;
		}
		
		DetalleFactura a = (DetalleFactura) obj;
		return this.id != null && this.id.equals(a.getId());
	}
	
	
}
