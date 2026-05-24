# Flight and Reservation Management

**Génie Logiciel L3 - TP3**

[Sujet](https://loriscroce.frama.io/enseignement/genie_logiciel_l3/tp4/) , [Guide-JUnit](https://docs.junit.org/5.7.2/user-guide/)

---

## How to run

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

## Architecture

![First UML implementation , not the final one](/docs/img/final_UML.png "UML Class Diagram")

The diagram above presents the first structural model of the flight and reservation management system.  
It focuses on the main entities: `Compagnie`, `Vol`, `Aeroport`, `Ville`, `Escale`, `Reservation`, `Client`, and `Passager`.

In the extended implementation, the model was completed with `Trajet` and `EtapeTrajet`.  
A `Vol` can be associated with a `Trajet`, which represents an ordered route composed of several steps.  
Each `EtapeTrajet` references an `Aeroport` and stores the temporal offsets used to compute departure and arrival times.

- Click here to see [Design choices](/docs/flight_management/architecture-FlightManagement.md)

- Click here to see [Tests](/docs/flight_management/tests-FlightManagement.md)

## Package organization

```text
src/main/java/org/uca
├── aeroport        # Flight domain: airports, cities, companies, flights, stops and routes
└── reservation     # Reservation domain: bookings, passengers, payments, pricing and states
    ├── model       # Core reservation entities
    ├── pricing     # Pricing strategies
    └── state       # Reservation state pattern
```

This package organization separates the flight domain from the reservation domain.

The `aeroport` package contains the classes related to flights, airports, companies, stops and routes.

The `reservation` package contains the reservation logic. It is divided into:

- `model` for the main reservation entities,
- `pricing` for fare calculation strategies,
- `state` for reservation states and transitions.

This structure keeps responsibilities clear and reduces coupling between unrelated parts of the application.

---

## Useful links

- [Compagnies aériennes et la gestion de vols et de réservations](https://exercicecorrige.blogspot.com/2013/08/compagnies-aeriennes-et-la-gestion-de.html)
