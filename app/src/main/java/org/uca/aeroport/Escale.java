package org.uca.aeroport;

import java.util.Date;


public Escale extends Etape {
    
    private Aeroport aeroport;

    public  Escale (Date depart, Date arrivee, Aeroport aeroport){
        super(depart, arrivee);
        this.aeroport = aeroport;
    }

    public Aeroport getAeroport (){ return aeroport; }

    public void setAeroport(Aeroport aeroport){ this.aeroport = aeroport; }
    
}