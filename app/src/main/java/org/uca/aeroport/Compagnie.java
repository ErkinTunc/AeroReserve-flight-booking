package org.uca.aeroport;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import java.time.ZonedDateTime;

public class Compagnie {

    private String name;

    private Set<Vol> vols = new HashSet<>(); // Hashset evite d'ajouter plusieurs fois le meme vol

    private int prochainNumero = 1;

    // ------------------- Constructors ------------------

    public Compagnie() {
    }

    // ------------------- Methods ------------------

    /**
     * Creer un vol avec un numero specifie
     * 
     * @param numero : le numero du vol (ex: "AF123")
     */
    public Vol creerVol(String numero,
            ZonedDateTime dateHeureDepart,
            ZonedDateTime dateHeureArrivee,
            Aeroport aeroportDepart,
            Aeroport aeroportArrivee) {

        // Validation des paramètres
        if (numero == null || dateHeureDepart == null || dateHeureArrivee == null
                || aeroportDepart == null || aeroportArrivee == null) {
            throw new IllegalArgumentException("Les informations du vol ne peuvent pas etre nulles");
        }

        if (contientNumero(numero)) {
            throw new IllegalArgumentException("Un vol avec ce numero existe deja dans cette compagnie");
        }

        Vol vol = new Vol(numero);

        vol.setDateHeureDepart(dateHeureDepart);
        vol.setDateHeureArrivee(dateHeureArrivee);
        vol.setDepart(aeroportDepart);
        vol.setArrivee(aeroportArrivee);

        this.addVol(vol);

        return vol;
    }

    /**
     * Cree un vol associe a un trajet
     */
    public Vol creerVol(String numero,
            ZonedDateTime dateHeureDepart,
            ZonedDateTime dateHeureArrivee,
            Trajet trajet) {

        if (numero == null || numero.isBlank()
                || dateHeureDepart == null
                || dateHeureArrivee == null
                || trajet == null) {
            throw new IllegalArgumentException("Les informations du vol ne peuvent pas etre nulles");
        }

        if (contientNumero(numero)) {
            throw new IllegalArgumentException("Un vol avec ce numero existe deja dans cette compagnie");
        }

        Vol vol = new Vol(numero);
        vol.setDateHeureDepart(dateHeureDepart);
        vol.setDateHeureArrivee(dateHeureArrivee);
        vol.setTrajet(trajet);

        this.addVol(vol);

        return vol;
    }

    /**
     * Creer un vol avec un numero genere automatiquement
     */
    public Vol creerVol(
            ZonedDateTime dateDepart,
            ZonedDateTime dateHeureArrivee,
            Aeroport aeroportDepart,
            Aeroport aeroportArrivee) {

        String numero = genererNumero();

        return creerVol(
                numero,
                dateDepart,
                dateHeureArrivee,
                aeroportDepart,
                aeroportArrivee);
    }

    private String genererNumero() {
        String numero;

        do {
            numero = "VOL-" + prochainNumero++;
        } while (contientNumero(numero));

        return numero;
    }

    private boolean contientNumero(String numero) {
        return this.vols.stream()
                .anyMatch(v -> v.getNumero().equals(numero));
    }

    protected boolean contientNumeroDeVol(Vol vol) {
        if (vol == null) {
            return false;
        }

        return this.vols.stream()
                .anyMatch(v -> v != vol && v.getNumero().equals(vol.getNumero()));
    }

    // ------------------- Getters and Setters ------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Vol> getVols() {
        return Collections.unmodifiableSet(vols);
    }

    protected void setVols(Collection<Vol> vols) {
        for (Vol v : this.vols) {
            v.setCompagnieWithoutBidirectional(null);
        }

        this.vols = new HashSet<>();

        if (vols != null) {
            for (Vol v : vols) {
                this.vols.add(v);
                v.setCompagnieWithoutBidirectional(this);
            }
        }
    }

    public void addVol(Vol vol) {
        if (vol == null) {
            return;
        }

        if (vol.getCompagnie() != this && contientNumero(vol.getNumero())) {
            throw new IllegalArgumentException("Un vol avec ce numero existe deja dans cette compagnie");
        }

        vol.setCompagnie(this);
    }

    public void removeVol(Vol vol) {
        if (vol == null) {
            return;
        }

        if (this.equals(vol.getCompagnie())) {
            vol.setCompagnie(null);
        }
    }

    protected void setVolsWithoutBidirectional(Collection<Vol> vols) {
        this.vols = new HashSet<>();

        if (vols != null) {
            this.vols.addAll(vols);
        }
    }

    protected void addVolWithoutBidirectional(Vol vol) {
        this.vols.add(vol);
    }

    protected void removeVolWithoutBidirectional(Vol vol) {
        this.vols.remove(vol);
    }
}
