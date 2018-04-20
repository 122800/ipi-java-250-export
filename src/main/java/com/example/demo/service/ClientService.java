package com.example.demo.service;

import com.example.demo.dto.ClientDTO;
import com.example.demo.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientMapper clientMapper;
    
    public ClientDTO findById(Long id) {
    	return clientMapper.map(
			clientRepository.findById(id).orElseThrow(() ->
	        new IllegalArgumentException("Client inconnu: " + id)
		));
    }

    public List<ClientDTO> findAllClients() {
        return clientRepository.findAll().stream().map(clientMapper::map).collect(toList());
    }
    
}
