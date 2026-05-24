# Flight and Reservation Management

**Génie Logiciel L3 - TP3**

[Sujet](https://loriscroce.frama.io/enseignement/genie_logiciel_l3/tp4/) , [Guide-JUnit](https://docs.junit.org/5.7.2/user-guide/)

This project implements a flight and reservation management system. 

It models the main concepts of an airline domain, including companies, flights, airports, cities, stopovers, routes, passengers, reservations, payments, pricing strategies and reservation states.  

The project also includes unit tests to validate the main business rules and object interactions.

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

![Initial UML model used as a design basis](/docs/img/final_UML.png "UML Class Diagram")

The diagram above shows the initial UML model used as a design basis for the project.  
During the implementation, the model was refined to better separate a concrete flight from its route.

In particular, we added `Trajet` and `EtapeTrajet`:

- `Vol` represents a concrete flight with a number, departure and arrival dates, airports, stopovers and reservation information.
- `Trajet` represents an ordered route.
- `EtapeTrajet` represents one step of this route, linked to an `Aeroport` and defined by time offsets.
- `Escale` remains a stopover with its own arrival and departure times, not an airport itself.

Therefore, this UML should be read as the conceptual starting point of the application, while the source code represents the refined implementation model.

- [Design choices](/docs/flight_management/architecture-FlightManagement.md)
- [Tests](/docs/flight_management/tests-FlightManagement.md)

## Package organization

```text
src/main/java/org/uca
├── aeroport        # Flight domain: airports, cities, companies, flights, stopovers and routes
└── reservation     # Reservation domain: bookings, passengers, payments, pricing and states
    ├── model       # Core reservation entities
    ├── pricing     # Pricing strategies
    └── state       # Reservation state pattern
```

---

## Useful links

- [Compagnies aériennes et la gestion de vols et de réservations](https://exercicecorrige.blogspot.com/2013/08/compagnies-aeriennes-et-la-gestion-de.html)
