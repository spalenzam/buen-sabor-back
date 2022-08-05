package com.buenSabor.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import net.minidev.json.JSONObject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buenSabor.entity.Cliente;
import com.buenSabor.entity.Pedido;
import com.buenSabor.repository.PedidoRepository;
import com.buenSabor.services.Enumeration.EstadoEnum;
import com.buenSabor.services.Enumeration.EstadoInternoEnum;
import com.buenSabor.services.dto.PedidosPorClienteDTO;
import com.buenSabor.services.dto.RakingComidasDTO;
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

		for (EstadoEnum e : EstadoEnum.values()) {
			JSONObject myObject = new JSONObject();
			myObject.put("codigo", e.getCodigo());
			myObject.put("nombre", e.getEstado());
			lista.add(myObject);
		}

		return lista;
	}

	@Override
	public List getAllEstadosInternos() {

		List lista = new ArrayList<EstadoInternoEnum>();

		for (EstadoInternoEnum e : EstadoInternoEnum.values()) {
			JSONObject myObject = new JSONObject();
			myObject.put("codigo", e.getCodigo());
			myObject.put("nombre", e.getEstado());
			lista.add(myObject);
		}

		return lista;
	}

	@Override
	public List<PedidosPorClienteDTO> listarPedidosPorCliente(Date desde, Date hasta) {

		List<Pedido> pedidos = repository.pedidosPorFecha(desde, hasta);

		System.out.println("Acá" + pedidos);

		List<PedidosPorClienteDTO> pedidosPorClienteDTOs = new ArrayList<>();

		Map<Cliente, Long> requirementCountMap = pedidos.stream()
				.collect(Collectors.groupingBy(Pedido::getCliente, Collectors.counting()));

		requirementCountMap.entrySet().forEach(e -> {

			PedidosPorClienteDTO pedidosPorClienteDTO = new PedidosPorClienteDTO();
			
			pedidosPorClienteDTO.setCliente(e.getKey());
			pedidosPorClienteDTO.setCantidadDePedidos(e.getValue().intValue());

			pedidosPorClienteDTOs.add(pedidosPorClienteDTO);
		});

		// System.out.println("Hola" +requirementCountMap);

		return pedidosPorClienteDTOs.stream().sorted(Comparator.comparingInt(PedidosPorClienteDTO::getCantidadDePedidos).reversed()).collect(Collectors.toList());
	}

	@Override
	public InputStream generarExcelPedidosPorCliente(List<PedidosPorClienteDTO> listaPedidos, Date desde, Date hasta) {
		
		Calendar desdeParse = Calendar.getInstance();
		desdeParse.setTime(desde);

		Calendar hastaParse = Calendar.getInstance();
		desdeParse.setTime(hasta);

		SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
		String formatted = format1.format(desdeParse.getTime());

		SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
		String formatted2 = format2.format(hastaParse.getTime());

		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("PedidosPorCliente");
			Integer fila = 0;

			Row row0 = sheet.createRow(fila);
			Cell cell0 = row0.createCell(0);
			cell0.setCellValue("PEDIDOS POR CLIENTE ENTRE EL " + formatted + " Y EL " + formatted2);

			/*
			 * sheet.addMergedRegion(new CellRangeAddress(0, // first row (0-based) 0, //
			 * last row (0-based) 0, // first column (0-based) 2 // last column (0-based)
			 * ));
			 */

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFillForegroundColor(IndexedColors.DARK_RED.getIndex());
			headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

			// Creating header
			++fila;
			Row row = sheet.createRow(++fila);
			Cell cell = row.createCell(0);
			cell.setCellValue("EMAIL");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(1);
			cell.setCellValue("APELLIDO");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(2);
			cell.setCellValue("NOMBRE");
			cell.setCellStyle(headerCellStyle);
			
			cell = row.createCell(3);
			cell.setCellValue("CANTIDAD DE PEDIDOS");
			cell.setCellStyle(headerCellStyle);

			CellStyle listCellStyle = workbook.createCellStyle();
			listCellStyle.setAlignment(HorizontalAlignment.CENTER);

			for (int i = 0; i < listaPedidos.size(); i++) {
				Row dataRow = sheet.createRow(++fila);
				Cell cell3 = dataRow.createCell(0);
				cell3.setCellValue(listaPedidos.get(i).getCliente().getEmail());
				cell3.setCellStyle(listCellStyle);

				cell3 = dataRow.createCell(1);
				cell3.setCellValue(listaPedidos.get(i).getCliente().getApellido());
				cell3.setCellStyle(listCellStyle);

				cell3 = dataRow.createCell(2);
				cell3.setCellValue(listaPedidos.get(i).getCliente().getNombre());
				cell3.setCellStyle(listCellStyle);
				
				cell3 = dataRow.createCell(3);
				cell3.setCellValue(listaPedidos.get(i).getCantidadDePedidos());
				cell3.setCellStyle(listCellStyle);

			}

			sheet.createFreezePane(0, 3);
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			return new ByteArrayInputStream(outputStream.toByteArray());

		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Pedido ultimoPedido() {
		return pedidoRepository.findTopByOrderByIdDesc();
	}

}
