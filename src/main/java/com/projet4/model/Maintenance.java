    package com.projet4.model;

    import jakarta.persistence.*;
    import java.time.LocalDate;

    @Entity
    @Table(name = "maintenances")
    public class Maintenance {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "equipement_id", nullable = false)
        private Equipement equipement;

        private LocalDate date;

        @Column(length = 1000)
        private String actions;

        // Constructeur sans argument requis par JPA
        public Maintenance() {
        }

        // Constructeur avec tous les champs
        public Maintenance(Long id, Equipement equipement, LocalDate date, String actions) {
            this.id = id;
            this.equipement = equipement;
            this.date = date;
            this.actions = actions;
        }

        // Getters et Setters

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Equipement getEquipement() {
            return equipement;
        }

        public void setEquipement(Equipement equipement) {
            this.equipement = equipement;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public String getActions() {
            return actions;
        }

        public void setActions(String actions) {
            this.actions = actions;
        }
    }
