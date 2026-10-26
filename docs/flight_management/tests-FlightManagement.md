# Tests - FlightManagement

This document summarizes the unit tests used to validate the flight and reservation management model.

---

## Airport and city model

### AeroportTest

`AeroportTest` verifies that an airport correctly stores its code, name and city.

It checks that:

- the constructor initializes code, name and city;
- setters correctly update code, name and city;
- `toString()` contains the airport code, name and city;
- null or blank code is rejected;
- null or blank name is rejected;
- null city is rejected.

### VilleTest

`VilleTest` verifies the behavior of the `Ville` class.

It checks that:

- the constructor initializes the city name;
- `setNom(...)` updates the city name;
- `toString()` returns the city name;
- null or blank names are rejected.

---

## Flight and company model

### CompagnieTest

`CompagnieTest` verifies that a company can create and manage flights.

It checks that:

- a company can create a flight and automatically link it to itself;
- `creerVol(...)` initializes the flight number, dates, departure airport and arrival airport;
- flight duration is correctly calculated;
- adding a flight to another company transfers it from the old company;
- `setCompagnie(null)` removes the flight from its company;
- creating a flight with missing information throws an exception;
- the same `Vol` object cannot appear twice in the same company;
- a company refuses two flights with the same business flight number;
- `addVol(...)` refuses a flight whose number already exists in the company;
- automatically generated flight numbers are unique;
- the automatic generator skips already used manual numbers such as `VOL-1`.

### VolTest

`VolTest` verifies the behavior, identity rules and stopover management of a flight.

It checks that:

- flight duration is calculated from departure and arrival dates;
- missing departure or arrival dates return `null` for the duration;
- setters correctly define flight information;
- `setCompagnie(...)` adds the flight to the company collection;
- `setCompagnie(null)` removes the flight from its previous company;
- changing a flight's company updates both the old and new company collections;
- two flights with the same business number still have different technical identities;
- `HashSet` distinguishes two flights with the same business number but different UUIDs;
- assigning a flight to a company is refused if the company already has a flight with the same number;
- a flight can contain an `Escale`;
- the escale collection returned by `getEscales()` is unmodifiable;
- a flight can remove an escale;
- adding a null escale throws an exception;
- a flight with no escale exposes an empty escale collection;
- two regular flights can share the same `Trajet`;
- a flight can compute the real arrival and departure times of route steps from its departure datetime.

---

## Stopover and temporal step model

### EtapeTest

`EtapeTest` verifies common temporal behavior shared by route steps.

It checks that:

- duration is `Duration.ZERO` when one date is missing;
- duration is calculated from departure and arrival dates;
- departure cannot be after arrival;
- `setDepart(...)` rejects a date after the current arrival date;
- `setArrivee(...)` rejects a date before the current departure date.

### EscaleTest

`EscaleTest` verifies that an escale represents a stopover at an airport.

It checks that:

- the constructor initializes departure date, arrival date and airport;
- `setAeroport(...)` changes the associated airport;
- `getDuree()` returns the stopover duration;
- an escale must have an airport;
- `setAeroport(null)` is rejected.

### EtapeTrajetTest

`EtapeTrajetTest` verifies the relative steps of a shared route.

It checks that:

- the constructor initializes order, airport, arrival offset and departure offset;
- stop duration is calculated from relative arrival and departure offsets;
- stop duration is zero when one offset is missing;
- negative order is rejected;
- null airport is rejected;
- negative arrival or departure offsets are rejected;
- departure offset cannot be before arrival offset.

### TrajetTest

`TrajetTest` verifies the route model used by regular flights.

It checks that:

- the constructor initializes the route code and steps;
- null or blank route code is rejected;
- a route must contain at least two steps;
- two steps cannot have the same order;
- the step list returned by `getEtapes()` is non-modifiable;
- steps are sorted by order;
- `getPremiereEtape()` returns the first step;
- `getDerniereEtape()` returns the last step.

---

## Reservation model

### ClientTest

`ClientTest` verifies the behavior of the `Client` class.

It checks that:

- getters and setters store client information correctly;
- `getReservations()` returns a non-modifiable list;
- `addReservation(...)` adds a reservation;
- `removeReservation(...)` removes a reservation;
- null reservations and duplicate reservations are ignored.

### PassagerTest

`PassagerTest` verifies the creation and validation of passengers.

It checks that:

- the constructor initializes last name, first name, age and passport number;
- `setTelephone(...)` updates the phone number;
- null or blank last name is rejected;
- null or blank first name is rejected;
- negative age is rejected;
- null or blank passport number is rejected;
- null or blank phone number is rejected.

### PaiementTest

`PaiementTest` verifies the payment lifecycle.

It checks that:

- a new payment starts with status `EN_ATTENTE`;
- `debiter()` changes the payment status to `DEBITE`;
- `rembourser()` changes the payment status to `REMBOURSE`;
- a negative amount is rejected;
- debiting twice is forbidden;
- refunding before debit is forbidden.

### ReservationTest

`ReservationTest` verifies reservation creation and the main reservation lifecycle.

It checks that:

- a reservation receives a generated number;
- the reservation date is initialized;
- the price, client, passenger and flight are correctly stored;
- a new reservation starts in `EN_ATTENTE`;
- the reservation is added to the client's reservation list;
- `payer()` changes the state to `PAYEE`;
- `payer()` debits the payment;
- `confirmer()` after payment changes the state to `CONFIRMEE`;
- `annuler()` from `EN_ATTENTE` changes the state to `ANNULEE`;
- confirming from `EN_ATTENTE` throws `TransitionInterditeException`;
- cancelling from `PAYEE` throws `TransitionInterditeException`;
- paying from `CONFIRMEE` throws `TransitionInterditeException`;
- a complete eco reservation scenario works from creation to confirmation.

### ReservationStateTest

`ReservationStateTest` focuses specifically on the State pattern behavior.

It checks that:

- `payer()` from `EN_ATTENTE` transitions to `PAYEE`;
- `confirmer()` after payment transitions to `CONFIRMEE`;
- `confirmer()` directly from `EN_ATTENTE` is forbidden.

---

## Pricing and reservation factory

### TarifEcoTest

`TarifEcoTest` verifies that the economy pricing policy keeps the base price unchanged.

### TarifBusinessTest

`TarifBusinessTest` verifies that the business pricing policy increases the base price by 50%.

### TarifPromoTest

`TarifPromoTest` verifies that the promotional pricing policy applies a 20% discount.

### ReservationFactoryTest

`ReservationFactoryTest` verifies that `ReservationFactory` correctly applies pricing policies and validates reservation creation inputs.

It checks that:

- creating a reservation with `TarifEco` keeps the base price unchanged;
- creating a reservation with `TarifBusiness` increases the price by 50%;
- creating a reservation with `TarifPromo` applies a 20% discount;
- creating a reservation with a negative base price is rejected;
- creating a reservation with a null pricing policy is rejected.
