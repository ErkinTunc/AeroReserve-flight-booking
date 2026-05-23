package org.uca.reservation.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Client {

    private String nom;
    private String mail;
    private String moyenPaiement;
    private int fidelitePoints;

    // Réservations effectuées par ce Client (optionnel)
    private final List<Reservation> reservations = new ArrayList<>();

    // ----------------- Association management methods ----------------

    // Ajoute une réservation SANS appeler setClientInternal
    public void addReservation(Reservation reservation) {
        if (reservation == null || reservations.contains(reservation)) {
            return;
        }
        reservations.add(reservation);
    }

    // Retire une réservation
    public void removeReservation(Reservation reservation) {
        if (reservation == null) {
            return;
        }
        reservations.remove(reservation);
    }

    // -------------------- Getters et Setters ----------------

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom du client est obligatoire");
        }
        this.nom = nom;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        if (mail == null || mail.isBlank()) {
            throw new IllegalArgumentException("Le mail du client est obligatoire");
        }
        this.mail = mail;
    }

    public String getMoyenPaiement() {
        return moyenPaiement;
    }

    public void setMoyenPaiement(String moyenPaiement) {
        if (moyenPaiement == null || moyenPaiement.isBlank()) {
            throw new IllegalArgumentException("Le moyen de paiement est obligatoire");
        }
        this.moyenPaiement = moyenPaiement;
    }

    public int getFidelitePoints() {
        return fidelitePoints;
    }

    public void setFidelitePoints(int fidelitePoints) {
        if (fidelitePoints < 0) {
            throw new IllegalArgumentException("Les points de fidélité ne peuvent pas être négatifs");
        }
        this.fidelitePoints = fidelitePoints;
    }

    // Vue non modifiable des réservations
    public List<Reservation> getReservations() {
        return Collections.unmodifiableList(reservations);
    }

}