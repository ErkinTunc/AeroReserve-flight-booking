package org.uca.reservation;

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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMoyenPaiement() {
        return moyenPaiement;
    }

    public void setMoyenPaiement(String moyenPaiement) {
        this.moyenPaiement = moyenPaiement;
    }

    public int getFidelitePoints() {
        return fidelitePoints;
    }

    public void setFidelitePoints(int fidelitePoints) {
        this.fidelitePoints = fidelitePoints;
    }

    // Vue non modifiable des réservations
    public List<Reservation> getReservations() {
        return Collections.unmodifiableList(reservations);
    }

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
}