package com.buenSabor.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buenSabor.entity.Pedido;
import com.buenSabor.repository.PedidoRepository;
import com.buenSabor.services.errors.BuenSaborException;
import com.buenSabor.services.errors.ErrorConstants;
import com.commons.services.CommonServiceImpl;

@Service
public class PedidoServiceImpl extends CommonServiceImpl<Pedido, PedidoRepository> implements PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;

	@Override
	public Iterable<Pedido> findAllPedidosAlta() {
		// TODO Auto-generated method stub
		return pedidoRepository.findAllPedidosAlta();
	}

	@Override
	public Pedido deleteByIdAndBaja(Long id) {
		Optional<Pedido> pedidoOptional = findById(id);

		if (pedidoOptional.isPresent()) {

			pedidoOptional.get().setFechaBaja(new Date());
			save(pedidoOptional.get());
			return pedidoOptional.get();
		} else {
			throw new BuenSaborException(ErrorConstants.ERR_BUSCAR,
					"No se encontró la entidad a la cual le quiere dar de baja");
		}
	}

}
