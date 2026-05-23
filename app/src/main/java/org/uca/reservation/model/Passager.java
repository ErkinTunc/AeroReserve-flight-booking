package org.uca.reservation.model;

public class Passager {

    private final String nom;
    private final String numeroPasseport;
    private final int age;
    private String telephone;

    // -------------------- Constructeur -------------------- //

    public Passager(String nom, String numeroPasseport, int age, String telephone) {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom du passager est obligatoire");
        }
        if (numeroPasseport == null || numeroPasseport.isBlank()) {
            throw new IllegalArgumentException("Le numéro de passeport est obligatoire");
        }
        if (age < 0) {
            throw new IllegalArgumentException("L'âge du passager ne peut pas être négatif");
        }

        this.nom = nom;
        this.numeroPasseport = numeroPasseport;
        this.age = age;
        this.telephone = telephone;
    }

    // -------------------- Getters et Setters -------------------- //

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
