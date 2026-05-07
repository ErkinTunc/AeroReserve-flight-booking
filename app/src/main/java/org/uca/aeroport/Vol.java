package org.uca.aeroport;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import java.time.ZonedDateTime;

public class Vol {

    private String numero;

    private Aeroport depart;

    private Aeroport arrivee;

    private Compagnie compagnie;

    private ZonedDateTime dateDepart;

    private ZonedDateTime dateArrivee;

    // ------------------- Constructors ------------------

    public Vol() {
    }

    protected Vol(String numero) {
        this.numero = numero;
    }

    // ------------------- Methods ------------------

    public Duration obtenirDuree() {
        if (this.dateDepart != null && this.dateArrivee != null) {
            return Duration.between(this.dateDepart, this.dateArrivee);
        }
        return null;
    }

    // ------------------- Getters and Setters ------------------

    public ZonedDateTime getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(ZonedDateTime dateDepart) {
        this.dateDepart = dateDepart;
    }

    public ZonedDateTime getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(ZonedDateTime dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public Compagnie getCompagnie() {
        return compagnie;
    }

    public void setCompagnie(Compagnie compagnie) {
        if (compagnie != null) {
            compagnie.addVolWithoutBidirectional(this);
        }
        if (this.compagnie != null) {
            this.compagnie.removeVolWithoutBidirectional(this);
        }
        this.compagnie = compagnie;
    }

    protected void setCompagnieWithoutBidirectional(Compagnie compagnie) {
        this.compagnie = compagnie;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Aeroport getDepart() {
        return depart;
    }

    public void setDepart(Aeroport depart) {
        this.depart = depart;
    }

    public Aeroport getArrivee() {
        return arrivee;
    }

    public void setArrivee(Aeroport arrivee) {
        this.arrivee = arrivee;
    }

    // ------------------- Equals and HashCode ------------------

    @Override
    public boolean equals(Object obj) {
        try {
            return ((Vol) obj).getNumero().equals(this.numero);
        } catch (Exception e) {
            return false;
        }
    }
}
