package com.buenSabor.services.Enumeration;

public enum EstadoInternoEnum {
	
	CAJERO("1", "Cajero"),
    COCINA("2", "Cocina"),
    DELIVERY("3", "Delivery");
	
	private String codigo;
    private String estado;


    private EstadoInternoEnum(String i, String e) {
        this.codigo= i;
        this.estado = e;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getEstado() {
        return estado;
    }
}
