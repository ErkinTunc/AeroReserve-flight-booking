package org.uca.demo;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class InteractiveBookingAppTest {

    private String runWithInput(String input) {
        return runWithInput(input, false);
    }

    private String runWithInput(String input, boolean ansi) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        new InteractiveBookingApp(
                new BufferedReader(new InputStreamReader(
                        new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                        StandardCharsets.UTF_8)),
                new ConsoleReporter(new PrintStream(output, true, StandardCharsets.UTF_8), ansi),
                new BookingSession()).run();
        return output.toString(StandardCharsets.UTF_8);
    }

    @Test
    void invalidOptionDoesNotCrashAndExitWorks() {
        String output = runWithInput("oops\n\n0\n");
        assertTrue(output.contains("Unknown option"));
        assertTrue(output.contains("Goodbye"));
    }

    @Test
    void invalidAgeIsRePromptedAndReservationCreated() {
        String output = runWithInput(
                "3\nDemo Buyer\nbuyer@example.com\nAlex\nSample\nabc\n25\nDEMO0001\n3\n\n7\n\n0\n");
        assertTrue(output.contains("Enter a whole number from 0 to 120"));
        assertTrue(output.contains("Reservation created: RES-"));
        assertTrue(output.contains("160.00 EUR"));
        assertTrue(output.contains("EN_ATTENTE"));
        assertFalse(output.contains("\u001B["));
    }

    @Test
    void ansiScreenIsClearedBeforeEachViewAndWaitsForEnter() {
        String text = runWithInput("1\n\n0\n", true);
        String clear = "\u001B[2J\u001B[H";
        assertTrue(text.split(java.util.regex.Pattern.quote(clear), -1).length >= 4);
        assertTrue(text.contains("Press ENTER to return to the main menu"));
    }

    @Test
    void eofExitsWithoutException() {
        String output = runWithInput("");
        assertTrue(output.contains("Input closed. Goodbye."));
    }

    @Test
    void consoleUsesCenteredContentColumn() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ConsoleReporter reporter = new ConsoleReporter(
                new PrintStream(bytes, true, StandardCharsets.UTF_8), false, 100, 32);
        reporter.banner();
        reporter.section(0, "MAIN MENU");
        reporter.field("Flight", "AF123");
        String text = bytes.toString(StandardCharsets.UTF_8);
        String leftMargin = " ".repeat(14); // (100 - 72) / 2
        assertTrue(text.contains(leftMargin + "=".repeat(72)));
        assertTrue(text.contains(leftMargin + "Flight:"));
        assertTrue(text.contains("AERORESERVE | FLIGHT BOOKING SYSTEM"));
    }

    @Test
    void narrowTerminalKeepsTextWithinItsWidth() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ConsoleReporter reporter = new ConsoleReporter(
                new PrintStream(bytes, true, StandardCharsets.UTF_8), false, 42, 18);
        reporter.success("A very long message that should wrap inside the console column");
        reporter.prompt("Select option: ");
        String text = bytes.toString(StandardCharsets.UTF_8);
        for (String line : text.split("\\R")) {
            assertTrue(line.length() <= 42);
        }
        assertTrue(text.contains("Select option:"));
    }

    @Test
    void centeredLayoutRetainsAnsiClearAndColors() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ConsoleReporter reporter = new ConsoleReporter(
                new PrintStream(bytes, true, StandardCharsets.UTF_8), true, 100, 32);
        reporter.clearScreen(14);
        reporter.success("Payment completed");
        String text = bytes.toString(StandardCharsets.UTF_8);
        assertTrue(text.contains("\u001B[2J\u001B[H"));
        assertTrue(text.contains("\u001B[1;32m"));
    }

}
