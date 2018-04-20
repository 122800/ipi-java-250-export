package com.example.demo.service.export;

import com.example.demo.dto.ClientDTO;
import com.example.demo.service.export.internal.XSLXFileWriter;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import javax.servlet.ServletOutputStream;

@Service
public class ExportXLSXService {  

    public void export(OutputStream os, List<ClientDTO> clients) throws IOException {
    	
    	XSLXFileWriter excelWriter = new XSLXFileWriter();
        
        excelWriter.addColumn("Nom", ClientDTO::getNom);
    	excelWriter.addColumn("Prenom", ClientDTO::getPrenom);
    	excelWriter.addColumn("Nombre d'armes", ClientDTO::getNb_weapons);
    	excelWriter.addColumn("Nom de l'arme favorite", ClientDTO::getFav_weapon);
    	
    	// maybe get a list of the names of each of the victims?
    	excelWriter.addColumn("Noms des victimes",
    			(C) -> C.getVictims().stream().map(
    					(V) -> V.getNom()
    				).reduce("", (a, b) -> a + (a.length() > 0 ? ", " : "") + b)
    			);
    	
    	XSSFWorkbook workbook = excelWriter.generateXSLXFile(os, clients);
    	
    	workbook.write(os);
        workbook.close();

        os.close();
    }

	public void exportfacturesDUnClient(ServletOutputStream outputStream, ClientDTO client) {
		// TODO Auto-generated method stub
		
	}
}
