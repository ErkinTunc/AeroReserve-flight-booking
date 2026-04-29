package org.uca.aeroport;

import java.util.ArrayList;
import java.util.Collection;

public class Compagnie {

    private String name;

    private Collection<Vol> vols = new ArrayList<>();

    public Compagnie() {
    }

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

        this.vols = vols;

        if (this.vols != null) {
            for (Vol v : this.vols) {
                v.setCompagnieWithoutBidirectional(this);
            }
        }
    }

    public void addVol(Vol vol) {
        vol.setCompagnieWithoutBidirectional(this);
        this.vols.add(vol);
    }

    public void removeVol(Vol vol) {
        vol.setCompagnieWithoutBidirectional(null);
        this.vols.remove(vol);
    }

    protected void setVolsWithoutBidirectional(Collection<Vol> vols) {
        this.vols = vols;
    }

    protected void addVolWithoutBidirectional(Vol vol) {
        this.vols.add(vol);
    }

    protected void removeVolWithoutBidirectional(Vol vol) {
        this.vols.remove(vol);
    }
}
