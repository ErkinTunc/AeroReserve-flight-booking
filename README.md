# Flight and Reservation Management

**Génie Logiciel L3 - TP3**

[Sujet](https://loriscroce.frama.io/enseignement/genie_logiciel_l3/tp4/) , [Guide JUnit](https://docs.junit.org/5.7.2/user-guide/)

## Changes

1. Added Gradlew instruction first version

##### Compagnie Changes

2. Added **hashset** to the Compagnie class to avoid adding the same vol multiple times.
3. Added **ZoneDateTime** to the Date class to have a better handling of date and time.
4. Added **createVol** method to the Compagnie class to create a new vol and add it to the compagnie.
5. Added an internal flight number generator in Compagnie to produce unique numbers automatically.

##### Vol Changes

6. Added **ZonedDateTime** to the Vol in the place of Date to have a better handling of date and time.
7. Modified methods to make them work with ZonedDateTime instead of Date
8. Modified equals and hashCode so that two Vol objects are considered equal only when they have the same technical UUID id.
   Flight number uniqueness is enforced by Compagnie, not by Vol.equals().
9. Added _unicity_ for the flights Companies can't have have the same flight number,
   - Added **UUID** -> internal unique technical id
   - Added **String numero** -> business flight number, ex: "AF123"
   - Modified equals and hashcode

##### CompagnieTest Changes

8. Created CompagnieTest class to test _Compagnie_ class

##### VolTest Changes

9. Added test class for _Vol_ class

---

## Architecture

![First UML implementation , not the final one](/docs/img/uml1.jpeg "UML Class Diagram")

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
