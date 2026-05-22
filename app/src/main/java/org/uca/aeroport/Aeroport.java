package org.uca.aeroport;

/*
 * Represents an airport identified by a code, a name and a city (Ville).
 */
public class Aeroport {

    private String code;
    private String nom;
    private Ville ville;

    // ------------------- Constructors ------------------

    public Aeroport() {
        // default constructor
    }

    public Aeroport(String code, String nom, Ville ville) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Airport code is required");
        }
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Airport name is required");
        }
        if (ville == null) {
            throw new IllegalArgumentException("Airport city is required");
        }
        this.code = code;
        this.nom = nom;
        this.ville = ville;
    }

    // ------------------- Getters and Setters ------------------

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Airport code is required");
        }
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom de l'aeroport est obligatoire");
        }
        this.nom = nom;
    }

    public Ville getVille() {
        return ville;
    }

    public void setVille(Ville ville) {
        if (ville == null) {
            throw new IllegalArgumentException("La ville de l'aeroport est obligatoire");
        }
        this.ville = ville;
    }

    // ------------------- Object methods ------------------

    @Override
    public String toString() {
        return code + " - " + nom + " (" + ville + ")";
    }
}