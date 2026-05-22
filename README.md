# Flight and Reservation Management

**Génie Logiciel L3 - TP3**

[Sujet](https://loriscroce.frama.io/enseignement/genie_logiciel_l3/tp4/) , [Guide JUnit](https://docs.junit.org/5.7.2/user-guide/)

## Changes

1. Added Gradlew instruction first version

##### Compagnie Changes

1. Added **hashset** to the Compagnie class to avoid adding the same vol multiple times.
2. Added **ZoneDateTime** to the Date class to have a better handling of date and time.
3. Added **createVol** method to the Compagnie class to create a new vol and add it to the compagnie.
4. Added an internal flight number generator in Compagnie to produce unique numbers automatically.
5. Added validation to reject null flight creation parameters.
6. Added business rule enforcement:
   - a company cannot contain two flights with the same flight number.
   - this rule is checked both through `Compagnie.addVol(...)` and `Vol.setCompagnie(...)`.
7. Added an internal controlled flight number generator:
   - automatically generated numbers follow the format `VOL-1`, `VOL-2`, etc.
   - the generator skips already used manual numbers.

##### Vol Changes

1. Added **ZonedDateTime** to the Vol in the place of Date to have a better handling of date and time.
2. Modified methods to make them work with ZonedDateTime instead of Date
3. Modified equals and hashCode so that two Vol objects are considered equal only when they have the same technical UUID id.
   Flight number uniqueness is enforced by Compagnie, not by Vol.equals().
4. Added _unicity_ for the flights Companies can't have have the same flight number,
   - Added **UUID** -> internal unique technical id
   - Added **String numero** -> business flight number, ex: "AF123"
   - Modified equals and hashcode
     Reservation and client changes

##### Escale Changes

1. Used an association class: Escale represents a relationship that became an object.
   - We do not model Escale as "Escale extends Aeroport", because an escale is not an airport
   - Escale stores information specific to the passage of a Vol through an Aeroport:
     *arrival time*, *departure time*, *order*, and *derived duration*.
   - Escale references exactly one Aeroport

2. Removed NullEscale.
   - The absence of escales is represented by an empty List<Escale>.
   - This avoids fake domain objects and allows proper validation in Escale

###### Escale management in Vol

1. Added escale collection in Vol:
   - *List\<Escale> escales*

2. Added methods:
   - *addEscale(Escale escale)*
   - *removeEscale(Escale escale)*
   - *getEscales()*

3. Added tests in VolTest:
   - a Vol can contain an Escale
   - the escale list cannot be modified from outside
   - a Vol can remove an Escale
   - adding a null Escale throws an exception

---

### Reservation and Client changes

1. Refactored the `Reservation` model to have:
   - an immutable reservation number, creation date, price, client and passenger;
   - a pluggable `EtatReservation` implementing the State pattern.

2. Implemented a full reservation state machine:
   - `EnAttente` (initial state):
     - `payer()` → transitions to `Payee`;
     - `annuler()` → transitions to `Annulee`;
     - `confirmer()` throws `TransitionInterditeException`.
   - `Payee`:
     - `confirmer()` → transitions to `Confirmee`;
     - `payer()` and `annuler()` throw `TransitionInterditeException`.
     - `Confirmee` and `Annulee`:
     - all further transitions are forbidden (always throw `TransitionInterditeException`).

3. Simplified the `Client` class:
   - stores basic data (name, email, payment method, loyalty points);
   - maintains a private list of reservations;
   - exposes an unmodifiable view through `getReservations()`;
   - provides `addReservation` / `removeReservation` methods that ignore nulls and duplicates.

### Pricing and ReservationFactory changes

1. Introduced a pricing strategy interface `PolitiqueTarif`:

```java
double calculer(double basePrice);
```

2. Implemented three pricing strategies:

- `TarifEco`: base price unchanged;
- `TarifBusiness`: +50% on top of the base price;
- `TarifPromo`: 20% discount on the base price.

3. Updated `ReservationFactory` to:

- create reservations from a base price, client and passenger;
- use `TarifEco` as default strategy when no explicit policy is provided;
- offer an overloaded `creer(double basePrice, PolitiqueTarif politique, Client client, Passager passager)` method that applies the given pricing policy.

### Aeroport, Ville and steps changes

1. Updated `Aeroport` to use a `Ville` object instead of a raw `String` for the city:
   - added validation on code, name and city;
   - improved `toString()` to include code, name and city.
   - Implemented `Ville` as a dedicated class for city names.
2. Added an abstract `Etape` class to represent temporal steps:
   - stores departure and arrival `Date`;
   - computes a derived `Duration getDuree()` as `arrivee - depart` (or `Duration.ZERO` when dates are missing).
3. Implemented `Escale` as a concrete `Etape` with an associated `Aeroport`.
4. Implemented `NullEscale` as a Null Object singleton (`NullEscale.getInstance()`) used to represent “no stopover” instead of using `null`.

#### Tests

- [Tests](/docs/tests-FlightManagement.md)

---

## Architecture

![First UML implementation , not the final one](/docs/img/uml1.jpeg "UML Class Diagram")

#### Design choices

Click here to see [Design choices](/docs/architecture-FlightManagement.md#design-decisions)

---

## Gradlew

1. > ./gradlew run

   Run with the run task, which assembles the application and executes some script or binary

2. > ./gradlew build

   Gradle builds for the build task to designate assembling all outputs and running all

3. > ./gradlew clean

   Delete the contents of the build directory using the clean task. Doing so will cause pre-computed outputs to be lost

- > ./gradlew test

  Runs the test task for all subprojects when invoked from the root project
  (if it is correct it should run all the tests and just show the summary of all test, if not it will only show the mistakes.)
  - > build/reports/tests/test/index.html

    To see the details of the tests on the browser.

---

## Useful links

- [Compagnies aériennes et la gestion de vols et de réservations](https://exercicecorrige.blogspot.com/2013/08/compagnies-aeriennes-et-la-gestion-de.html)
