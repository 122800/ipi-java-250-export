package com.example.demo.service;

import com.example.demo.dto.FactureDTO;
import com.example.demo.entity.Client;
import com.example.demo.entity.Facture;
import com.example.demo.repository.FactureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class FactureService {

    @Autowired
    private FactureRepository factureRepository;
    @Autowired
    private FactureMapper factureMapper;
    
    public List<Facture> findByClient(Client client) {
    	return factureRepository.findByClient(client);
    }
    public List<FactureDTO> findByClientDTO(Client client) {
    	return findByClient(client).stream().map(factureMapper::map).collect(toList());
    }

    public List<Facture> findAllFactures() {
        return factureRepository.findAll();
    }
    public List<FactureDTO> findAllFacturesDTO() {
        return findAllFactures().stream().map(factureMapper::map).collect(toList());
    }

    public Facture findById(Long id) {
        return factureRepository.findById(id).orElseThrow(() ->
		        new IllegalArgumentException("Facture inconnue: " + id)
			);
    }
    public FactureDTO findByIdDTO(Long id) {
    	return factureMapper.map(findById(id));
    }
}
