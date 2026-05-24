package org.uca.reservation.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Vérifie les règles principales d'un paiement.
 */
public class PaiementTest {

    // ------------------ Tests de reussite ------------------

    @Test
    @DisplayName("1.1 Reussite : le constructeur initialise un paiement en attente")
    public void constructeurDoitInitialiserPaiementEnAttente() {
        Paiement paiement = new Paiement(100.0);

        assertEquals(100.0, paiement.getMontant(), 0.0001);
        assertEquals(Paiement.StatutPaiement.EN_ATTENTE, paiement.getStatut());
        assertNull(paiement.getDateDebit());
        assertNull(paiement.getDateRemboursement());
    }

    @Test
    @DisplayName("1.2 Reussite : debiter passe le paiement en debite")
    public void debiterDoitPasserLePaiementEnDebite() {
        Paiement paiement = new Paiement(100.0);

        paiement.debiter();

        assertEquals(Paiement.StatutPaiement.DEBITE, paiement.getStatut());
        assertTrue(paiement.estDebite());
        assertNotNull(paiement.getDateDebit());
    }

    @Test
    @DisplayName("1.3 Reussite : rembourser passe le paiement en rembourse")
    public void rembourserDoitPasserLePaiementEnRembourse() {
        Paiement paiement = new Paiement(100.0);

        paiement.debiter();
        paiement.rembourser();

        assertEquals(Paiement.StatutPaiement.REMBOURSE, paiement.getStatut());
        assertTrue(paiement.estRembourse());
        assertNotNull(paiement.getDateRemboursement());
    }

    // ------------------ Tests d'invalidite ------------------

    @Test
    @DisplayName("2.1 Invalidite : le constructeur refuse un montant negatif")
    public void constructeurDoitRefuserMontantNegatif() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Paiement(-1.0));
    }

    @Test
    @DisplayName("2.2 Invalidite : debiter deux fois est interdit")
    public void debiterDeuxFoisDoitEchouer() {
        Paiement paiement = new Paiement(100.0);

        paiement.debiter();

        assertThrows(IllegalStateException.class, paiement::debiter);
    }

    @Test
    @DisplayName("2.3 Invalidite : rembourser sans debit est interdit")
    public void rembourserSansDebitDoitEchouer() {
        Paiement paiement = new Paiement(100.0);

        assertThrows(IllegalStateException.class, paiement::rembourser);
    }
}