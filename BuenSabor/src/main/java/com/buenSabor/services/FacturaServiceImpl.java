package com.buenSabor.services;


import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.io.ClassPathResource;

import com.buenSabor.entity.DetalleFactura;
import com.buenSabor.entity.Factura;
import com.buenSabor.repository.FacturaRepository;
import com.buenSabor.services.dto.DetalleFacturaDTO;
import com.buenSabor.services.dto.FacturaDTO;
import com.buenSabor.services.errors.BuenSaborException;
import com.buenSabor.services.errors.ErrorConstants;
import com.commons.services.CommonServiceImpl;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
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

                /**Seteamos los atributos a la facturaDTO**/
            	facturaDTO.setNombre(factura.getPedido().getCliente().getNombre());
            	facturaDTO.setApellido(factura.getPedido().getCliente().getApellido());
            	facturaDTO.setCalle(factura.getPedido().getCliente().getDomicilio().getCalle());
            	facturaDTO.setNumeroCalle(String.valueOf(factura.getPedido().getCliente().getDomicilio().getNumero()));
            	facturaDTO.setLocalidad(factura.getPedido().getCliente().getDomicilio().getLocalidad());
     
            	String formattedDate = DateFormat
            			  .getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
            			  .format(factura.getFechaFactura());
            	facturaDTO.setFechaFactura(formattedDate);
            	facturaDTO.setNumeroFactura(String.valueOf(factura.getNumeroFactura()));
            	facturaDTO.setMontoDescuento(factura.getMontoDescuento().toString());
            	facturaDTO.setFormaPago(factura.getFormaPago());
            	facturaDTO.setNroTarjeta(factura.getNroTarjeta());
            	
            	
                
            	/**Seteamos las listas al FacturaDTO**/
                //DetalleFacturaDTO
                List<DetalleFacturaDTO> detallesList = new ArrayList<>();
                
                Double totalCostoDouble = 0.0;
                
            	for(DetalleFactura detalleFactura : factura.getDetallefacturas()) {
            		
            		DetalleFacturaDTO detalleFacturaDTO = new DetalleFacturaDTO();
            		
            		detalleFacturaDTO.setCantidad(String.valueOf(detalleFactura.getCantidad()));
            		
            		if(detalleFactura.getArtmanufacturado() != null)
            			detalleFacturaDTO.setDenominacionProducto(detalleFactura.getArtmanufacturado().getDenominacion());
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
        	
            //Creo el vector con la colección de todos los DTOS que quiera tener
            Vector collection = new Vector();
            
            //Creo la facturaDTO
            FacturaDTO facturaDTO = datosFacturaDto(factura);

            collection.add(facturaDTO);

            //Dirección donde está el reporte
            String path_report = new ClassPathResource("static").getPath();
            String path_images = new ClassPathResource("static").getPath();

            InputStream jasperFile = new ClassPathResource("static/FacturaBuenSabor.jrxml").getInputStream();

            //Parámetros de los directorios que vamos a usar y la dirección correspondiente
            HashMap parameters = new HashMap();
            parameters.put("SUBREPORT_DIR", path_report);
            parameters.put("IMAGES_DIR", path_images);

            //Pasamos el reporte que queremos cargar
            JasperDesign jasperDesign = JRXmlLoader.load(jasperFile);
            
            //se compila el reporte
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            //Le pasamos la colección que queremos usar
            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(collection);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);

            // lo graba a disco
            JasperExportManager.exportReportToPdfFile(jasperPrint, "c://temp" + File.separator + "factura " + factura.getNumeroFactura() +".pdf");

            //return null;

            //Brinda el PDF para que se pueda mostrar
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


}
