package org.uca.aeroport;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import java.time.ZonedDateTime;

public class Compagnie {

    private String name;

    private Set<Vol> vols = new HashSet<>(); // Hashset evite d'ajouter plusieurs fois le meme vol

    // ------------------- Constructors ------------------

    public Compagnie() {
    }

    // ------------------- Methods ------------------

    public Vol creerVol(String numero,
            ZonedDateTime dateDepart,
            ZonedDateTime dateArrivee,
            Aeroport aeroportDepart,
            Aeroport aeroportArrivee) {

        Vol vol = new Vol(numero);

        vol.setDateDepart(dateDepart);
        vol.setDateArrivee(dateArrivee);
        vol.setDepart(aeroportDepart);
        vol.setArrivee(aeroportArrivee);

        this.addVol(vol);

        return vol;
    }

    // ------------------- Getters and Setters ------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Vol> getVols() {
        return vols;
    }

    public void setVols(Collection<Vol> vols) {
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
