package org.uca.aeroport;

import java.time.Duration;
import java.util.Date;


/*
    Classe de base qui va être commune aux éléments ayant un départ et une arrivée
    On pourrait l'utiliser pour factoriser du code si nécessaire
*/

public abstract class Etape{

    protected Date depart;

    protected Date arrivee;

    protected Etape(Date depart , Date arrivee){
        this.depart = depart;
        this.arrivee = arrivee;
    }

    public Date getDepart (){ return depart; }

    public void setDepart(Date depart){ this.depart = depart; }

    public Date getArrivee(){ return arrivee; }

    public void setArrivee(Date arrivee){ this.arrivee = arrivee; }

    // Duree dérivée = arrivee - depart

    public Duration getDuree(){
        if (depart == null || arrivee == null){
            return Duration.ZERO;
        }
        return Duration.ofMillis(arrivee.getTime() - depart.getTime());
    }
}