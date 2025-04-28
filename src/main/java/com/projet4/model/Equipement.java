package com.projet4.model;

import com.projet4.model.enums.EtatEquipement;

import jakarta.persistence.*;

@Entity
public class Equipement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String numeroSerie;

    @Enumerated(EnumType.STRING)
    private EtatEquipement etat;

    private String laboratoire;

    // Constructeur sans argument requis par JPA
    public Equipement() {
    }

    // Constructeur avec tous les champs
    public Equipement(Long id, String type, String numeroSerie, EtatEquipement etat, String laboratoire) {
        this.id = id;
        this.type = type;
        this.numeroSerie = numeroSerie;
        this.etat = etat;
        this.laboratoire = laboratoire;
    }

    // Getters et Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public EtatEquipement getEtat() {
        return etat;
    }

    public void setEtat(EtatEquipement etat) {
        this.etat = etat;
    }

    public String getLaboratoire() {
        return laboratoire;
    }

    public void setLaboratoire(String laboratoire) {
        this.laboratoire = laboratoire;
    }
}
