package org.uca.aeroport;

public class Aeroport {

    private String nom;

    private String ville;

    public Aeroport() {
    }

    public Aeroport(String nom , String ville){
        this.nom = nom;
        this.ville = ville;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    @Override

    public String toString(){
        return nom + " " + ville;
    }
}
