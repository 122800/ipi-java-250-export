package com.example.demo.service.export.internal;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.example.demo.dto.ClientDTO;

public class CSVFileGenerator {

	public final String SEPARATOR = ";";
	public final String ESCAPE_QUOTE = "\"";

	private List<String> headers = new ArrayList<>();
	private List<Function<ClientDTO, Object>> functions = new ArrayList<>();

	private String escapeCSVChars(String string) {
		string.replace(ESCAPE_QUOTE, ESCAPE_QUOTE + ESCAPE_QUOTE);
		if(string.contains(SEPARATOR)) {
			string = ESCAPE_QUOTE + string + ESCAPE_QUOTE;
		}
		return string;
	}

	public void addColumn(String headerName, Function<ClientDTO, Object> function) {
		headers.add(headerName);
		functions.add(function);
	}

	public void writeCSVFile(Writer printWriter, List<ClientDTO> clients) throws IOException {
		for(String header : headers) {
    		printWriter.write(header + SEPARATOR);
    	}
		
    	for (ClientDTO client : clients) {
    		printWriter.write("\n");

    		for(Function<ClientDTO, Object> function : functions) {
    			printWriter.write(escapeCSVChars(function.apply(client).toString()) + SEPARATOR);
        	}
         }
	}
}