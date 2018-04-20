package com.example.demo.service.export.internal;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XSLXFileGenerator<T> {
	
	XSSFWorkbook workbook = new XSSFWorkbook();
	
	public static final int HEADER_SIZE = 1;
	private List<String> headers = new ArrayList<>();
	private List<Function<T, String>> functions = new ArrayList<>();
	private List<BiConsumer<XSSFSheet, XSSFCell>> uniqueFunctions = new ArrayList<>();
	
	public void addColumn(String headerName, Function<T, String> function) {
    	headers.add(headerName);
		functions.add(function);
    }
	
	public void addUniqueColumn(String headerName, BiConsumer<XSSFSheet, XSSFCell> function) {
    	headers.add(headerName);
		uniqueFunctions.add(function);
    }
	
	public void newSheet(String name, List<T> liste) {
		XSSFSheet sheet = workbook.createSheet(name);
		XSSFRow headerRow = sheet.createRow(0);
		
		// write headers
		int i = 0;
    	for(String header : headers) {
			headerRow.createCell(i++).setCellValue(header);
    	}
    	
    	writeColumns(sheet, liste);
	}
	
	private void writeColumns(XSSFSheet sheet, List<T> liste) {
		
		int i = 0;
		for(T element : liste) {
			XSSFRow currentRow = sheet.createRow((i++) + HEADER_SIZE);
			
			int j = 0;
			for(Function<T, String> function : functions) {
				String stringValue = function.apply(element);
				
				try {
					Double numericalValue = Double.parseDouble(stringValue);
					currentRow.createCell(j++).setCellValue(numericalValue);
					
				} catch (NumberFormatException e) {
					currentRow.createCell(j++).setCellValue(stringValue);					
				}
				
	    	}
			
			for(BiConsumer<XSSFSheet, XSSFCell> uniqueFunction : uniqueFunctions) {
				uniqueFunction.accept(sheet, currentRow.createCell(j++));// apply custom function to cell
	    	}
		}
	}

	public void write(OutputStream os) throws IOException {
		workbook.write(os);   
	}
	public void close() throws IOException {
		workbook.close();
	}

}
