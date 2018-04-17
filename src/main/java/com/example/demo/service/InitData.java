package com.example.demo.service;

import com.example.demo.entity.Article;
import com.example.demo.entity.Client;
import com.example.demo.entity.Facture;
import com.example.demo.entity.LigneFacture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
@Transactional
public class InitData {

    @Autowired
    private EntityManager em;

    public void insertTestData() {
    	Client client1 = new Client("PETRI;LLO", "Alexandre");
    	Client client2 = new Client("Dupont", "Jérome");
    	em.persist(client1);
    	em.persist(client2);

        Article article1 = new Article("Carte mère ASROCK 2345", 79.90);
        Article article2 = new Article("Clé USB", 9.90);
        em.persist(article1);
        em.persist(article2);

        {
            Facture facture = new Facture(client1);
            em.persist(facture);
            em.persist(new LigneFacture(article1, facture, 1));
        }
        {
            Facture facture = new Facture(client1);
            em.persist(facture);
            em.persist(new LigneFacture(article1, facture, 1));
            em.persist(new LigneFacture(article2, facture, 5));
        }
        {
            Facture facture = new Facture(client2);
            em.persist(facture);
            em.persist(new LigneFacture(article2, facture, 5));
        }
    }
    
}
