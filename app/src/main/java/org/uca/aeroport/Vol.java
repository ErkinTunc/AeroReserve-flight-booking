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

    private ZonedDateTime dateHeureDepart;
    private ZonedDateTime dateHeureArrivee;

    private Trajet trajet;

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
        if (this.dateHeureDepart != null && this.dateHeureArrivee != null) {
            return Duration.between(this.dateHeureDepart, this.dateHeureArrivee);
        }
        return null;
    }

    private void verifierDatesCoherentes() {
        if (dateHeureDepart != null
                && dateHeureArrivee != null
                && dateHeureArrivee.isBefore(dateHeureDepart)) {
            throw new IllegalArgumentException("La date d'arrivee doit etre apres la date de depart");
        }
    }

    public ZonedDateTime dateHeureArriveeEtape(EtapeTrajet etape) {
        if (etape == null || etape.getDecalageArrivee() == null) {
            throw new IllegalArgumentException("L'etape ou son decalage d'arrivee est obligatoire");
        }
        return dateHeureDepart.plus(etape.getDecalageArrivee());
    }

    public ZonedDateTime dateHeureDepartEtape(EtapeTrajet etape) {
        if (etape == null || etape.getDecalageDepart() == null) {
            throw new IllegalArgumentException("L'etape ou son decalage de depart est obligatoire");
        }
        return dateHeureDepart.plus(etape.getDecalageDepart());
    }

    // ------------------- Getters and Setters ------------------

    public UUID getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public Aeroport getDepart() {
        return depart;
    }

    public void setDepart(Aeroport depart) {
        if (depart == null) {
            throw new IllegalArgumentException("L'aeroport de depart est obligatoire");
        }
        this.depart = depart;
    }

    public Aeroport getArrivee() {
        return arrivee;
    }

    public void setArrivee(Aeroport arrivee) {
        if (arrivee == null) {
            throw new IllegalArgumentException("L'aeroport d'arrivee est obligatoire");
        }
        this.arrivee = arrivee;
    }

    public ZonedDateTime getDateHeureDepart() {
        return dateHeureDepart;
    }

    public void setDateHeureDepart(ZonedDateTime dateHeureDepart) {
        if (dateHeureDepart == null) {
            throw new IllegalArgumentException("La date de depart est obligatoire");
        }
        this.dateHeureDepart = dateHeureDepart;
        verifierDatesCoherentes();
    }

    public ZonedDateTime getDateHeureArrivee() {
        return dateHeureArrivee;
    }

    public void setDateHeureArrivee(ZonedDateTime dateHeureArrivee) {
        if (dateHeureArrivee == null) {
            throw new IllegalArgumentException("La date d'arrivee est obligatoire");
        }
        this.dateHeureArrivee = dateHeureArrivee;
        verifierDatesCoherentes();
    }

    public Trajet getTrajet() {
        return trajet;
    }

    public void setTrajet(Trajet trajet) {
        if (trajet == null) {
            throw new IllegalArgumentException("Le trajet du vol est obligatoire");
        }
        this.trajet = trajet;
    }

    public List<Escale> getEscales() {
        return Collections.unmodifiableList(escales);
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
        return Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
