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
@Table(name = "detalle_pedido")
public class DetallePedido {

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
	private double subtotal;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "fk_articulo_manufacturado")
	private ArticuloManufacturado articulomanufacturado;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "fk_articulo_insumo")
	private ArticuloInsumo articuloinsumo;
	
	@JsonIgnoreProperties (value = {"detallepedidos"})
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinColumn(name = "fk_pedido")
	private Pedido pedido;

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

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public ArticuloManufacturado getArticulomanufacturado() {
		return articulomanufacturado;
	}

	public void setArticulomanufacturado(ArticuloManufacturado articulomanufacturado) {
		this.articulomanufacturado = articulomanufacturado;
	}

	public ArticuloInsumo getArticuloinsumo() {
		return articuloinsumo;
	}

	public void setArticuloinsumo(ArticuloInsumo articuloinsumo) {
		this.articuloinsumo = articuloinsumo;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(!(obj instanceof DetallePedido)) {
			return false;
		}
		
		DetallePedido a = (DetallePedido) obj;
		return this.id != null && this.id.equals(a.getId());
	}
	
	
}
