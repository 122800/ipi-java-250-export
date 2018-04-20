package com.example.demo.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.validator.constraints.UniqueElements;

import com.example.demo.exception.MurdererException;

/**
 * Created by Kayne on 09/04/2018.
 */
@Entity
public class Client {

    public Client() {};
    public Client(String nom, String prenom) {
    	setNom(nom);
    	setPrenom(prenom);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String prenom;

    @Column
    private String nom;
    
    @Column private Integer nb_weapons = 1;
    @Column private String fav_weapon = "Dagger";
    @ManyToMany @UniqueElements private Set<Client> victims = new HashSet<>();
    
    public Integer getNb_weapons() {
		return nb_weapons;
	}
	/**
	 * 
	 * @param new_amount
	 * @throws MurdererException - if trying to reduce the amount of weapons.
	 */
    public void setNb_weapons(Integer new_amount) throws MurdererException {
		if(new_amount >= nb_weapons) {
			nb_weapons = new_amount;			
		} else {
			throw new MurdererException("You will not take my weapons away from me."); 
		}
	}
	public String getFav_weapon() {
		return fav_weapon;
	}
	/**
	 * 
	 * @param new_fav
	 * @throws MurdererException - if trying to set an empty name.
	 */
	public void setFav_weapon(String new_fav) throws MurdererException {
		if(new_fav.length() > 0) {
			fav_weapon = new_fav;
		} else {
			throw new MurdererException("I must always find a favorite among my pretties.");
		}
	}
	public Set<Client> getVictims() {
		return victims;
	}
	/**
	 * Dummy function. Will always throw an exception.
	 * @param victims
	 * @throws MurdererException
	 */
	public void setVictims(Set<Client> victims) throws MurdererException {
		throw new MurdererException("I can only add new victims. The victims of the past may never change or be revived.");
	}
	/**
	 * 
	 * @param new_victim
	 * @throws MurdererException - if the victim already exists.
	 */
	public void addVictim(Client new_victim) throws MurdererException {
		if(!victims.contains(new_victim)) {
			victims.add(new_victim);
		} else {
			throw new MurdererException("That person is already taken care of.");
		}
	}
	
	
	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
