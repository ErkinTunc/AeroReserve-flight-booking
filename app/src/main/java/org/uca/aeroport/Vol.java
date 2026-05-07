package org.uca.aeroport;

import java.time.Duration;

import java.time.ZonedDateTime;

import java.util.Objects;

public class Vol {

    private String numero;

    private Aeroport depart;

    private Aeroport arrivee;

    private Compagnie compagnie;

    private ZonedDateTime dateDepart;

    private ZonedDateTime dateArrivee;

    // ------------------- Constructors ------------------

    protected Vol() { // protected -> limits direct creation of Vol outside the package/subclasses
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
        if (this.compagnie == compagnie) {
            return;
        }

        if (this.compagnie != null) {
            this.compagnie.removeVolWithoutBidirectional(this);
        }

        this.compagnie = compagnie;

        if (compagnie != null) {
            compagnie.addVolWithoutBidirectional(this);
        }
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
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Vol)) {
            return false;
        }

        Vol other = (Vol) obj;
        return Objects.equals(this.numero, other.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }
}
