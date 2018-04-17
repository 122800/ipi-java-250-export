package com.example.demo.repository;

import com.example.demo.entity.Client;
import com.example.demo.entity.Facture;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactureRepository extends JpaRepository<Facture, Long> {

	public List<Facture> findAllByClient(Client client);
	
}