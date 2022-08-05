package com.buenSabor.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.buenSabor.entity.Pedido;

public interface PedidoRepository extends PagingAndSortingRepository<Pedido, Long> {
	
	@Query("select a from Pedido a where a.fechaBaja is null")
	public Iterable<Pedido> findAllPedidosAlta();
	
	@Query(value = "SELECT * FROM pedidos p WHERE p.fecha_pedido BETWEEN ?1 AND ?2 ", nativeQuery = true)
	public List<Pedido> pedidosPorFecha(Date desde, Date hasta);
	
	public Pedido findTopByOrderByIdDesc();
}
