package com.example.demo.service.export;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.function.Consumer;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ClientDTO;
import com.example.demo.dto.FactureDTO;
import com.example.demo.dto.LigneFactureDTO;
import com.example.demo.service.export.internal.XSLXFileGenerator;

@Service
public class ExportXLSXService {

    public void export(OutputStream outputStream, List<ClientDTO> clients) throws IOException {

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

    	excelWriter.newSheet("clients");
    	excelWriter.writeRows(clients);

    	excelWriter.write(outputStream);
    	excelWriter.close();

    	outputStream.close();
    }

	public void exportfacturesDUnClient(OutputStream outputStream, List<FactureDTO> factures) throws IOException {

		XSLXFileGenerator<LigneFactureDTO> excelWriter = new XSLXFileGenerator<>();

		excelWriter.addColumn("Produit", LigneFactureDTO::getDesignation);
		excelWriter.addColumn("Quantité", (l) -> l.getQuantite().toString());
		excelWriter.addColumn("Prix unitaire", (l) -> l.getPrixUnitaire().toString());
		
		// THE BORING WAY OF GETTING THE SUM
		//excelWriter.addColumn("Prix total", (l) -> Double.toString(l.getPrixUnitaire() * l.getQuantite()));
		
		// THE TERRIBLE BUT LESS BORING WAY
		excelWriter.addUniqueColumn("Prix total", getTotalPriceFunction);

		for(FactureDTO facture : factures) {
			excelWriter.newSheet("Facture #" + facture.getId());
			excelWriter.writeRows(facture.getLigneFactures());

			// ALSO PRETTY TERRIBLE
			excelWriter.writeCustomRow(getSommeTotaleFunction);
		}

		excelWriter.write(outputStream);
    	excelWriter.close();

    	outputStream.close();
	}

	private Consumer<XSSFCell> getTotalPriceFunction = (cell) -> {
		int cellIndex = cell.getColumnIndex();
		XSSFRow row = cell.getRow();

		String quantiteCell = row.getCell(cellIndex - 1).getReference();
		String prixCell = row.getCell(cellIndex - 2).getReference();

		String formula = quantiteCell + " * " + prixCell;

		cell.setCellFormula(formula);
	};

	private Consumer<XSSFRow> getSommeTotaleFunction = (bottomRow) -> {
		int columnIndex = 3;
		XSSFCell sommeCell = bottomRow.createCell(columnIndex);

		XSSFSheet sheet = sommeCell.getSheet();
		String topCell = sheet.getRow(XSLXFileGenerator.HEADER_SIZE).getCell(columnIndex).getReference();
		String justAboveCell = sheet.getRow(sommeCell.getRowIndex() - 1).getCell(columnIndex).getReference();

		String formula = String.format("SOMME(%s:%s)", topCell, justAboveCell);
		sommeCell.setCellFormula(formula);
		sommeCell.setCellType(CellType.FORMULA);// FIXME STILL CAUSES STRANGE FORMULA BUG
	};
}
