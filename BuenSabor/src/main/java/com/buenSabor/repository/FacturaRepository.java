package com.buenSabor.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.buenSabor.entity.Factura;
import com.buenSabor.entity.Pedido;


public interface FacturaRepository extends CrudRepository<Factura, Long>{
	
	@Query("select f from Factura f where f.numeroFactura =?1")
	Factura findByNumeroFactura(Integer numFactura);
	
	@Query(value = "SELECT * FROM facturas f WHERE month(f.fecha_factura) = ?1 AND year(f.fecha_factura) = ?2", nativeQuery = true)
	public List<Factura> ingresosMensuales(int mes, int anio);
	
	@Query(value = "SELECT * FROM facturas f WHERE month(f.fecha_factura) = ?1 AND year(f.fecha_factura) = ?2 AND day(f.fecha_factura) = ?3", nativeQuery = true)
	public List<Factura> ingresosDiarios(int mes, int anio, int day);
	
	@Query(value = "SELECT * FROM facturas f WHERE f.fecha_factura BETWEEN ?1 AND ?2", nativeQuery = true)
	public List<Factura> facturasPorFecha(Date desde, Date hasta);

}
