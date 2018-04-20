package com.example.demo.dto;

import java.util.HashSet;
import java.util.Set;

import com.example.demo.entity.Client;

/**
 * Created by Kayne on 09/04/2018.
 */
public class ClientDTO {

    private Long id;

    private String prenom;

    private String nom;
    
    private Integer nb_weapons;
    public Integer getNb_weapons() {
		return nb_weapons;
	}

	public void setNb_weapons(Integer nb_weapons) {
		this.nb_weapons = nb_weapons;
	}

	public String getFav_weapon() {
		return fav_weapon;
	}

	public void setFav_weapon(String fav_weapon) {
		this.fav_weapon = fav_weapon;
	}

	public Set<Client> getVictims() {
		return victims;
	}

	public void setVictims(Set<Client> victims) {
		this.victims = victims;
	}

	private String fav_weapon;
    private Set<Client> victims = new HashSet<>();

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
