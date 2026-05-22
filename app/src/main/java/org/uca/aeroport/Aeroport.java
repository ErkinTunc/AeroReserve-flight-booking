package org.uca.aeroport;

public class Aeroport {

    private String code;

    private String nom;

    private Ville ville;

    // ------------------- Constructors ------------------
    public Aeroport() {
    }

<<<<<<< HEAD
    public Aeroport(String code, String nom , Ville ville){
        this.code = code;
        this.nom = nom;
        this.ville = ville;
    }

    public String getCode(){ return code; }

    public void setCode(String code){ this.code = code; }

=======
    // ------------------- Methods ------------------

    // ------------------- Getters and Setters ------------------
>>>>>>> origin/main
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

<<<<<<< HEAD
    public void setVille(Ville ville) {
=======
    public void setVille(String ville) {
        if (ville == null || ville.isBlank()) {
            throw new IllegalArgumentException("La ville de l'aeroport est obligatoire");
        }
>>>>>>> origin/main
        this.ville = ville;
    }

    @Override

    public String toString(){
        return code + " - " + nom + "(" + ville + ")";
    }
}
