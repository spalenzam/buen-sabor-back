package com.buenSabor.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import net.minidev.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buenSabor.entity.Pedido;
import com.buenSabor.repository.PedidoRepository;
import com.buenSabor.services.Enumeration.EstadoEnum;
import com.buenSabor.services.Enumeration.EstadoInternoEnum;
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

	@Override
	public List getAllEstados() {
		
		List lista = new ArrayList<EstadoEnum>();

        for(EstadoEnum e : EstadoEnum.values()) {
            JSONObject myObject = new JSONObject();
            myObject.put("codigo", e.getCodigo());
            myObject.put("nombre", e.getEstado());
            lista.add(myObject);
        }

        return  lista;
	}

	@Override
	public List getAllEstadosInternos() {
		
		List lista = new ArrayList<EstadoInternoEnum>();

        for(EstadoInternoEnum e : EstadoInternoEnum.values()) {
            JSONObject myObject = new JSONObject();
            myObject.put("codigo", e.getCodigo());
            myObject.put("nombre", e.getEstado());
            lista.add(myObject);
        }

        return  lista;
	}

}
