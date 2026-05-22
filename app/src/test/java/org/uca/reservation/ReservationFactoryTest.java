// package org.uca.reservation;

// import org.junit.jupiter.api.Test;

// import static org.junit.jupiter.api.Assertions.*;

// public class ReservationFactoryTest {

//     private Client newClient() {
//         Client c = new Client();
//         c.setNom("Durand");
//         c.setMail("client@example.com");
//         c.setMoyenPaiement("CB");
//         return c;
//     }

//     private Passager newPassager() {
//         return new Passager("Dupont", "Alice", 30, "AB123456");
//     }

//     @Test
//     public void creerAvecTarifEcoDoitConserverLePrixDeBase() {
//         Client client = newClient();
//         Passager passager = newPassager();
//         ReservationFactory factory = new ReservationFactory();
//         PolitiqueTarif eco = new TarifEco();

//         Reservation reservation = factory.creer(100.0, eco, client, passager);

//         assertEquals(100.0, reservation.getPrix(), 0.0001);
//     }

//     @Test
//     public void creerAvecTarifBusinessDoitAugmenterLePrixDe50Pourcents() {
//         Client client = newClient();
//         Passager passager = newPassager();
//         ReservationFactory factory = new ReservationFactory();
//         PolitiqueTarif business = new TarifBusiness();

//         Reservation reservation = factory.creer(100.0, business, client, passager);

//         assertEquals(150.0, reservation.getPrix(), 0.0001);
//     }

//     @Test
//     public void creerAvecTarifPromoDoitAppliquerUneRemiseDe20Pourcents() {
//         Client client = newClient();
//         Passager passager = newPassager();
//         ReservationFactory factory = new ReservationFactory();
//         PolitiqueTarif promo = new TarifPromo();

//         Reservation reservation = factory.creer(100.0, promo, client, passager);

//         assertEquals(80.0, reservation.getPrix(), 0.0001);
//     }
// }
