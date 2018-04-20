package com.example.demo.service.export.internal;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.example.demo.dto.ClientDTO;

public class XSLXFileWriter {
	
	public static final int HEADER_SIZE = 1;
	private List<String> headers = new ArrayList<>();
	private List<Function<ClientDTO, Object>> functions = new ArrayList<>();
	
	public void addColumn(String headerName, Function<ClientDTO, Object> function) {
    	headers.add(headerName);
		functions.add(function);
    }
	
	public XSSFWorkbook generateXSLXFile(OutputStream os, List<ClientDTO> clients) throws IOException {
    	XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("clients");
        
        XSSFRow headerRow = sheet.createRow(0);
        
        int i = 0;
    	for(String header : headers) {
    		headerRow.createCell(i++).setCellValue(header);
    	}
    	
    	i = 0;
    	for (ClientDTO client : clients) {
    		
    		XSSFRow clientRow = sheet.createRow((i++) + HEADER_SIZE);
    		
    		int j = 0;
    		for(Function<ClientDTO, Object> function : functions) {
    			clientRow.createCell(j++).setCellValue(
    					function.apply(client).toString()
    			);
        	}
         }
    	
    	return workbook;
	}

}
