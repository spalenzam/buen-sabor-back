package com.buenSabor.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.io.ClassPathResource;

import com.buenSabor.entity.DetalleFactura;
import com.buenSabor.entity.Factura;
import com.buenSabor.repository.FacturaRepository;
import com.buenSabor.services.dto.DetalleFacturaDTO;
import com.buenSabor.services.dto.FacturaDTO;
import com.buenSabor.services.dto.IngresosDiarioYMensualDTO;
import com.buenSabor.services.dto.RakingComidasDTO;
import com.buenSabor.services.errors.BuenSaborException;
import com.buenSabor.services.errors.ErrorConstants;
import com.commons.services.CommonServiceImpl;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.util.*;

@Service
public class FacturaServiceImpl extends CommonServiceImpl<Factura, FacturaRepository> implements FacturaService {

	@Autowired
	private FacturaRepository facturaRepository;

	private final Logger log = LoggerFactory.getLogger(FacturaServiceImpl.class);

	public FacturaServiceImpl(FacturaRepository facturaRepository) {
		super();
		this.facturaRepository = facturaRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public Factura findByNumeroFactura(Integer numFactura) {
		return facturaRepository.findByNumeroFactura(numFactura);
	}

	@Override
	public FacturaDTO datosFacturaDto(Factura factura) throws Exception {

		try {

			FacturaDTO facturaDTO = new FacturaDTO();

			Optional<Factura> facturaOptional = findById(factura.getId());

			if (facturaOptional.isPresent()) {

				/** Seteamos los atributos a la facturaDTO **/
				facturaDTO.setNombre(factura.getPedido().getCliente().getNombre());
				facturaDTO.setApellido(factura.getPedido().getCliente().getApellido());
				facturaDTO.setCalle(factura.getPedido().getCliente().getDomicilio().getCalle());
				facturaDTO.setNumeroCalle(String.valueOf(factura.getPedido().getCliente().getDomicilio().getNumero()));
				facturaDTO.setLocalidad(factura.getPedido().getCliente().getDomicilio().getLocalidad());

				String formattedDate = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
						.format(factura.getFechaFactura());
				facturaDTO.setFechaFactura(formattedDate);
				facturaDTO.setNumeroFactura(String.valueOf(factura.getNumeroFactura()));
				facturaDTO.setMontoDescuento(factura.getMontoDescuento().toString());
				facturaDTO.setFormaPago(factura.getFormaPago());
				facturaDTO.setNroTarjeta(factura.getNroTarjeta());

				/** Seteamos las listas al FacturaDTO **/
				// DetalleFacturaDTO
				List<DetalleFacturaDTO> detallesList = new ArrayList<>();

				Double totalCostoDouble = 0.0;

				for (DetalleFactura detalleFactura : factura.getDetallefacturas()) {

					DetalleFacturaDTO detalleFacturaDTO = new DetalleFacturaDTO();

					detalleFacturaDTO.setCantidad(String.valueOf(detalleFactura.getCantidad()));

					if (detalleFactura.getArtmanufacturado() != null)
						detalleFacturaDTO
								.setDenominacionProducto(detalleFactura.getArtmanufacturado().getDenominacion());
					else
						detalleFacturaDTO.setDenominacionProducto(detalleFactura.getArtinsumo().getDenominacion());
					detalleFacturaDTO.setSubtotal(detalleFactura.getSubtotal().toString());

					totalCostoDouble += detalleFactura.getSubtotal();

					detallesList.add(detalleFacturaDTO);
				}

				Double ventaFinal = totalCostoDouble - factura.getMontoDescuento();

				facturaDTO.setTotalCosto(totalCostoDouble.toString());
				facturaDTO.setTotalVenta(factura.getTotalVenta().toString());
				facturaDTO.setDetallesList(detallesList);

				return facturaDTO;

			} else {
				throw new BuenSaborException(ErrorConstants.ERR_BUSCAR, "La factura no existe");
			}
		} catch (BuenSaborException e) {
			log.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}

	@Override
	public ByteArrayInputStream generarFacturaPDF(Factura factura) throws Exception {
		try {

			// Creo el vector con la colección de todos los DTOS que quiera tener
			Vector collection = new Vector();

			// Creo la facturaDTO
			FacturaDTO facturaDTO = datosFacturaDto(factura);

			collection.add(facturaDTO);

			// Dirección donde está el reporte
			String path_report = new ClassPathResource("static").getPath();
			String path_images = new ClassPathResource("static").getPath();

			InputStream jasperFile = new ClassPathResource("static/FacturaBuenSabor.jrxml").getInputStream();

			// Parámetros de los directorios que vamos a usar y la dirección correspondiente
			HashMap parameters = new HashMap();
			parameters.put("SUBREPORT_DIR", path_report);
			parameters.put("IMAGES_DIR", path_images);

			// Pasamos el reporte que queremos cargar
			JasperDesign jasperDesign = JRXmlLoader.load(jasperFile);

			// se compila el reporte
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

			// Le pasamos la colección que queremos usar
			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(collection);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);

			// lo graba a disco
			JasperExportManager.exportReportToPdfFile(jasperPrint,
					"c://temp" + File.separator + "factura " + factura.getNumeroFactura() + ".pdf");

			// return null;

			// Brinda el PDF para que se pueda mostrar
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, out);

			return new ByteArrayInputStream(out.toByteArray());

		} catch (JRException ex) {
			log.error(ex.getMessage());
			throw ex;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}

	@Override
	public IngresosDiarioYMensualDTO ingresoMensual(Date date) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);

