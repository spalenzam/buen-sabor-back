package com.buenSabor.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.buenSabor.entity.Pedido;

public interface PedidoRepository extends PagingAndSortingRepository<Pedido, Long> {
	
	@Query("select a from Pedido a where a.fechaBaja is null")
	public Iterable<Pedido> findAllPedidosAlta();
	
}
