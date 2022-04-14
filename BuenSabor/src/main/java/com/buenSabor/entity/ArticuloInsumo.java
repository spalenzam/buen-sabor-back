package com.buenSabor.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "articulo_insumo")
public class ArticuloInsumo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	@Column(name = "denominacion")
	private String denominación;
	
	@Min(1)
	@NotNull
	@Column(name = "precio_compra")
	private double precioCompra;
	
	@Min(1)
	@NotNull
	@Column(name = "precio_venta")
	private double precioVenta;
	
	@Min(1)
	@NotNull
	@Column(name = "stock_actul")
	private double stockActual;
	
	@Min(1)
	@NotNull
	@Column(name = "stock_minimo")
	private double stockMinimo; 
	
	@NotEmpty
	@Column(name = "unidad_medida")
	private String unidadMedida;
	
	@Column(name = "es_insumo")
	private boolean esInsumo;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "fk_rubro_articulo")
	private RubroArticulo rubroarticulo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDenominación() {
		return denominación;
	}

	public void setDenominación(String denominación) {
		this.denominación = denominación;
	}

	public double getPrecioCompra() {
		return precioCompra;
	}

	public void setPrecioCompra(double precioCompra) {
		this.precioCompra = precioCompra;
	}

	public double getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(double precioVenta) {
		this.precioVenta = precioVenta;
	}

	public double getStockActual() {
		return stockActual;
	}

	public void setStockActual(double stockActual) {
		this.stockActual = stockActual;
	}

	public double getStockMinimo() {
		return stockMinimo;
	}

	public void setStockMinimo(double stockMinimo) {
		this.stockMinimo = stockMinimo;
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	public boolean isEsInsumo() {
		return esInsumo;
	}

	public void setEsInsumo(boolean esInsumo) {
		this.esInsumo = esInsumo;
	}

	public RubroArticulo getRubroarticulo() {
		return rubroarticulo;
	}

	public void setRubroarticulo(RubroArticulo rubroarticulo) {
		this.rubroarticulo = rubroarticulo;
	}

}
