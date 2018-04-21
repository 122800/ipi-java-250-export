package com.example.demo.service.export.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.example.demo.dto.LigneFactureDTO;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.Barcode;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFTableGenerator {

	private List<String> headers = new ArrayList<>();
	private List<Function<LigneFactureDTO, Object>> functions = new ArrayList<>();

	private Document pdfDoc;
	private PdfPTable table;
	private PdfContentByte cb;

	public PDFTableGenerator(Document document, PdfWriter writer) {
		pdfDoc = document;
		cb = writer.getDirectContent();
	}

	public void addColumn(String headerName, Function<LigneFactureDTO, Object> function) {
		headers.add(headerName);
		functions.add(function);
	}

	public void writePDFTable(List<LigneFactureDTO> lignes) throws IOException, DocumentException {
		headers.add("Code barre");// Random stuff

		table = new PdfPTable(headers.size());

		for(String header : headers) {
			PdfPCell headerCell = new PdfPCell();

        	headerCell.setPhrase(new Phrase(header));

        	headerCell.setBorderWidth(1);
        	headerCell.setBackgroundColor(BaseColor.GRAY);
        	headerCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        	headerCell.setPadding(5);;

        	table.addCell(headerCell);
    	}

    	for (LigneFactureDTO ligne : lignes) {

    		table.addCell(genRandomBarCodeCell(cb));

    		for(Function<LigneFactureDTO, Object> function : functions) {
            	table.addCell(function.apply(ligne).toString());
            }
         }

    	pdfDoc.add(table);
	}

	private PdfPCell genRandomBarCodeCell(PdfContentByte cb) {
    	String random14Digits = String.valueOf(Math.random()).substring(2, 16);

    	Barcode128 code128 = new Barcode128();
    	code128.setCode(random14Digits);
    	code128.setCodeType(Barcode.CODE128);
    	Image code128Image = code128.createImageWithBarcode(cb, null, null);

    	return new PdfPCell(code128Image, true);
    }
}