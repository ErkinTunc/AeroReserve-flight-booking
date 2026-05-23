package org.uca.reservation.model;

public class Passager {

    private final String nom;
    private final String numeroPasseport;
    private final int age;
    private String telephone;

    public Passager(String nom, String numeroPasseport, int age, String telephone) {
        this.nom = nom;
        this.numeroPasseport = numeroPasseport;
        this.age = age;
        this.telephone = telephone;
    }

    // Getters pour les attributs non modifiables
    public String getNom() {
        return nom;
    }

    public String getNumeroPasseport() {
        return numeroPasseport;
    }

    public int getAge() {
        return age;
    }

    // Getter + Setter pour l'attribut modifiable
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
