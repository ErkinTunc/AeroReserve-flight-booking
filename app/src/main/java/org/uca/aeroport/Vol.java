package org.uca.aeroport;

import java.time.Duration;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Vol {

    private final UUID id;
    private final String numero;

    private Aeroport depart;

    private Aeroport arrivee;

    private Compagnie compagnie;

    private ZonedDateTime dateDepart;

    private ZonedDateTime dateArrivee;

    private List<Escale> escales = new ArrayList<>();

    // ------------------- Constructors ------------------

    protected Vol(String numero) { // protected -> limits direct creation of Vol outside the package/subclasses
        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException("Le numero du vol est obligatoire");
        }

        this.id = UUID.randomUUID(); // identifiant technique unique
        this.numero = numero;
    }
    // ------------------- Methods ------------------

    public void addEscale(Escale escale) {
        if (escale == null) {
            throw new IllegalArgumentException("Escale obligatoire");
        }
        escales.add(escale);
    }

    public void removeEscale(Escale escale) {
        escales.remove(escale);
    }

    public Duration obtenirDuree() {
        if (this.dateDepart != null && this.dateArrivee != null) {
            return Duration.between(this.dateDepart, this.dateArrivee);
        }
        return null;
    }

    private void verifierDatesCoherentes() {
        if (dateDepart != null && dateArrivee != null && dateArrivee.isBefore(dateDepart)) {
            throw new IllegalArgumentException("La date d'arrivee doit etre apres la date de depart");
        }
    }

    // ------------------- Getters and Setters ------------------

    public UUID getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public List<Escale> getEscales() {
        return Collections.unmodifiableList(escales);
    }

    public Aeroport getDepart() {
        return depart;
    }

    public ZonedDateTime getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(ZonedDateTime dateDepart) {
        this.dateDepart = dateDepart;
        verifierDatesCoherentes();
    }

    public ZonedDateTime getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(ZonedDateTime dateArrivee) {
        this.dateArrivee = dateArrivee;
        verifierDatesCoherentes();
    }

    public Compagnie getCompagnie() {
        return compagnie;
    }

    public void setCompagnie(Compagnie compagnie) {
        if (this.compagnie == compagnie) {
            return;
        }

        if (compagnie != null && compagnie.contientNumeroDeVol(this)) {
            throw new IllegalArgumentException("Un vol avec ce numero existe deja dans cette compagnie");
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
        if (this == obj)
            return true;
        if (!(obj instanceof Vol))
            return false;
        Vol other = (Vol) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
