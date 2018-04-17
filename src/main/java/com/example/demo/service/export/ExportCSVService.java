package com.example.demo.service.export;

import com.example.demo.dto.ClientDTO;
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

@Service
public class ExportCSVService {
	
	public final String SEPARATOR = ";"; 

    public void export(Writer printWriter, List<ClientDTO> clients) throws IOException {
    	
    	Class<? extends ClientDTO> classType = clients.get(0).getClass();
    	Method[] classMethods = classType.getDeclaredMethods();
    	
    	HashMap<String, Method> getters = new HashMap<>(); 
    	for(Method m : classMethods) {
    		String name = m.getName();
    		if(name.startsWith("get")) {
    			getters.put(name.substring(3), m);
    		}
    	}
    	
    	for(Entry<String, Method> set : getters.entrySet()) {
    		printWriter.write(set.getKey() + SEPARATOR);// write headers
    	}
    	
    	for (ClientDTO client : clients) {
    		
    		printWriter.write("\n");
    		
    		for(Entry<String, Method> set : getters.entrySet()) {
    			try {
					String gottenVar = set.getValue().invoke(client, new Object[]{}).toString();
					printWriter.write(gottenVar + ";");
				} catch (Exception e) {
					// blub
				}
        	}
         }
    }
}
