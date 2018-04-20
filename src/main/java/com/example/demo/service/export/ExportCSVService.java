package com.example.demo.service.export;

import com.example.demo.dto.ClientDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
public class ExportCSVService {
	
	public final String SEPARATOR = ";";
	
	private List<String> headers = new ArrayList<>();
	private List<Function<ClientDTO, Object>> functions = new ArrayList<>();
	
    public void export(Writer printWriter, List<ClientDTO> clients) throws IOException {
    	
    	addColumn("Nom", ClientDTO::getNom);
    	addColumn("Prenom", ClientDTO::getPrenom);
    	addColumn("Nombre d'armes", ClientDTO::getNb_weapons);
    	addColumn("Nom de l'arme favorite", ClientDTO::getFav_weapon);
    	
    	// maybe get a list of the names of each of the victims?
    	addColumn("Noms des victimes",
    			(C) -> C.getVictims().stream().map(
    					(V) -> V.getNom()
    				).reduce("", (a, b) -> a + (a.length() > 0 ? ", " : "") + b)
    			);
    	
    	writeCSVFile(printWriter, clients);
    	
    	printWriter.close();
    }
    
    private void addColumn(String headerName, Function<ClientDTO, Object> function) {
		headers.add(headerName);
		functions.add(function);
	}
	private void writeCSVFile(Writer printWriter, List<ClientDTO> clients) throws IOException {
		for(String header : headers) {
    		printWriter.write(header + SEPARATOR);
    	}
    	
    	for (ClientDTO client : clients) {
    		
    		printWriter.write("\n");
    		
    		for(Function<ClientDTO, Object> function : functions) {
    				
    			printWriter.write(
    				escapeCSVChars(function.apply(client).toString())
    				+ SEPARATOR
    			);
        	}
         }
	}

	private String escapeCSVChars(String string) {
		string.replace("\"", "\"\"");
		if(string.contains(SEPARATOR)) {
			string = "\"" + string + "\"";
		}
		return string;
	}
}
