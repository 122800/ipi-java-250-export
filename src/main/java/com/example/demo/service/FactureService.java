package com.example.demo.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.FactureDTO;
import com.example.demo.repository.FactureRepository;

@Service
@Transactional
public class FactureService {

    @Autowired
    private FactureRepository factureRepository;
    @Autowired
    private FactureMapper factureMapper;

    public List<FactureDTO> findByClientId(Long id) {
    	return factureRepository.findByClientId(id).stream().map(factureMapper::map).collect(toList());
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