package com.example.demo.service.export;

import com.example.demo.dto.ClientDTO;
import com.example.demo.dto.FactureDTO;
import com.example.demo.dto.LigneFactureDTO;
import com.example.demo.service.export.internal.XSLXFileGenerator;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.servlet.ServletOutputStream;

@Service
public class ExportXLSXService {  

    public void export(OutputStream os, List<ClientDTO> clients) throws IOException {
    	
    	XSLXFileGenerator<ClientDTO> excelWriter = new XSLXFileGenerator<>();
        
        excelWriter.addColumn("Nom", ClientDTO::getNom);
    	excelWriter.addColumn("Prenom", ClientDTO::getPrenom);
    	excelWriter.addColumn("Nombre d'armes", (c) -> c.getNb_weapons().toString());
    	excelWriter.addColumn("Nom de l'arme favorite", ClientDTO::getFav_weapon);
    	
    	// maybe get a list of the names of each of the victims?
    	excelWriter.addColumn("Noms des victimes",
    			(client) -> client.getVictims().stream()// get each victim
	    			.map((victime) -> victime.getNom())// get name from each victim
	    			.reduce("", (a, b) -> // concatenate list of names
	    				a
	    				+ (a.length() > 0 ? ", " : "")// add ', ' after first name in the list
	    				+ b)
    			);
    	
    	excelWriter.newSheet("clients", clients);
    	
    	excelWriter.write(os);
    	excelWriter.close();
    	
    	os.close();
    }

	public void exportfacturesDUnClient(OutputStream outputStream, List<FactureDTO> factures) throws IOException {
		
		XSLXFileGenerator<LigneFactureDTO> excelWriter = new XSLXFileGenerator<>();
		
		excelWriter.addColumn("Produit", LigneFactureDTO::getDesignation);
		excelWriter.addColumn("Quantité", (l) -> l.getQuantite().toString());
		excelWriter.addColumn("Prix unitaire", (l) -> l.getPrixUnitaire().toString());
		
		// THE BORING WAY OF DOING THIS
		//excelWriter.addColumn("Prix total", (l) -> Double.toString(l.getPrixUnitaire() * l.getQuantite()));
		
		BiConsumer<XSSFSheet, XSSFCell> getTotalPrice = (sheet, cell) -> {
			int cellIndex = cell.getColumnIndex();
			XSSFRow row = cell.getRow();
			
			XSSFCell quantiteCell = row.getCell(cellIndex - 1);
			XSSFCell prixCell = row.getCell(cellIndex - 2);
			
			String formula = quantiteCell.getReference() + " * " + prixCell.getReference();
			
			cell.setCellFormula(formula);
		};
		
		excelWriter.addUniqueColumn("Prix total", getTotalPrice);

		for(FactureDTO facture : factures) {
			excelWriter.newSheet(
				"Facture #" + facture.getId(),
				facture.getLigneFactures()
			);
		}
		
		excelWriter.write(outputStream);
    	excelWriter.close();
    	
    	outputStream.close();
    	
	}
}
