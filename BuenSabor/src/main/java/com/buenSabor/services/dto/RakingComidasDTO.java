package com.buenSabor.services.dto;

public class RakingComidasDTO {
	
		private String denominacion;
	
		private Double canti;
		
		private Double cantidadTotal;
		
		private Long manufacturadoId;

		
		
		
		public RakingComidasDTO() {
			super();
		}

		public RakingComidasDTO(Double canti, Double cantidadTotal, Long manufacturadoId, String denominacion) {
			super();
			this.canti = canti;
			this.cantidadTotal = cantidadTotal;
			this.manufacturadoId = manufacturadoId;
			this.denominacion = denominacion;
		}

		public Double getCanti() {
			return canti;
		}

		public void setCanti(Double canti) {
			this.canti = canti;
		}

		public Double getCantidadTotal() {
			return cantidadTotal;
		}

		public void setCantidadTotal(Double cantidadTotal) {
			this.cantidadTotal = cantidadTotal;
		}

		public Long getManufacturadoId() {
			return manufacturadoId;
		}

		public void setManufacturadoId(Long manufacturadoId) {
			this.manufacturadoId = manufacturadoId;
		}

		public String getDenominacion() {
			return denominacion;
		}

		public void setDenominacion(String denominacion) {
			this.denominacion = denominacion;
		}

		

}
