package com.buenSabor.repository;


import java.util.Date;
import java.util.List;



import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.buenSabor.entity.ArticuloManufacturado;
import com.buenSabor.entity.DetallePedido;

public interface DetallePedidoRepository extends CrudRepository<DetallePedido, Long>{
	

	
	@Query(value = "SELECT d.* FROM pedidos p " + 
			"INNER JOIN detalle_pedido d " + 
			"ON p.id = d.fk_pedido " + 
			"WHERE p.estado LIKE 'Facturado' AND p.fecha_pedido BETWEEN ?1 AND ?2", nativeQuery = true)
	public List<DetallePedido> rankingDeComidas(Date desde, Date hasta);

	@Query("select a from DetallePedido a where fk_pedido = ?1")
	public Iterable<DetallePedido> findByfkPedido(Long id);


	@Query("select a from DetallePedido a where fk_articulo_manufacturado = ?1")
	public Iterable<ArticuloManufacturado> findArtManuf(Long id);
	
}