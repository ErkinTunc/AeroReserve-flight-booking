package org.uca.aeroport;

public class Ville {

    private String nom;

    // ------------------- Constructors ------------------

    public Ville() {
    }

    public Ville(String nom) {
        setNom(nom);
    }

    // ------------------- Getters and Setters ------------------

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom de la ville est obligatoire");
        }

        this.nom = nom;
    }

    @Override
    public String toString() {
        return nom;
    }

}