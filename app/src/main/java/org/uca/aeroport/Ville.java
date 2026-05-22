package org.uca.aeroport;


public class Ville{

    private String nom;

    // ------------------- Constructors ------------------

    public Ville() { } 
    
    public Ville(String nom){
        this.nom = nom;
    }

    // ------------------- Getters and Setters ------------------

    public String getNom(){
        return nom;
    }
    
    public void setNom(String nom){
        this.nom = nom;
    }

    @Override
    public String toString(){
        return nom;
    }

}