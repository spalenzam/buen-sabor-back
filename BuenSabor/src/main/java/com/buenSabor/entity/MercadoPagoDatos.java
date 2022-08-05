package com.buenSabor.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name="mercado_pago_datos")
public class MercadoPagoDatos {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long identificadorPago;
	
	//@FutureOrPresent
	@Column(name= "fecha_aprobacion")
	private Date fechaAprobacion;
	
	@NotEmpty
	@Column(name= "forma_pago")
	private String formaPago;
	
	@NotEmpty
	@Column(name= "metodo_pago")
	private String metodoPago;
	
	@Column(name= "nro_tarjeta")
	private String nroTarjeta;
	
	@NotEmpty
	@Column(name= "estado")
	private String estado;
	
	@Column(name="fecha_creacion") 
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaCreacion;
	//Método que antes de persistir en la DB, asígna la fecha
	@PrePersist
	public void prePersist() {
		this.fechaCreacion = new Date();
	}

	//Getter and Setter
	public Long getIdentificadorPago() {
		return identificadorPago;
	}
	public void setIdentificadorPago(Long identificadorPago) {
		this.identificadorPago = identificadorPago;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public Date getFechaAprobacion() {
		return fechaAprobacion;
	}
	public void setFechaAprobacion(Date fechaAprobacion) {
		this.fechaAprobacion = fechaAprobacion;
	}
	public String getFormaPago() {
		return formaPago;
	}
	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}
	public String getMetodoPago() {
		return metodoPago;
	}
	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}
	public String getNroTarjeta() {
		return nroTarjeta;
	}
	public void setNroTarjeta(String nroTarjeta) {
		this.nroTarjeta = nroTarjeta;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
}
