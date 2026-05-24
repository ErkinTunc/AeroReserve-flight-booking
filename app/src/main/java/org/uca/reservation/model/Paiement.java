package org.uca.reservation.model;

import java.time.ZonedDateTime;

public class Paiement {

    // ---------------------------------- Enum -------------------- //
    public enum StatutPaiement {
        EN_ATTENTE,
        DEBITE,
        REMBOURSE
    }

    // ------------------------------------------ Attributs ------------------- //
    private final double montant;
    private StatutPaiement statut;
    private ZonedDateTime dateDebit;
    private ZonedDateTime dateRemboursement;

    // ---------------------------------- Constructeur -------------------- //
    public Paiement(double montant) {
        if (montant < 0) {
            throw new IllegalArgumentException("Le montant du paiement ne peut pas être négatif");
        }

        this.montant = montant;
        this.statut = StatutPaiement.EN_ATTENTE;
    }

    // ----------------------------------------- Methodes ------------------- //

    public void debiter() {
        if (statut != StatutPaiement.EN_ATTENTE) {
            throw new IllegalStateException("Seul un paiement en attente peut être débité.");
        }

        this.statut = StatutPaiement.DEBITE;
        this.dateDebit = ZonedDateTime.now();
    }

    public void rembourser() {
        if (statut != StatutPaiement.DEBITE) {
            throw new IllegalStateException("Seul un paiement débité peut être remboursé.");
        }

        this.statut = StatutPaiement.REMBOURSE;
        this.dateRemboursement = ZonedDateTime.now();
    }

    // ---------------------------------- Getters -------------------- //

    public double getMontant() {
        return montant;
    }

    public StatutPaiement getStatut() {
        return statut;
    }

    public ZonedDateTime getDateDebit() {
        return dateDebit;
    }

    public ZonedDateTime getDateRemboursement() {
        return dateRemboursement;
    }

    public boolean estDebite() {
        return statut == StatutPaiement.DEBITE;
    }

    public boolean estRembourse() {
        return statut == StatutPaiement.REMBOURSE;
    }
}
