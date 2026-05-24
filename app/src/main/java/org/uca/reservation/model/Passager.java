package org.uca.reservation.model;

public class Passager {

    private final String nom;
    private final String prenom;
    private final String numeroPasseport;
    private final int age;
    private String telephone;

    // -------------------- Constructeur -------------------- //

    public Passager(String nom, String prenom, int age, String numeroPasseport) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom est obligatoire.");
        }
        if (prenom == null || prenom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prenom est obligatoire.");
        }
        if (age < 0) {
            throw new IllegalArgumentException("L'age ne peut pas etre negatif.");
        }
        if (numeroPasseport == null || numeroPasseport.trim().isEmpty()) {
            throw new IllegalArgumentException("Le numero de passeport est obligatoire.");
        }

        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.numeroPasseport = numeroPasseport;
    }

    // -------------------- Getters et Setters -------------------- //

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public int getAge() {
        return age;
    }

    public String getNumeroPasseport() {
        return numeroPasseport;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        if (telephone == null || telephone.trim().isEmpty()) {
            throw new IllegalArgumentException("Le telephone est obligatoire.");
        }
        this.telephone = telephone;
    }
}
