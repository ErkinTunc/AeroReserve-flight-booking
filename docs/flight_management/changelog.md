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

---

##### Escale Changes

1. Used an association class: Escale represents a relationship that became an object.
   - We do not model Escale as "Escale extends Aeroport", because an escale is not an airport
   - Escale stores information specific to the passage of a Vol through an Aeroport:
     _arrival time_, _departure time_, _order_, and _derived duration_.
   - Escale references exactly one Aeroport

2. Removed NullEscale.
   - The absence of escales is represented by an empty List<Escale>.
   - This avoids fake domain objects and allows proper validation in Escale

##### Escale management in Vol

1. Added an escale collection to `Vol`:
   - `List<Escale> escales`

2. Added escale management methods:
   - `addEscale(Escale escale)`
   - `removeEscale(Escale escale)`
   - `getEscales()`

3. Enforced escale-related rules:
   - a `Vol` can contain zero or more `Escale`;
   - adding a `null` escale throws an exception;
   - `getEscales()` returns an unmodifiable collection;
   - a `Vol` without escales is represented by an empty list, not by `null` or `NullEscale`.

4. Added tests in `VolTest`:
   - a `Vol` can contain an `Escale`;
   - the escale list cannot be modified from outside;
   - a `Vol` can remove an `Escale`;
   - adding a `null` `Escale` throws an exception;
   - a `Vol` without escales contains an empty escale list.

---

##### Reservation and Client changes

1. Refactored `Reservation` as a domain entity with:
   - an immutable reservation number;
   - an immutable creation date;
   - an immutable price;
   - one `Client`;
   - one `Passager`;
   - one `Vol`;
   - a current `EtatReservation`.

2. Separated `Client` and `Passager`:
   - `Client` represents the person or entity that makes the reservation;
   - `Passager` represents the person who travels;
   - one client can reserve flights for different passengers;
   - each reservation concerns exactly one client, one passenger and one flight.

3. Implemented the reservation lifecycle with the State pattern:
   - `EnAttente` is the initial state;
   - `Payee` represents a paid reservation;
   - `Confirmee` represents a confirmed reservation;
   - `Annulee` represents a cancelled reservation.

4. Implemented allowed transitions:
   - `EnAttente.payer()` -> `Payee`;
   - `EnAttente.annuler()` -> `Annulee`;
   - `Payee.confirmer()` -> `Confirmee`.

5. Implemented forbidden transitions:
   - `EnAttente.confirmer()` throws `TransitionInterditeException`;
   - `Payee.payer()` throws `TransitionInterditeException`;
   - `Payee.annuler()` throws `TransitionInterditeException` in the current simplified model;
   - all transitions from `Confirmee` throw `TransitionInterditeException`;
   - all transitions from `Annulee` throw `TransitionInterditeException`.

6. Updated the `Client` class:
   - stores name, email, payment method and loyalty points;
   - maintains a private list of reservations;
   - exposes an unmodifiable view through `getReservations()`;
   - provides `addReservation(...)` and `removeReservation(...)`;
   - ignores `null` reservations and duplicate reservations.

---

##### Pricing and ReservationFactory changes

1. Introduced the pricing strategy interface `PolitiqueTarif`:

```java
double calculer(double basePrice);
```

2. Implemented three pricing strategies:

- `TarifEco`: keeps the base price unchanged;
- `TarifBusiness`: increases the base price by 50%;
- `TarifPromo`: applies a 20% discount.

3. Updated ReservationFactory to:

- centralize reservation creation;
- generate reservation numbers automatically;
- create reservations from a base price, client, passenger and flight;
- use TarifEco as the default pricing strategy;
- offer an overloaded method with an explicit pricing policy:

```java
creer(double basePrice, PolitiqueTarif politique, Client client, Passager passager, Vol vol)
```

---

#### Aeroport, Ville and steps changes

1. Updated `Aeroport` to use a `Ville` object instead of a raw `String`:
   - `Aeroport` stores a code, a name and a `Ville`;
   - validation rejects null or blank code;
   - validation rejects null or blank name;
   - validation rejects null city;
   - `toString()` includes the airport code, name and city.

2.Implemented Ville as a dedicated domain class:

- `Ville` stores the city name;
- validation rejects null or blank names;
- `toString()` returns the city name.

3. Added an abstract Etape class to represent temporal steps:

- stores departure and arrival Date;
- computes a derived Duration getDuree();
- returns Duration.ZERO when one date is missing;
- rejects inconsistent dates where departure is after arrival.

4. Implemented Escale as a concrete Etape:

- references exactly one Aeroport;
- stores arrival and departure dates through Etape;
- derives its duration from those dates;
- rejects a null airport.

5. Removed NullEscale from the model:
   - absence of stopovers is represented by an empty List<Escale>;
   - no fake escale object is needed;
   - this keeps the model simpler and avoids invalid domain objects.
