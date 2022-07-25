package com.buenSabor.services;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import com.buenSabor.entity.ArticuloManufacturado;
import com.buenSabor.entity.DetallePedido;
import com.buenSabor.repository.DetallePedidoRepository;
import com.buenSabor.services.dto.RakingComidasDTO;
import com.commons.services.CommonServiceImpl;

@Service
public class DetallePedidoServiceImpl extends CommonServiceImpl<DetallePedido, DetallePedidoRepository> implements DetallePedidoService{
	
	private final ArticuloInsumoService articuloInsumoService;
		
	@Autowired
	private DetallePedidoRepository detPedidoRepository;
	
	public DetallePedidoServiceImpl(ArticuloInsumoService articuloInsumoService) {
		super();
		this.articuloInsumoService = articuloInsumoService;
	}
	
	@Override
	public Iterable<DetallePedido> findDetPedidos(Long id) {
			
		return detPedidoRepository.findByfkPedido(id);
	}


	@Override
	public List<RakingComidasDTO> rankingDeComidas(Date desde, Date hasta) {
		
		List<DetallePedido> detallePedidos = repository.rankingDeComidas(desde, hasta);
		
		System.out.println("Acá"+detallePedidos);
		
		List<RakingComidasDTO> rakingComidasDTOs = new ArrayList<>();
		
        
        Map<ArticuloManufacturado, Long> requirementCountMap = detallePedidos.stream().filter(a -> a.getArticulomanufacturado() != null).collect(Collectors.groupingBy(DetallePedido::getArticulomanufacturado, Collectors.counting()));

        requirementCountMap.entrySet()
        .forEach(e -> {
        		
        		RakingComidasDTO rankingComidas = new RakingComidasDTO();
        
    			rankingComidas.setDenominacion(e.getKey().getDenominacion());
    			rankingComidas.setCanti(e.getValue().doubleValue());
    			
    			Double cantidadVendida = 0.0;
    			for(DetallePedido detallePedido : detallePedidos) {
    				
    				if(detallePedido.getArticulomanufacturado() != null && detallePedido.getArticulomanufacturado().getId() == e.getKey().getId()) {
    					cantidadVendida += (detallePedido.getCantidad());
    				}
    			}
    			
    			System.out.println(cantidadVendida);
    			System.out.println(e.getValue().doubleValue());
    			
    			rankingComidas.setCantidadTotal(cantidadVendida);
    			rankingComidas.setManufacturadoId(e.getKey().getId());
    			
    			rakingComidasDTOs.add(rankingComidas);
    	
    			
        });
        
        //System.out.println("Hola" +requirementCountMap);
		
		
		
		return rakingComidasDTOs.stream().sorted(Comparator.comparingDouble(RakingComidasDTO::getCantidadTotal).reversed()).collect(Collectors.toList());
	}
	
	@Override
    public ByteArrayInputStream generarExcelRanking(List<RakingComidasDTO> rakingComidasDTOs, Date desde, Date hasta) throws Exception {
		
		Calendar desdeParse = Calendar.getInstance();
		desdeParse.setTime(desde);
		
		Calendar hastaParse = Calendar.getInstance();
		desdeParse.setTime(hasta);
		
		SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
	    String formatted = format1.format(desdeParse.getTime());
	    
	    SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
	    String formatted2 = format2.format(hastaParse.getTime());
		
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("RankingDeComidas");
            Integer fila = 0;

            Row row0 = sheet.createRow(fila);
            Cell cell0 = row0.createCell(0);
            cell0.setCellValue("RANKING DE PRODUCTOS MÁS VENDIDOS ENTRE EL " +  formatted + " Y EL " + formatted2);

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.DARK_RED.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

            // Creating header
            ++fila;
            Row row = sheet.createRow(++fila);
            Cell cell = row.createCell(0);
            cell.setCellValue("NOMBRE PRODUCTO");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(1);
            cell.setCellValue("CANTIDAD VENDIDA");
            cell.setCellStyle(headerCellStyle);

            /*cell = row.createCell(2);
            cell.setCellValue("BOLETOS");
            cell.setCellStyle(headerCellStyle);*/

            CellStyle listCellStyle = workbook.createCellStyle();
            listCellStyle.setAlignment(HorizontalAlignment.CENTER);

            for (int i = 0; i < rakingComidasDTOs.size(); i++) {
                Row dataRow = sheet.createRow(++fila);
                Cell cell3 = dataRow.createCell(0);
                cell3.setCellValue(rakingComidasDTOs.get(i).getDenominacion());
                cell3.setCellStyle(listCellStyle);

                cell3 = dataRow.createCell(1);
                cell3.setCellValue(rakingComidasDTOs.get(i).getCantidadTotal());

                /*cell3 = dataRow.createCell(2);
                cell3.setCellValue(listadoDTO.getListadoDetalles().get(i).getCantidadBoletos());
                cell3.setCellStyle(listCellStyle);*/
            }
            
            sheet.createFreezePane(0, 3);
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
