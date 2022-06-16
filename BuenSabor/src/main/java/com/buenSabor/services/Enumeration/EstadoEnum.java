package com.buenSabor.services.Enumeration;

public enum EstadoEnum {
	
	PENDIENTE("1", "Pendiente"),
    PAGADO("2", "Pagado"),
    TERMINADO("3", "Terminado"),
    DELIVERY("4", "Delivery"),
    FACTURADO("5", "Facturado");
	
	private String codigo;
    private String estado;


    private EstadoEnum(String i, String e) {
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
