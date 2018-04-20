package com.example.demo.service.export;

import com.example.demo.dto.ClientDTO;
import com.example.demo.entity.Client;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;

@Service
public class ExportCSVService {
	
	public final String SEPARATOR = ";";
	
	private List<String> headers = new ArrayList<>();
	private List<Function<ClientDTO, Object>> functions = new ArrayList<>();
	
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

    public void export(Writer printWriter, List<ClientDTO> clients) throws IOException {
    	
    	addColumn("Nom", ClientDTO::getNom);
    	addColumn("Prenom", ClientDTO::getPrenom);
    	
    	writeCSVFile(printWriter, clients);
    }

	private String escapeCSVChars(String string) {
		string.replace("\"", "\"\"");
		if(string.contains(SEPARATOR)) {
			string = "\"" + string + "\"";
		}
		return string;
	}
}
