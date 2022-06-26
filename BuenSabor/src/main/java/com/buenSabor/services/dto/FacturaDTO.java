package com.buenSabor.services.dto;

import java.util.List;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class FacturaDTO {
	
	//Cliente
	private String nombre;
	
	private String apellido;
	
	private String calle;

	private String numeroCalle;

	private String localidad;
	
	
	//Factura
	private String fechaFactura;
	
	private String numeroFactura;
	
	private String montoDescuento;  

	private String formaPago;		

	private String nroTarjeta;

	private String totalVenta;

	private String totalCosto;

	
	//Detalles
	private List<DetalleFacturaDTO> detallesList;

	private JRBeanCollectionDataSource detallesListJRBean;

	
	
	
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public String getNumeroCalle() {
		return numeroCalle;
	}

	public void setNumeroCalle(String numeroCalle) {
		this.numeroCalle = numeroCalle;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public String getFechaFactura() {
		return fechaFactura;
	}

	public void setFechaFactura(String fechaFactura) {
		this.fechaFactura = fechaFactura;
	}

	public String getNumeroFactura() {
		return numeroFactura;
	}

	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	public String getMontoDescuento() {
		return montoDescuento;
	}

	public void setMontoDescuento(String montoDescuento) {
		this.montoDescuento = montoDescuento;
	}

	public String getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}

	public String getNroTarjeta() {
		return nroTarjeta;
	}

	public void setNroTarjeta(String nroTarjeta) {
		this.nroTarjeta = nroTarjeta;
	}

	public String getTotalVenta() {
		return totalVenta;
	}

	public void setTotalVenta(String totalVenta) {
		this.totalVenta = totalVenta;
	}

	public String getTotalCosto() {
		return totalCosto;
	}

	public void setTotalCosto(String totalCosto) {
		this.totalCosto = totalCosto;
	}

	public List<DetalleFacturaDTO> getDetallesList() {
		return detallesList;
	}

	public void setDetallesList(List<DetalleFacturaDTO> detallesList) {
		this.detallesList = detallesList;
		this.detallesListJRBean = new JRBeanCollectionDataSource(detallesList);
	}

	public JRBeanCollectionDataSource getDetallesListJRBean() {
		return detallesListJRBean;
	}

	public void setDetallesListJRBean(JRBeanCollectionDataSource detallesListJRBean) {
		this.detallesListJRBean = detallesListJRBean;
	}
	

	


}
