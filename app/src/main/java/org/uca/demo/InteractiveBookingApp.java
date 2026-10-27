package org.uca.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.uca.aeroport.Vol;
import org.uca.reservation.model.Reservation;
import org.uca.reservation.state.TransitionInterditeException;

/** CLI controller: input/output only, never implements booking business rules. */
public final class InteractiveBookingApp {

    private enum BookingAction { PAY, CONFIRM, CANCEL }

    private final BufferedReader input;
    private final ConsoleReporter output;
    private final BookingSession session;

    InteractiveBookingApp(BufferedReader input, ConsoleReporter output, BookingSession session) {
        this.input = Objects.requireNonNull(input, "input");
        this.output = Objects.requireNonNull(output, "output");
        this.session = Objects.requireNonNull(session, "session");
    }

    public static void runInConsole() {
        new InteractiveBookingApp(
                new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8)),
                ConsoleReporter.forCurrentTerminal(),
                new BookingSession()).run();
    }

    void run() {
        while (true) {
            output.clearScreen(18);
            output.banner();
            output.info("Interactive mode - sample flight, simulated payments, no seat inventory.");

            try {
                printMenu();
                String choice = readLine("Select option: ");
                if (choice == null) {
                    output.info("Input closed. Goodbye.");
                    return;
                }

                // Only the CLI controls screen presentation; domain logic stays unchanged.
                output.clearScreen(estimatedLines(choice.strip()));
                output.banner();
                switch (choice.strip()) {
                    case "1" -> showFlight();
                    case "2" -> showPricing();
                    case "3" -> createReservation();
                    case "4" -> changeReservation(BookingAction.PAY);
                    case "5" -> changeReservation(BookingAction.CONFIRM);
                    case "6" -> changeReservation(BookingAction.CANCEL);
                    case "7" -> listReservations();
                    case "8" -> new BookingDemo(output).run();
                    case "0" -> {
                        output.info("Goodbye - local changes are not persisted.");
                        return;
                    }
                    default -> output.error("Unknown option. Choose a number from 0 to 8.");
                }
            } catch (EndOfInputException e) {
                output.info("Input closed. Goodbye.");
                return;
            } catch (TransitionInterditeException e) {
                output.expectedRejection("Operation not allowed in the current reservation state.");
            } catch (NoSuchElementException | IllegalArgumentException e) {
                output.error("Invalid request: " + e.getMessage());
            } catch (IOException e) {
                output.error("Unable to read input: " + e.getClass().getSimpleName());
                return;
            }

            // Do not erase the result until the user has had time to read it.
            try {
                output.info("");
                if (readLine("Press ENTER to return to the main menu...") == null) {
                    output.info("Input closed. Goodbye.");
                    return;
                }
            } catch (IOException e) {
                output.error("Unable to read input: " + e.getClass().getSimpleName());
                return;
            }
        }
    }

    /** Estimate view height to center the block without moving the cursor mid-input. */
    private int estimatedLines(String choice) {
        return switch (choice) {
            case "1" -> 14;
            case "2" -> 12;
            case "3" -> 24;
            case "7" -> Math.min(28, 12 + session.reservations().size());
            case "8" -> 25;
            default -> 12;
        };
    }

    private void printMenu() {
        output.section(0, "MAIN MENU");
        output.info("1 - Show flight");
        output.info("2 - Compare fares");
        output.info("3 - Create reservation");
        output.info("4 - Pay reservation (simulated)");
        output.info("5 - Confirm reservation");
        output.info("6 - Cancel pending reservation");
        output.info("7 - List reservations");
        output.info("8 - Run original automated demo");
        output.info("0 - Exit");
    }

    private void showFlight() {
        Vol flight = session.flight();
        Duration duration = Objects.requireNonNull(flight.obtenirDuree(), "flight duration");
        output.section(1, "AVAILABLE SAMPLE FLIGHT");
        output.field("Number", flight.getNumero());
        output.field("Airline", flight.getCompagnie().getName());
        output.field("From", flight.getDepart().getVille().getNom());
        output.field("To", flight.getArrivee().getVille().getNom());
        output.field("Duration", duration.toHours() + "h " + duration.toMinutesPart() + "m");
        output.info("Note: this is a demo flight, not live availability.");
    }

    private void showPricing() {
        output.section(2, "FARE OPTIONS");
        for (BookingSession.Fare fare : BookingSession.Fare.values()) {
            output.fare(fare.label(), fare.price());
        }
    }

    private void createReservation() throws IOException {
        output.section(3, "NEW RESERVATION");
        String customerName = required("Customer full name: ");
        String customerEmail = required("Customer email: ");
        String firstName = required("Passenger first name: ");
        String lastName = required("Passenger last name: ");
        int age = numberInRange("Passenger age (0-120): ", 0, 120);
        String passport = required("Passport number (demo value): ");
        showPricing();
        int fareOption = numberInRange("Fare (1=Economy, 2=Business, 3=Promotion): ", 1, 3);
        BookingSession.BookingRequest request = new BookingSession.BookingRequest(
                customerName, customerEmail, firstName, lastName,
                age, passport, BookingSession.Fare.fromOption(fareOption));
        Reservation reservation = session.createReservation(request);
        output.success("Reservation created: " + reservation.getNumero());
        output.field("Total", String.format(java.util.Locale.ROOT, "%.2f EUR", reservation.getPrix()));
        output.field("Status", reservation.getEtat().libelle());
    }

    private void changeReservation(BookingAction action) throws IOException {
        String reference = required("Reservation reference (e.g., RES-1): ");
        Reservation reservation = switch (action) {
            case PAY -> session.pay(reference);
            case CONFIRM -> session.confirm(reference);
            case CANCEL -> session.cancel(reference);
        };
        output.success("Operation completed for " + reservation.getNumero());
        output.field("Status", reservation.getEtat().libelle());
    }

    private void listReservations() {
        output.section(7, "SESSION RESERVATIONS");
        if (session.reservations().isEmpty()) {
            output.info("No reservations created yet.");
            return;
        }
        for (Reservation reservation : session.reservations()) {
            output.field(reservation.getNumero(),
                    reservation.getPassager().getPrenom() + " " + reservation.getPassager().getNom()
                            + " | " + reservation.getEtat().libelle());
        }
        output.info("Local memory only - reservations disappear when the app exits.");
    }

    private String required(String label) throws IOException {
        while (true) {
            String value = readLine(label);
            if (value == null) {
                throw new EndOfInputException();
            }
            if (!value.isBlank()) {
                return value.strip();
            }
            output.error("This field cannot be empty.");
        }
    }

    private int numberInRange(String label, int min, int max) throws IOException {
        while (true) {
            String value = required(label);
            try {
                int number = Integer.parseInt(value);
                if (number >= min && number <= max) {
                    return number;
                }
            } catch (NumberFormatException ignored) {
                // Invalid user input is expected; show a helpful prompt.
            }
            output.error("Enter a whole number from " + min + " to " + max + ".");
        }
    }

    private String readLine(String prompt) throws IOException {
        output.prompt(prompt);
        return input.readLine();
    }

    private static final class EndOfInputException extends RuntimeException {
    }
}
