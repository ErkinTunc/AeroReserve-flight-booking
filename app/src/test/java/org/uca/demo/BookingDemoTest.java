package org.uca.demo;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class BookingDemoTest {

    @Test
    void demoShowsSuccessfulFlowAndExpectedRejectionWithoutAnsi() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(buffer, true, StandardCharsets.UTF_8);

        new BookingDemo(new ConsoleReporter(stream, false)).run();

        String output = buffer.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("[EXPECTED REJECTION] Cannot confirm a reservation before payment."));
        assertTrue(output.contains("[OK] Simulated payment debited"));
        assertTrue(output.contains("[OK] Reservation status: CONFIRMEE"));
        assertTrue(output.contains("3h 20m"));
        assertTrue(output.contains("DEMO COMPLETED SUCCESSFULLY"));
        assertFalse(output.contains("\u001B["));
    }

    @Test
    void consoleReporterSupportsAnsiColorsWhenEnabled() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(buffer, true, StandardCharsets.UTF_8);

        ConsoleReporter reporter = new ConsoleReporter(stream, true);
        reporter.section(1, "FLIGHT INFORMATION");
        reporter.success("Payment confirmed");
        reporter.expectedRejection("Expected failure");

        String output = buffer.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("\u001B[1;36m"));
        assertTrue(output.contains("\u001B[1;32m"));
        assertTrue(output.contains("\u001B[1;33m"));
        assertTrue(output.contains("\u001B[0m"));
    }
}
