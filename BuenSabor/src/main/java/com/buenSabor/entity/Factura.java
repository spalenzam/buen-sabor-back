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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.FutureOrPresent;

import org.hibernate.validator.constraints.CreditCardNumber;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="facturas")
public class Factura {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name= "fecha_factura")
	private Date fechaFactura;
	
	//@Min(1)
	//@NotNull
	@Column(name= "numero_factura")
	private int numeroFactura;
	
	@Column(name= "monto_descuento")
	private Double montoDescuento;  
	
	@Column(name= "forma_pago")
	private String formaPago;		
	
	@Column(name= "nro_tarjeta")
	private String nroTarjeta;
	
	@Column(name= "total_venta")
	private Double totalVenta;
	
	@Column(name= "total_costo")
	private Double totalCosto;
	
	@OneToOne()
	@JoinColumn(name = "fk_pedido")
	private Pedido pedido;
	
	@Column(name="created_at") //
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	//Método que antes de persistir en la DB, asígna la fecha
	@PrePersist
	public void prePersist() {
		this.createdAt = new Date();
	}
	
	@JsonIgnoreProperties(value = {"factura"}, allowSetters = true)
	@OneToMany(mappedBy = "factura", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DetalleFactura> detallefacturas;
	
	public Factura() {
		this.detallefacturas = new ArrayList<>();
	}
	
	//Getter and Setter
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getFechaFactura() {
		return fechaFactura;
	}
	public void setFechaFactura(Date fechaFactura) {
		this.fechaFactura = fechaFactura;
	}
	public int getNumeroFactura() {
		return numeroFactura;
	}
	public void setNumeroFactura(int numeroFactura) {
		this.numeroFactura = numeroFactura;
	}
	public Double getMontoDescuento() {
		return montoDescuento;
	}
	public void setMontoDescuento(Double montoDescuento) {
		this.montoDescuento = montoDescuento;
	}
	public String getNroTarjeta() {
		return nroTarjeta;
	}
	public void setNroTarjeta(String nroTarjeta) {
		this.nroTarjeta = nroTarjeta;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}
	
	public String getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}

	public Double getTotalVenta() {
		return totalVenta;
	}

	public void setTotalVenta(Double totalVenta) {
		this.totalVenta = totalVenta;
	}

	public Double getTotalCosto() {
		return totalCosto;
	}

	public void setTotalCosto(Double totalCosto) {
		this.totalCosto = totalCosto;
	}

	public List<DetalleFactura> getDetallefacturas() {
		return detallefacturas;
	}
	public void setDetallefacturas(List<DetalleFactura> detallefacturas) {
		this.detallefacturas.clear();
		detallefacturas.forEach(this::addDetallefactura);
	}
	
	public void addDetallefactura(DetalleFactura detallefactura) {
		this.detallefacturas.add(detallefactura);
		detallefactura.setFactura(this);
	}
	
	public void removeDetallefactura(DetalleFactura detallefactura) {
		this.detallefacturas.remove(detallefactura); 
		detallefactura.setFactura(null);
	}
	
	

}
