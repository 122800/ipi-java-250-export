package com.example.demo.service;

import com.example.demo.dto.ClientDTO;
import com.example.demo.entity.Client;
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
    
    public Client findById(Long id) {
        return clientRepository.findById(id).orElseThrow(() ->
		        new IllegalArgumentException("Client inconnu: " + id)
			);
    }
    public ClientDTO findByIdDTO(Long id) {
    	return clientMapper.map(findById(id));
    }

    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }
    public List<ClientDTO> findAllClientsDTO() {
        return findAllClients().stream().map(clientMapper::map).collect(toList());
    }
    
}
