package org.uca.aeroport;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NullEscaleTest {

    @Test
    public void getInstanceRetourneToujoursLaMemeInstance() {
        NullEscale e1 = NullEscale.getInstance();
        NullEscale e2 = NullEscale.getInstance();

        assertSame(e1, e2);
    }

    @Test
    public void nullEscaleEstUneEscaleValide() {
        Escale escale = NullEscale.getInstance();
        assertNotNull(escale);
        // Tu peux ajouter des assertions si tu définis un comportement particulier
    }
}
