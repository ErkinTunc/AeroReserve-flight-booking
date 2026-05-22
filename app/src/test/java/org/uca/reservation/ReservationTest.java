// package org.uca.reservation;

// import static org.junit.jupiter.api.Assertions.*;

// import org.junit.jupiter.api.Test;

// public class ReservationTest {

//     private Client newClient() {
//         Client c = new Client();
//         c.setNom("Durand");
//         c.setMail("client@example.com");
//         c.setMoyenPaiement("CB");
//         return c;
//     }

//     private Passager newPassager() {
//         // Adapter aux paramètres de ton constructeur Passager(String, String, int, String)
//         return new Passager("Dupont", "Alice", 30, "AB123456");
//     }

//     @Test
//     public void creationReservationDoitInitialiserLesChamps() {
//         Client client = newClient();
//         Passager passager = newPassager();
//         ReservationFactory factory = new ReservationFactory();

//         Reservation reservation = factory.creer(100.0, client, passager);

//         assertNotNull(reservation.getNumero(), "Numero should not be null");
//         assertTrue(reservation.getNumero().startsWith("RES-"), "Numero should start with RES-");
//         assertNotNull(reservation.getDate(), "Date should be initialized");
//         assertEquals(100.0, reservation.getPrix(), 0.0001);
//         assertEquals(client, reservation.getClient());
//         assertEquals(passager, reservation.getPassager());
//         assertEquals("EN_ATTENTE", reservation.getEtat().libelle());
//     }

//     @Test
//     public void payerDevraitChangerLEtatEnPayee() {
//         Client client = newClient();
//         Passager passager = newPassager();
//         ReservationFactory factory = new ReservationFactory();

//         Reservation reservation = factory.creer(100.0, client, passager);

//         reservation.payer();

//         assertEquals("PAYEE", reservation.getEtat().libelle());
//     }

//     @Test
//     public void confirmerApresPaiementDevraitDonnerConfirmee() {
//         Client client = newClient();
//         Passager passager = newPassager();
//         ReservationFactory factory = new ReservationFactory();

//         Reservation reservation = factory.creer(100.0, client, passager);

//         reservation.payer();
//         reservation.confirmer();

//         assertEquals("CONFIRMEE", reservation.getEtat().libelle());
//     }

//     @Test
//     public void annulerDepuisEnAttenteDevraitDonnerAnnulee() {
//         Client client = newClient();
//         Passager passager = newPassager();
//         ReservationFactory factory = new ReservationFactory();

//         Reservation reservation = factory.creer(100.0, client, passager);

//         reservation.annuler();

//         assertEquals("ANNULEE", reservation.getEtat().libelle());
//     }
//     @Test
//     public void confirmerDepuisEnAttenteDoitEchouer() {
//         Client client = newClient();
//         Passager passager = newPassager();
//         ReservationFactory factory = new ReservationFactory();

//         Reservation reservation = factory.creer(100.0, client, passager);

//         assertEquals("EN_ATTENTE", reservation.getEtat().libelle());

//         assertThrows(TransitionInterditeException.class, reservation::confirmer);
//     }

//     @Test
//     public void annulerDepuisPayeeDoitEchouer() {
//         Client client = newClient();
//         Passager passager = newPassager();
//         ReservationFactory factory = new ReservationFactory();

//         Reservation reservation = factory.creer(100.0, client, passager);

//         reservation.payer();
//         assertEquals("PAYEE", reservation.getEtat().libelle());

//         assertThrows(TransitionInterditeException.class, reservation::annuler);
//     }

//     @Test
//     public void payerDepuisConfirmeeDoitEchouer() {
//         Client client = newClient();
//         Passager passager = newPassager();
//         ReservationFactory factory = new ReservationFactory();

//         Reservation reservation = factory.creer(100.0, client, passager);

//         reservation.payer();
//         reservation.confirmer();
//         assertEquals("CONFIRMEE", reservation.getEtat().libelle());

//         assertThrows(TransitionInterditeException.class, reservation::payer);
//     }

//     @Test
//     public void scenarioCompletReservationEco() {
//         Client client = newClient();
//         Passager passager = newPassager();
//         ReservationFactory factory = new ReservationFactory();

//         Reservation reservation = factory.creer(100.0, client, passager);

//         // EN_ATTENTE
//         assertEquals("EN_ATTENTE", reservation.getEtat().libelle());
//         assertEquals(100.0, reservation.getPrix(), 0.0001);

//         // PAYEE
//         reservation.payer();
//         assertEquals("PAYEE", reservation.getEtat().libelle());

//         // CONFIRMEE
//         reservation.confirmer();
//         assertEquals("CONFIRMEE", reservation.getEtat().libelle());

//         // plus d’annulation possible
//         assertThrows(TransitionInterditeException.class, reservation::annuler);
//     }

// }
