package com.example.demo.service;

import com.example.demo.entity.Article;
import com.example.demo.entity.Client;
import com.example.demo.entity.Facture;
import com.example.demo.entity.LigneFacture;
import com.example.demo.exception.MurdererException;

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
    	Client client1 = new Client("Petri;llo", "Alexandre");
    	Client client2 = new Client("Dupont", "Jérome");
    	
    	Client client3 = new Client("Guy", "Random");
    	Client client4 = new Client("Passerby", "Hapless");
    	Client client5 = new Client("Damage", "Collateral");
    	
    	try {
    		client1.setNb_weapons(4);
    		client1.setFav_weapon("Katana");
    		client1.addVictim(client2);
    		client1.addVictim(client3);
    		client1.addVictim(client4);
    		
    		client2.addVictim(client5);

    		client3.setNb_weapons(32);
    		client3.setFav_weapon("Halberd");
			
		} catch (MurdererException e) {
			System.out.println("The murderers are complaining: " + e.getMessage());
		}
    	
    	em.persist(client1);
    	em.persist(client2);
    	em.persist(client3);
    	em.persist(client4);
    	em.persist(client5);

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
