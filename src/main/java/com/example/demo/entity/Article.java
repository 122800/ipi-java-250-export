package com.example.demo.entity;

import javax.persistence.*;

/**
 * Created by Kayne on 09/04/2018.
 */
@Entity
public class Article {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column private String libelle;
    @Column private Double prix;
    
    public Article() {}
    public Article(String libelle, double prix) {
        setLibelle(libelle);
        setPrix(prix);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }
}
