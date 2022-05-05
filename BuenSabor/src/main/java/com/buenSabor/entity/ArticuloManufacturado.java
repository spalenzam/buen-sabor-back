package com.buenSabor.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "articuloManufacturado")
public class ArticuloManufacturado {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Min(1)
	@NotNull
	@Column(name="tiempoEstimadoCocina")
	private int tiempoEstimadoCocina;
	
	@NotEmpty
	@Column(name = "denominacion")
	private String denominacion;
	
	@Min(1)
	@NotNull
	@Column(name = "precioVenta")
	private double precioVenta;
	
	@Column(name = "fecha_baja")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaBaja;
	
	//VER EL TIPO DE DATO DE IMAGEN MAGNI PONE STRING
	//LOB campo de objeto pesado 
	@Lob
	@JsonIgnore
	private byte [] imagen;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "fk_rubro_general")
	private RubroGeneral rubrogeneral;
	
	@JsonIgnoreProperties(value = {"articulomanufacturado"}, allowSetters = true)
	@OneToMany(mappedBy = "articulomanufacturado", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ArticuloManufacturadoDetalle> articulomanufacturadodetalles;

	public ArticuloManufacturado() {
		this.articulomanufacturadodetalles = new ArrayList<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getTiempoEstimadoCocina() {
		return tiempoEstimadoCocina;
	}

	public void setTiempoEstimadoCocina(int tiempoEstimadoCocina) {
		this.tiempoEstimadoCocina = tiempoEstimadoCocina;
	}

	public String getDenominacion() {
		return denominacion;
	}

	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

	public double getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(double precioVenta) {
		this.precioVenta = precioVenta;
	}

	public byte[] getImagen() {
		return imagen;
	}

	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}
	
	public RubroGeneral getRubrogeneral() {
		return rubrogeneral;
	}

	public void setRubrogeneral(RubroGeneral rubrogeneral) {
		this.rubrogeneral = rubrogeneral;
	}

	public List<ArticuloManufacturadoDetalle> getArticulomanufacturadodetalles() {
		return articulomanufacturadodetalles;
	}

	public void setArticulomanufacturadodetalle(List<ArticuloManufacturadoDetalle> articulomanufacturadodetalles) {
		this.articulomanufacturadodetalles.clear();
		articulomanufacturadodetalles.forEach(this::addArticulomanufacturadodetalle);;
	}
	
	public Date getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	//agregar art manf detalle + establecer relacion inversa
	public void addArticulomanufacturadodetalle(ArticuloManufacturadoDetalle articulomanufacturadodetalle) {
		this.articulomanufacturadodetalles.add(articulomanufacturadodetalle);
		articulomanufacturadodetalle.setArticulomanufacturado(this);
	}
	
	//eliminar art manf detalle + establecer relacion inversa
		public void removeArticulomanufacturadodetalle(ArticuloManufacturadoDetalle articulomanufacturadodetalle) {
			this.articulomanufacturadodetalles.remove(articulomanufacturadodetalle);
			articulomanufacturadodetalle.setArticulomanufacturado(null);
		}
		
	public Integer getImagenHashCode() {
		return (this.imagen != null) ? this.imagen.hashCode() : null;
	}
}
