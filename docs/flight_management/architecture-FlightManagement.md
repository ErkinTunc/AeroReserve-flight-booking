# Design Decisions

## Table of Contents

- [Design Decisions](#design-decisions)
  - [Table of Contents](#table-of-contents)
  - [1. Flight Identity and Airline Relationships](#1-flight-identity-and-airline-relationships)
    - [Flight identity vs flight number](#flight-identity-vs-flight-number)
    - [Bidirectional association](#bidirectional-association)
  - [2. Airport and City Relationships](#2-airport-and-city-relationships)
  - [3. Stopover Modeling](#3-stopover-modeling)
    - [Escale as an association class](#escale-as-an-association-class)
  - [4. Reservation Domain and Pricing Design](#4-reservation-domain-and-pricing-design)
    - [Reservation as a domain entity](#reservation-as-a-domain-entity)
    - [Client and passenger distinction](#client-and-passenger-distinction)
    - [Pricing strategy](#pricing-strategy)
    - [Reservation creation with Factory](#reservation-creation-with-factory)
  - [5. Reservation Lifecycle and State Transitions](#5-reservation-lifecycle-and-state-transitions)
    - [Reservation state model](#reservation-state-model)
    - [Reservation state transitions](#reservation-state-transitions)
    - [Why State Pattern instead of a simple enum](#why-state-pattern-instead-of-a-simple-enum)
  - [6. Shared Routes and Regular Flights](#6-shared-routes-and-regular-flights)
  - [7. Package Organization and Separation of Concerns](#7-package-organization-and-separation-of-concerns)
  - [8. Interactive CLI and Local Booking Session](#8-interactive-cli-and-local-booking-session)
    - [Why the CLI is separate](#why-the-cli-is-separate)
    - [Console presentation](#console-presentation)
  - [9. Payment Coordination and Validation](#9-payment-coordination-and-validation)
    - [Payment before state change](#payment-before-state-change)
    - [Defensive validation and reference generation](#defensive-validation-and-reference-generation)
    - [Known encapsulation limitation](#known-encapsulation-limitation)
  - [10. Testing and Continuous Integration](#10-testing-and-continuous-integration)
  - [11. Current Scope and Next Steps](#11-current-scope-and-next-steps)

---

## 1. Flight Identity and Airline Relationships

### Flight identity vs flight number

A `Vol` has two different identifiers:

- `id: UUID` is the technical identity of the object.
- `numero: String` is the business flight number.

Two different `Vol` objects may technically have the same `numero`, but a single `Compagnie` is not allowed to contain two flights with the same number.

Therefore:

- `Vol.equals()` and `Vol.hashCode()` use the technical UUID.
- flight number uniqueness is enforced by `Compagnie`.
- both `Compagnie.addVol(vol)` and `vol.setCompagnie(compagnie)` preserve this invariant.

### Bidirectional association

The association between `Compagnie` and `Vol` is bidirectional:

- adding a flight to a company updates the flight's company reference;
- changing a flight's company updates both the old and the new company collections;
- removing a flight from a company clears the flight's company reference.

---

## 2. Airport and City Relationships

An `Aeroport` references exactly one `Ville`.

A `Ville` can be served by zero or more airports. For example, a large city may have several airports, while each airport is located in one main city.

This gives the following multiplicity:

```text
Ville 1 --- 0..* Aeroport
Aeroport 1 --- 1 Ville
```

In the implementation, the relationship is stored only from `Aeroport` to `Ville` to keep the model simple and avoid unnecessary bidirectional coupling.

---

## 3. Stopover Modeling

### Escale as an association class

An `Escale` is not an `Aeroport`.

An airport is a stable domain entity, while an escale represents the passage of a specific flight through an airport. This passage has its own information:

- arrival time
- departure time
- derived duration
- possibly an order in the route

A simple association between `Vol` and `Aeroport` would not be enough, because the association itself carries data. For this reason, `Escale` is modeled conceptually as an association class between `Vol` and `Aeroport`.

The chosen model is:

- one `Vol` contains zero or more `Escale`;
- one `Escale` references exactly one `Aeroport`;
- the absence of stopovers is represented by an empty collection, rather than a fake airport or a `NullEscale`.

In the implementation, `Escale` is represented as a normal class contained by `Vol`. It extends `Etape` only to reuse the common temporal attributes `depart`, `arrivee`, and the derived duration calculation. Conceptually, however, an escale remains an enriched relation between a flight and an airport.

---

## 4. Reservation Domain and Pricing Design

### Reservation as a domain entity

A `Reservation` is modeled as a real domain entity, not as a simple association between `Client` and `Vol`.

A reservation has its own identity and business data:

- reservation number
- creation date
- price
- client
- passenger
- flight
- current state
- simulated payment (`Paiement`)

This is necessary because a reservation has its own lifecycle: it can be paid, confirmed, or cancelled depending on its current state.

### Client and passenger distinction

`Client` and `Passager` are modeled as two separate concepts.

A `Client` is the person or entity that creates and pays for reservations.  
A `Passager` is the person who actually travels on a flight.

This distinction is required because one client may reserve flights for different passengers. Therefore, a reservation references exactly one client and exactly one passenger.

### Pricing strategy

Reservation pricing is separated from reservation creation by using the Strategy pattern.

The `PolitiqueTarif` interface defines one operation:

```java
double calculer(double basePrice);
```

Each implementation applies a different pricing rule, such as economy, business, or promotional pricing.

### Reservation creation with Factory

Reservations are created through `ReservationFactory`.

This centralizes reservation creation and keeps the constructor controlled. The factory is responsible for:

- generating a reservation number;
- applying a pricing policy;
- creating a reservation with its client, passenger and flight.

By default, the factory uses the economy pricing policy. `ReservationFactory` now validates finite, nonnegative prices and generates `RES-*` references using an `AtomicInteger`. This avoids counter races **within a single JVM**, but the references are not persistent or globally unique across application restarts.

---

## 5. Reservation Lifecycle and State Transitions

### Reservation state model

A reservation has one current state at a time.

We do not represent reservation status with multiple booleans such as `paid`, `confirmed`, or `cancelled`, because this could create inconsistent combinations.

Instead, reservation states are modeled with the State pattern.

The possible states are:

- `EnAttente`: initial state
- `Payee`: payment has been completed
- `Confirmee`: reservation is confirmed
- `Annulee`: reservation is cancelled

Each state is responsible for deciding which transitions are allowed and which transitions are forbidden.

### Reservation state transitions

<p align="center">
  <em>
    The reservation lifecycle is modeled with the following transitions:
  </em>
</p>

<p align="center">
  <img
    src="../img/State Diagram_ Payment Workflow.png"
    alt="AeroReserve reservation state diagram"
    width="650"
  />
</p>





```text
EN_ATTENTE
   -- payer() --> PAYEE
   -- annuler() --> ANNULEE
   -- confirmer() --> forbidden

PAYEE
   -- confirmer() --> CONFIRMEE
   -- payer() --> forbidden
   -- annuler() --> forbidden in the current simplified model

CONFIRMEE
   -- payer(), confirmer(), annuler() --> forbidden

ANNULEE
   -- payer(), confirmer(), annuler() --> forbidden
```

Forbidden transitions throw a `TransitionInterditeException`. Payment is coordinated separately, as described in [Section 9](#9-payment-coordination-and-validation).

### Why State Pattern instead of a simple enum

A simple enum would be enough if states only stored names.

However, in this model, each state has its own behavior:

- `EnAttente` allows payment and cancellation.
- `Payee` allows confirmation.
- `Confirmee` forbids further modification.
- `Annulee` forbids further modification.

For this reason, the State pattern is more explicit and avoids large conditional blocks inside `Reservation`.

---

## 6. Shared Routes and Regular Flights

The solution can be interpreted as an application of the Flyweight pattern.

`Trajet` represents the intrinsic state shared between several regular flights:

- ordered list of steps;
- airports;
- relative arrival/departure offsets.

`Vol` represents the extrinsic state of a concrete flight occurrence:

- departure date and time;
- arrival date and time;
- flight-specific information.

This avoids duplicating the same route structure for every regular flight.

To limit side effects when sharing routes, `Trajet` stores its steps in a sorted, non-modifiable list. Its fields and the fields of `EtapeTrajet` are final, allowing multiple flights to reuse the same route structure. However, referenced `Aeroport` objects remain mutable, so the entire object graph is not deeply immutable.

---

## 7. Package Organization and Separation of Concerns

```text
app/src/main/java/org/uca/
├── Start.java              # Application entry point
├── aeroport/               # Airlines, flights, airports, stopovers and routes
├── demo/                   # CLI, local booking session, demo data and presentation
│   ├── InteractiveBookingApp.java
│   ├── BookingSession.java
│   ├── BookingDemo.java
│   ├── DemoFixtures.java
│   └── ConsoleReporter.java
└── reservation/
    ├── model/              # Reservation, payment and reservation creation
    ├── pricing/            # Pricing strategies
    └── state/              # State pattern and transition rules
```

The project separates three concerns:

- `aeroport` defines flights and routes without depending on reservations.
- `reservation` implements booking rules and references `Vol` from `aeroport`.
- `demo` provides a local interface over those domain classes; domain rules do not depend on console output.

`Start` only chooses the interactive CLI or the automatic demonstration. This keeps terminal behavior out of the domain model.

---

## 8. Interactive CLI and Local Booking Session

### Why the CLI is separate

`InteractiveBookingApp` reads user input and dispatches menu choices with a `switch`. It does not implement pricing or reservation transition rules itself.

The interactive menu supports viewing the sample flight, comparing fares, creating reservations, simulated payment, confirmation, cancellation of pending bookings, and listing the current session's reservations. The previous scripted demonstration remains available through `--demo` or the menu.

```text
Start
  ├── --demo → BookingDemo → domain objects
  └── default → InteractiveBookingApp
                  ├── BookingSession → ReservationFactory / Reservation
                  │                      ├── pricing strategies
                  │                      └── state and payment logic
                  └── ConsoleReporter (terminal output only)
```

`BookingSession` owns a local map of reservation references to `Reservation` objects. It assembles `Client`, `Passager` and the selected pricing policy, then delegates creation to `ReservationFactory`. This is session state **in memory**, not database storage or seat inventory.

### Console presentation

`ConsoleReporter` is responsible for ANSI colors, a centered content column, screen clearing and formatted messages. `InteractiveBookingApp` decides when to render each view and waits for Enter before returning to the menu. Separating them avoids mixing display code with booking rules.

Input parsing is handled by the CLI using `BufferedReader`, required-text checks and bounded integer parsing. Invalid options and forbidden state transitions produce readable messages without requiring changes in the underlying domain model.

---

## 9. Payment Coordination and Validation

### Payment before state change

Previously, `Reservation.payer()` changed the reservation state before debiting its `Paiement`. If debit failed, the reservation could incorrectly remain `PAYEE`.

Now `Reservation.payer()` delegates to the package-private `ReservationPaymentService`, which:

1. Checks whether the current state allows payment through `EtatReservation.peutPayer()`.
2. Debits the simulated `Paiement`.
3. Moves the reservation from `EN_ATTENTE` to `PAYEE` only after the debit succeeds.

If the debit throws, the state change is not performed. `confirmer()` still requires a paid reservation; the State pattern continues to reject other invalid transitions.

This is a **local simulated payment flow**, not an atomic database transaction or integration with a payment provider.

### Defensive validation and reference generation

`Reservation` and `ReservationFactory` use `Objects.requireNonNull()` for required object references. The factory and reservation constructor reject negative, `NaN` and infinite prices. Reservation references use a shared `AtomicInteger` for thread-safe increments within the running JVM.

### Known encapsulation limitation

`Reservation.setEtat(...)` is still public for compatibility with the existing State implementation, which means other callers can bypass the intended lifecycle. The public `debiter()` and `rembourser()` shortcuts likewise expose payment operations directly. These are technical debts to address before exposing the domain to untrusted clients. Moving transitions behind a controlled internal API would require a coordinated change to the State classes and their tests.

---

## 10. Testing and Continuous Integration

The project uses **JUnit 5** and **GitHub Actions CI** with Java 21. Domain tests cover flight number uniqueness, bidirectional associations, pricing policies, reservation states and payment behavior.

The recent CLI and payment changes added these focused test classes:

- `BookingDemoTest` — scripted demo behavior.
- `BookingSessionTest` — local booking actions and state changes.
- `InteractiveBookingAppTest` — interactive input and menu behavior.
- `ReservationPaymentRegressionTest` — failed debit, forbidden payment, invalid prices and concurrent reservation-reference generation.

The reference-generation concurrency test checks that simultaneous threads receive distinct IDs. It **does not** verify concurrent seat allocation, because the application has no seat inventory yet.

From the repository root:

```bash
./gradlew clean test
./gradlew --console=plain run
./gradlew --console=plain run --args="--demo"
```

On Windows PowerShell, use `./gradlew.bat` (or `.\gradlew.bat`) instead. Gradle's `run` task forwards standard input to the CLI. `--console=plain` avoids the animated progress indicator interfering with interactive prompts.

---

## 11. Current Scope and Next Steps

AeroReserve is currently a **local Java domain-model application with an interactive CLI**, not a deployed flight-booking service.

**Implemented:** sample flight and fare selection, in-memory reservations, simulated payment, controlled reservation transitions, input checks, automated tests and CI.

**Not implemented:** persistent reservations, seat availability, concurrent seat-booking protection, an HTTP API, authentication or real payment processing. Reservations are lost when the application exits.

**Next engineering goal:** model seat identity as `(flightId, seatNumber)`, reject competing requests for the same seat, and validate the invariant under concurrency. Once the in-memory booking invariant is tested, move it to PostgreSQL with a database uniqueness constraint and transactional reservation creation, then expose it through a small REST API.
