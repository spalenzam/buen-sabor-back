package com.buenSabor.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Table(name="configuracion")
public class Configuracion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Min(1)
	@NotNull
	@Column(name = "cantidad_cocineros")
	private int cantidadCocineros;
	
	@NotEmpty
	@Email
	@Column(name = "email_empresa")
	private String emailEmpresa;
	
	@NotEmpty
	@Column(name = "token_mercado_pago")
	private String tokenMercadoPago;

	public int getCantidadCocineros() {
		return cantidadCocineros;
	}

	public void setCantidadCocineros(int cantidadCocineros) {
		this.cantidadCocineros = cantidadCocineros;
	}

	public String getEmailEmpresa() {
		return emailEmpresa;
	}

	public void setEmailEmpresa(String emailEmpresa) {
		this.emailEmpresa = emailEmpresa;
	}

	public String getTokenMercadoPago() {
		return tokenMercadoPago;
	}

	public void setTokenMercadoPago(String tokenMercadoPago) {
		this.tokenMercadoPago = tokenMercadoPago;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}
