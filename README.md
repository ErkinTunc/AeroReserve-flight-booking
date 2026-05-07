# Flight and Reservation Management

**Génie Logiciel L3 - TP3**

[Sujet](https://loriscroce.frama.io/enseignement/genie_logiciel_l3/tp4/) , [Guide JUnit](https://docs.junit.org/5.7.2/user-guide/)

1. Added Gradlew instruction first version

##### Compagnie Changes

2. Added **hashset** to the Compagnie class to avoid adding the same vol multiple times.
3. Added **ZoneDateTime** to the Date class to have a better handling of date and time.
4. Added **createVol** method to the Compagnie class to create a new vol and add it to the compagnie.

##### Vol Changes

6. Added **ZonedDateTime** to the Vol in the place of Date to have a better handling of date and time.
7. Modified methods to make them work with ZonedDateTime instead of Date
8. Modified equals and hashCode so that two Vol objects are considered equal when they have the same numero.

##### CompagnieTest Changes

8. Created CompagnieTest class to test _Compagnie_ class

##### VolTest Changes

9. Added test class for _Vol_ class

---

2. Implemented the travel package to the project.
   - Aeroport
   - Compagnie
   - Vol
3. Implemented Date class to the project.
   - Date

4. will implement reservation package

---

## Architecture

![First UML implementation , not the final one](/docs/img/uml1.jpeg "UML Class Diagram")

![First UML implementation , not the final one](/docs/img/uml2.jpeg "UML Class Diagram")

**1. STATE PATTERN** :

- _Escale_ extends _Etape_
- _Vol_ extends _Escale_

  so we can say that _Vol_ is a type of _Escale_ and _Escale_ is a type of _Etape_.

  ```
  Vol -> currentState : Etat
  Vol -> setState(Etat) : void
  Vol -> getState() : Etat
  ```

**2. STRATEGY PATTERN**

- _Valeur\<T>_ and othergeneric changes
  if we want to have
  - different calculation methods
  - different validation
  - different treatment

ex:

```java
interface PolitiqueTarif {
   double calculer(...)
}
```

Concrete strategyies:

- TarifEco
- TarifBusiness
- TarifPromo

**3. Observer Pattern**

- _Compagnie_ / _Vol_ relation with "general view":
  if we want to update _view_ when model is changed.

  ```
  Model -> notify()
  View -> update()
  ```

  donc

  ```
  Vol is changed
  -> notify observers
  -> GeneralView update
  ```

  **4. Composition Pattern**

**5. GENERALIZATION / INHERITANCE**

```
Escal -> Etat
```

---

### Should be added

**A) FACTORY METHOD**

```

EtatFactory.createEtat(type)
```

it is important if

- Escale
- Annule
- EnVol
- Retard
  states will be created

**B) NULL OBJECT**
Especially for the situation

- _Vol sans escale_

**C) ITERATOR** (If there is escale list)
if

```
Vol -> List<Escale>
```

then

```
Iterator<Escale>
```

is essential

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
