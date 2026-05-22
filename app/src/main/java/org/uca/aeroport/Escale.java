package org.uca.aeroport;

import java.util.Date;

public class Escale extends Etape {

    private Aeroport aeroport;

    public Escale(Date depart, Date arrivee, Aeroport aeroport) {
        super(depart, arrivee);

        if (aeroport == null) {
            throw new IllegalArgumentException("L'aeroport de l'escale est obligatoire");
        }

        this.aeroport = aeroport;
    }

    public Aeroport getAeroport() {
        return aeroport;
    }

    public void setAeroport(Aeroport aeroport) {
        if (aeroport == null) {
            throw new IllegalArgumentException("L'aeroport de l'escale est obligatoire");
        }

        this.aeroport = aeroport;
    }

}