		System.out.println(date);

		List<Factura> facturas = repository.ingresosMensuales(month, year);

		Double total = 0.0;

		for (Factura factura : facturas) {
			total += factura.getTotalVenta();

			System.out.println(factura.getTotalVenta());
		}

		IngresosDiarioYMensualDTO ingresosDiarioYMensualDTO = new IngresosDiarioYMensualDTO();
		ingresosDiarioYMensualDTO.setFactura(facturas);
		ingresosDiarioYMensualDTO.setIngreso(total);

		return ingresosDiarioYMensualDTO;
	}

	@Override
	public ByteArrayInputStream generarExcelIngresoMensual(IngresosDiarioYMensualDTO ingresosDiarioYMensualDTO,
			Date date) throws Exception {

		Calendar fechaParse = Calendar.getInstance();
		fechaParse.setTime(date);

		SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
		String formatted = format1.format(fechaParse.getTime());

		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("IngresoMensual");
			Integer fila = 0;

			Row row0 = sheet.createRow(fila);
			Cell cell0 = row0.createCell(0);
			cell0.setCellValue("INGRESOS MENSUALES DE LA FECHA " + formatted);

			sheet.addMergedRegion(new CellRangeAddress(0, // first row (0-based)
					0, // last row (0-based)
					0, // first column (0-based)
					2 // last column (0-based)
			));

			Row row1 = sheet.createRow(++fila);
			Cell cell1 = row1.createCell(0);
			cell1.setCellValue("El ingreso mensual es de $ " + ingresosDiarioYMensualDTO.getIngreso());
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 2));

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFillForegroundColor(IndexedColors.DARK_RED.getIndex());
			headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

			// Creating header
			++fila;
			Row row = sheet.createRow(++fila);
			Cell cell = row.createCell(0);
			cell.setCellValue("NÚMERO DE FACTURA");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(1);
			cell.setCellValue("TOTAL VENTA");
			cell.setCellStyle(headerCellStyle);
			
			cell = row.createCell(2);
			cell.setCellValue("DETALLE");
			cell.setCellStyle(headerCellStyle);

			

			CellStyle listCellStyle = workbook.createCellStyle();
			listCellStyle.setAlignment(HorizontalAlignment.CENTER);

			for (int i = 0; i < ingresosDiarioYMensualDTO.getFactura().size(); i++) {
				Row dataRow = sheet.createRow(++fila);
				Cell cell3 = dataRow.createCell(0);
				cell3.setCellValue(ingresosDiarioYMensualDTO.getFactura().get(i).getNumeroFactura());
				cell3.setCellStyle(listCellStyle);

				cell3 = dataRow.createCell(1);
				cell3.setCellValue(ingresosDiarioYMensualDTO.getFactura().get(i).getTotalVenta());

				String detalle = "";
				
				for(DetalleFactura deta : ingresosDiarioYMensualDTO.getFactura().get(i).getDetallefacturas()) {
					detalle = detalle.concat(String.valueOf(deta.getCantidad()) + " - " + deta.getArtmanufacturado().getDenominacion() + ", ");
				}
			
				
				cell3 = dataRow.createCell(2);
				cell3.setCellValue(detalle);
			}

			sheet.createFreezePane(0, 4);
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			return new ByteArrayInputStream(outputStream.toByteArray());

		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public IngresosDiarioYMensualDTO ingresoDiario(Date fecha) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(fecha);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		System.out.println(month);
		System.out.println(year);
		System.out.println(day);

		List<Factura> facturas = repository.ingresosDiarios(month, year, day);

		Double total = 0.0;

		for (Factura factura : facturas) {
			total += factura.getTotalVenta();

			System.out.println(factura.getTotalVenta());
		}

		IngresosDiarioYMensualDTO ingresosDiarioYMensualDTO = new IngresosDiarioYMensualDTO();
		ingresosDiarioYMensualDTO.setFactura(facturas);
		ingresosDiarioYMensualDTO.setIngreso(total);

		return ingresosDiarioYMensualDTO;
	}

	@Override
	public ByteArrayInputStream generarExcelIngresoDiario(IngresosDiarioYMensualDTO ingresosDiarioYMensualDTO,
			Date date) throws Exception {

		Calendar fechaParse = Calendar.getInstance();
		fechaParse.setTime(date);

		SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
		String formatted = format1.format(fechaParse.getTime());

		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("IngresoDiario");
			Integer fila = 0;

			Row row0 = sheet.createRow(fila);
			Cell cell0 = row0.createCell(0);
			cell0.setCellValue("INGRESO DIARIO DE LA FECHA " + formatted);

			sheet.addMergedRegion(new CellRangeAddress(0, // first row (0-based)
					0, // last row (0-based)
					0, // first column (0-based)
					2 // last column (0-based)
			));

			Row row1 = sheet.createRow(++fila);
			Cell cell1 = row1.createCell(0);
			cell1.setCellValue("El ingreso diario es de $ " + ingresosDiarioYMensualDTO.getIngreso());
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 2));

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFillForegroundColor(IndexedColors.DARK_RED.getIndex());
			headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

			// Creating header
			++fila;
			Row row = sheet.createRow(++fila);
			Cell cell = row.createCell(0);
			cell.setCellValue("NÚMERO DE FACTURA");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(1);
			cell.setCellValue("TOTAL VENTA");
			cell.setCellStyle(headerCellStyle);
			
			cell = row.createCell(2);
			cell.setCellValue("DETALLE");
			cell.setCellStyle(headerCellStyle);



			CellStyle listCellStyle = workbook.createCellStyle();
			listCellStyle.setAlignment(HorizontalAlignment.CENTER);

			for (int i = 0; i < ingresosDiarioYMensualDTO.getFactura().size(); i++) {
				Row dataRow = sheet.createRow(++fila);
				Cell cell3 = dataRow.createCell(0);
				cell3.setCellValue(ingresosDiarioYMensualDTO.getFactura().get(i).getNumeroFactura());
				cell3.setCellStyle(listCellStyle);

				cell3 = dataRow.createCell(1);
				cell3.setCellValue(ingresosDiarioYMensualDTO.getFactura().get(i).getTotalVenta());
				

				String detalle = "";
				
				for(DetalleFactura deta : ingresosDiarioYMensualDTO.getFactura().get(i).getDetallefacturas()) {
					detalle = detalle.concat(String.valueOf(deta.getCantidad()) + " - " + deta.getArtmanufacturado().getDenominacion() + ", ");
				}
			
				
				cell3 = dataRow.createCell(2);
				cell3.setCellValue(detalle);
				
				System.out.println(detalle);
				

			}

			sheet.createFreezePane(0, 4);
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			return new ByteArrayInputStream(outputStream.toByteArray());

		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public IngresosDiarioYMensualDTO gananciasPorFecha(Date desde, Date hasta) {
		List<Factura> facturas = facturaRepository.facturasPorFecha(desde, hasta);

		Double gananciasDouble = 0.0;

		for (Factura factura : facturas) {
			gananciasDouble = gananciasDouble + (factura.getTotalVenta() - factura.getTotalCosto());
		}

		IngresosDiarioYMensualDTO ingresosDiarioYMensualDTO = new IngresosDiarioYMensualDTO();
		ingresosDiarioYMensualDTO.setFactura(facturas);
		ingresosDiarioYMensualDTO.setIngreso(gananciasDouble);

		return ingresosDiarioYMensualDTO;
	}

	@Override
	public ByteArrayInputStream generarExcelGananciasPorFecha(IngresosDiarioYMensualDTO ingresosDiarioYMensualDTO,
			Date desde, Date hasta) throws Exception {

		Calendar desdeParse = Calendar.getInstance();
		desdeParse.setTime(desde);

		Calendar hastaParse = Calendar.getInstance();
		desdeParse.setTime(hasta);

		SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
		String formatted = format1.format(desdeParse.getTime());

		SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
		String formatted2 = format2.format(hastaParse.getTime());

		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("IngresoDiario");
			Integer fila = 0;

			Row row0 = sheet.createRow(fila);
			Cell cell0 = row0.createCell(0);
			cell0.setCellValue("GANANCIA ENTRE EL " + formatted + " Y EL " + formatted2);

			sheet.addMergedRegion(new CellRangeAddress(0, // first row (0-based)
					0, // last row (0-based)
					0, // first column (0-based)
					2 // last column (0-based)
			));

			Row row1 = sheet.createRow(++fila);
			Cell cell1 = row1.createCell(0);
			cell1.setCellValue("La ganancia es de $ " + ingresosDiarioYMensualDTO.getIngreso());
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 2));

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFillForegroundColor(IndexedColors.DARK_RED.getIndex());
			headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

			// Creating header
			++fila;
			Row row = sheet.createRow(++fila);
			Cell cell = row.createCell(0);
			cell.setCellValue("NÚMERO DE FACTURA");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(1);
			cell.setCellValue("TOTAL COSTO");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(2);
			cell.setCellValue("TOTAL VENTA");
			cell.setCellStyle(headerCellStyle);

			CellStyle listCellStyle = workbook.createCellStyle();
			listCellStyle.setAlignment(HorizontalAlignment.CENTER);

			for (int i = 0; i < ingresosDiarioYMensualDTO.getFactura().size(); i++) {
				Row dataRow = sheet.createRow(++fila);
				Cell cell3 = dataRow.createCell(0);
				cell3.setCellValue(ingresosDiarioYMensualDTO.getFactura().get(i).getNumeroFactura());
				cell3.setCellStyle(listCellStyle);

				cell3 = dataRow.createCell(1);
				cell3.setCellValue(ingresosDiarioYMensualDTO.getFactura().get(i).getTotalCosto());

				cell3 = dataRow.createCell(2);
				cell3.setCellValue(ingresosDiarioYMensualDTO.getFactura().get(i).getTotalVenta());
				cell3.setCellStyle(listCellStyle);

			}

			sheet.createFreezePane(0, 4);
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			return new ByteArrayInputStream(outputStream.toByteArray());

		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
