package org.uca.reservation.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PaiementTest {

    @Test
    public void constructeurDoitInitialiserPaiementEnAttente() {
        Paiement paiement = new Paiement(100.0);

        assertEquals(100.0, paiement.getMontant(), 0.0001);
        assertEquals(Paiement.StatutPaiement.EN_ATTENTE, paiement.getStatut());
        assertTrue(paiement.getDateDebit() == null);
        assertTrue(paiement.getDateRemboursement() == null);
    }

    @Test
    public void constructeurDoitRefuserMontantNegatif() {
        assertThrows(IllegalArgumentException.class,
                () -> new Paiement(-1.0));
    }

    @Test
    public void debiterDoitPasserLePaiementEnDebite() {
        Paiement paiement = new Paiement(100.0);

        paiement.debiter();

        assertEquals(Paiement.StatutPaiement.DEBITE, paiement.getStatut());
        assertTrue(paiement.estDebite());
        assertTrue(paiement.getDateDebit() != null);
    }

    @Test
    public void debiterDeuxFoisDoitEchouer() {
        Paiement paiement = new Paiement(100.0);

        paiement.debiter();

        assertThrows(IllegalStateException.class, paiement::debiter);
    }

    @Test
    public void rembourserSansDebitDoitEchouer() {
        Paiement paiement = new Paiement(100.0);

        assertThrows(IllegalStateException.class, paiement::rembourser);
    }

    @Test
    public void rembourserDoitPasserLePaiementEnRembourse() {
        Paiement paiement = new Paiement(100.0);

        paiement.debiter();
        paiement.rembourser();

        assertEquals(Paiement.StatutPaiement.REMBOURSE, paiement.getStatut());
        assertTrue(paiement.estRembourse());
        assertTrue(paiement.getDateRemboursement() != null);
    }
}