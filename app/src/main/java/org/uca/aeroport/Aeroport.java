package org.uca.aeroport;

public class Aeroport {

    private String code;

    private String nom;

    private Ville ville;

    public Aeroport() {
    }

    public Aeroport(String code, String nom , Ville ville){
        this.code = code;
        this.nom = nom;
        this.ville = ville;
    }

    public String getCode(){ return code; }

    public void setCode(String code){ this.code = code; }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Ville getVille() {
        return ville;
    }

    public void setVille(Ville ville) {
        this.ville = ville;
    }

    @Override

    public String toString(){
        return code + " - " + nom + "(" + ville + ")";
    }
}
