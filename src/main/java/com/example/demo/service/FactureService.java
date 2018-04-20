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
    
    public List<FactureDTO> findByClient(Long id) {
    	return factureRepository.findByClient(id).stream().map(factureMapper::map).collect(toList());
    }

    public List<FactureDTO> findAllFactures() {
        return factureRepository.findAll().stream().map(factureMapper::map).collect(toList());
    }
    
    public FactureDTO findById(Long id) {
    	return factureMapper.map(
    		factureRepository.findById(id).orElseThrow(() ->
    			new IllegalArgumentException("Facture inconnue: " + id)
    	));
    }
}
