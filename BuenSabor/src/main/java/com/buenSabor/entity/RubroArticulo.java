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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "rubro_articulo")
public class RubroArticulo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	@Column(name = "denominacion")
	private String denominacion;
	
	@Column(name = "fecha_baja")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaBaja;
	
	//@JsonIgnoreProperties(value= {"rubroarticuloHijos"})
	@ManyToOne()
	private RubroArticulo rubroarticuloPadre;
	
	@JsonIgnoreProperties(value = {"rubroarticuloPadre"}, allowSetters = true)
	@OneToMany(mappedBy = "rubroarticuloPadre", fetch = FetchType.EAGER)
	private List<RubroArticulo> rubroarticuloHijos;
	
	public RubroArticulo() {
		this.rubroarticuloHijos = new ArrayList<>();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDenominacion() {
		return denominacion;
	}

	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

	public RubroArticulo getRubroarticuloPadre() {
		return rubroarticuloPadre;
	}

	public void setRubroarticuloPadre(RubroArticulo rubroarticuloPadre) {
		this.rubroarticuloPadre = rubroarticuloPadre;
	}

	public List<RubroArticulo> getRubroarticuloHijos() {
		return rubroarticuloHijos;
	}

	public void setRubroarticuloHijos(List<RubroArticulo> rubroarticuloHijos) {
		this.rubroarticuloHijos = rubroarticuloHijos;
	}

	public Date getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
	}
	
	

}
