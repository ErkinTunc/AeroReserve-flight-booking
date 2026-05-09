package org.uca.aeroport;

public class Aeroport {

    private String nom;

    private String ville;

    // ------------------- Constructors ------------------
    public Aeroport() {
    }

    // ------------------- Methods ------------------

    // ------------------- Getters and Setters ------------------
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom de l'aeroport est obligatoire");
        }
        this.nom = nom;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        if (ville == null || ville.isBlank()) {
            throw new IllegalArgumentException("La ville de l'aeroport est obligatoire");
        }
        this.ville = ville;
    }
}
