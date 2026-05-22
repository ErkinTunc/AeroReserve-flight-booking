package org.uca.reservation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class Client{

    private String nom;

    private String mail; 

    private String moyenPaiement;

    private int fidelitePoints;


    //Réservation effecuées par ce Client

    private final List<Reservation> reservations = new ArrayList<>();

    public String nom(){ return nom; }

    public void setNom(String nom){ this.nom = nom ;}

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



    /*
      Retourne une vue non modifiable des réservations du client.
     */
    public List<Reservation> getReservations() {
        return Collections.unmodifiableList(reservations);
    }

    /*
      Ajoute une réservation à la liste du client
        en maintenant la double navigabilité.
     */
    public void addReservation(Reservation reservation) {
        if (reservation == null || reservations.contains(reservation)) {
            return;
        }
        reservations.add(reservation);
        reservation.setClientInternal(this);
    }

    /*
        Méthode interne appelée par Reservation.setClient
       pour éviter les boucles infinies.
     */
    void addReservationInternal(Reservation reservation) {
        if (!reservations.contains(reservation)) {
            reservations.add(reservation);
        }
    }

    /*
      Retire une réservation de la liste.
     */
    public void removeReservation(Reservation reservation) {
        if (reservations.remove(reservation)) {
            reservation.setClientInternal(null);
        }
    }



}
