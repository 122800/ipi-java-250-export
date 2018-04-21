package com.example.demo.service.export;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.ClientDTO;
import com.example.demo.service.export.internal.CSVFileGenerator;

@Service
public class ExportCSVService {

    public void export(Writer printWriter, List<ClientDTO> clients) throws IOException {

    	CSVFileGenerator csvWriter = new CSVFileGenerator();

    	csvWriter.addColumn("Nom", ClientDTO::getNom);
    	csvWriter.addColumn("Prenom", ClientDTO::getPrenom);
    	csvWriter.addColumn("Nombre d'armes", ClientDTO::getNb_weapons);
    	csvWriter.addColumn("Nom de l'arme favorite", ClientDTO::getFav_weapon);

    	// maybe get a list of the names of each of the victims?
    	csvWriter.addColumn("Noms des victimes",
    			(client) -> client.getVictims().stream()// get each victim
	    			.map((victime) -> victime.getNom())// get name from each victim
	    			.reduce("", (a, b) -> // concatenate list of names
	    				a
	    				+ (a.length() > 0 ? ", " : "")// add ', ' after first name in the list
	    				+ b)
    			);

    	csvWriter.writeCSVFile(printWriter, clients);

    	printWriter.close();
    }
}
