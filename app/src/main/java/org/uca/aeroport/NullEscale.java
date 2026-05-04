package org.uca.aeroport;

import java.util.Date;

// Null Object : représente "pas d'escale " au lieu d'utiliser null


public final class NullEscale extends Escale{

    private static final NullEscale INSTANCE = new NullEscale();

    private NullEscale (){
        super((Date),null, null, null);
    }

    public static NullEscale getInstance(){ return INSTANCE; }

}