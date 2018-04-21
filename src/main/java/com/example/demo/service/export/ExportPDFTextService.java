package com.example.demo.service.export;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.stereotype.Service;

import com.example.demo.dto.ClientDTO;
import com.example.demo.dto.FactureDTO;
import com.example.demo.dto.LigneFactureDTO;
import com.example.demo.service.export.internal.PDFTableGenerator;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class ExportPDFTextService {

    public void export(OutputStream os, FactureDTO facture) throws IOException, DocumentException {

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, os);

        document.open();

        ClientDTO client = facture.getClient();

        Paragraph factureTitle = new Paragraph(
    		"Facture #" + facture.getId()
    		+ " -- "
    		+ client.getPrenom() + " " + client.getNom()
    		+ "\n\n"
        );
        factureTitle.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(factureTitle);

        PDFTableGenerator pdfTableWriter = new PDFTableGenerator(document, writer);

        pdfTableWriter.addColumn("Produit", LigneFactureDTO::getDesignation);
		pdfTableWriter.addColumn("Quantité", LigneFactureDTO::getQuantite);
		pdfTableWriter.addColumn("Prix unitaire", LigneFactureDTO::getPrixUnitaire);

		pdfTableWriter.writePDFTable(facture.getLigneFactures());
		
		Double sommeTotale = facture.getLigneFactures().stream()
				.map((ligne) -> ligne.getPrixUnitaire() * ligne.getQuantite())
				.reduce(0d, (a, b) -> a + b);
		
		Paragraph sommeTotaleP = new Paragraph("Somme totale : " + (Math.round(sommeTotale*100)/100d) + "€");
		sommeTotaleP.setAlignment(Paragraph.ALIGN_RIGHT);
        document.add(sommeTotaleP);

        document.close();
    }
}
