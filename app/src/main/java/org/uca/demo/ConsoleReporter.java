package org.uca.demo;

import java.io.PrintStream;
import java.util.Locale;
import java.util.Objects;

/**
 * Pure console presentation: colors, a centered content column and screen clearing.
 * No booking rules or user-input logic belongs here.
 */
final class ConsoleReporter {

    private static final String RESET = "\u001B[0m";
    private static final String CYAN_BOLD = "\u001B[1;36m";
    private static final String GREEN_BOLD = "\u001B[1;32m";
    private static final String YELLOW_BOLD = "\u001B[1;33m";
    private static final String RED_BOLD = "\u001B[1;31m";
    private static final int MAX_CONTENT_WIDTH = 72;
    private static final int DEFAULT_COLUMNS = 100;
    private static final int DEFAULT_ROWS = 32;

    private final PrintStream out;
    private final boolean ansiEnabled;
    private final int contentWidth;
    private final int terminalRows;
    private final String indent;

    ConsoleReporter(PrintStream out, boolean ansiEnabled) {
        this(out, ansiEnabled,
                dimension("AERORESERVE_COLUMNS", "COLUMNS", DEFAULT_COLUMNS),
                dimension("AERORESERVE_ROWS", "LINES", DEFAULT_ROWS));
    }

    // Explicit dimensions make layout deterministic and easy to unit-test.
    ConsoleReporter(PrintStream out, boolean ansiEnabled, int columns, int rows) {
        this.out = Objects.requireNonNull(out, "out");
        this.ansiEnabled = ansiEnabled;
        int safeColumns = Math.max(30, columns);
        this.contentWidth = Math.min(MAX_CONTENT_WIDTH, safeColumns - 4);
        this.terminalRows = Math.max(12, rows);
        this.indent = " ".repeat((safeColumns - contentWidth) / 2);
    }

    static ConsoleReporter forCurrentTerminal() {
        return new ConsoleReporter(System.out, supportsAnsi());
    }

    /** Clears the viewport, then approximates vertical centering for this view. */
    void clearScreen(int estimatedLineCount) {
        if (ansiEnabled) {
            out.print("\u001B[2J\u001B[H");
        } else {
            out.print("\n".repeat(40));
        }
        int topPadding = Math.max(1, (terminalRows - estimatedLineCount) / 2);
        out.print("\n".repeat(topPadding));
        out.flush();
    }

    void clearScreen() {
        clearScreen(18);
    }

    void banner() {
        printLine("=".repeat(contentWidth), CYAN_BOLD);
        centeredLine("AERORESERVE | FLIGHT BOOKING SYSTEM", CYAN_BOLD);
        centeredLine("Local demo - payments are simulated; no live inventory", null);
        printLine("=".repeat(contentWidth), CYAN_BOLD);
    }

    void section(int number, String heading) {
        out.println();
        centeredLine(String.format(Locale.ROOT, "[%02d] %s", number, heading), CYAN_BOLD);
    }

    void field(String label, String value) {
        printLine(String.format(Locale.ROOT, "%-12s %s", label + ":", value), null);
    }

    void fare(String label, double amount) {
        field(label, String.format(Locale.ROOT, "%.2f EUR", amount));
    }

    void success(String message) {
        prefixedLine("[OK]", message, GREEN_BOLD);
    }

    void expectedRejection(String message) {
        prefixedLine("[EXPECTED REJECTION]", message, YELLOW_BOLD);
    }

    void info(String message) {
        // Handles intentional newlines, keeping every visible line indented.
        String[] lines = message.split("\\R", -1);
        for (String line : lines) {
            printLine(line, null);
        }
    }

    void error(String message) {
        prefixedLine("[ERROR]", message, RED_BOLD);
    }

    void prompt(String message) {
        String[] lines = message.split("\\R", -1);
        for (int i = 0; i < lines.length - 1; i++) {
            printLine(lines[i], null);
        }
        out.print(indent + lines[lines.length - 1]);
        out.flush();
    }

    void complete() {
        out.println();
        printLine("=".repeat(contentWidth), GREEN_BOLD);
        centeredLine("DEMO COMPLETED SUCCESSFULLY", GREEN_BOLD);
        printLine("=".repeat(contentWidth), GREEN_BOLD);
    }

    private void centeredLine(String text, String color) {
        if (text.length() > contentWidth) {
            printLine(text, color);
            return;
        }
        String extra = " ".repeat((contentWidth - text.length()) / 2);
        out.println(indent + extra + style(text, color));
    }

    private void prefixedLine(String prefix, String message, String color) {
        String text = prefix + " " + message;
        if (text.length() <= contentWidth) {
            out.println(indent + style(prefix, color) + " " + message);
            return;
        }
        // Wrap long validation messages so they stay within the centered column.
        int available = contentWidth - prefix.length() - 1;
        String firstPart = message.substring(0, Math.min(available, message.length()));
        out.println(indent + style(prefix, color) + " " + firstPart);
        printLine(message.substring(firstPart.length()).stripLeading(), null);
    }

    private void printLine(String text, String color) {
        if (text.isEmpty()) {
            out.println();
            return;
        }
        // Deliberately simple word-independent wrapping; guarantees no line overflow.
        for (int offset = 0; offset < text.length(); offset += contentWidth) {
            String chunk = text.substring(offset, Math.min(offset + contentWidth, text.length()));
            out.println(indent + style(chunk, color));
        }
    }

    private String style(String text, String color) {
        return ansiEnabled && color != null ? color + text + RESET : text;
    }

    private static int dimension(String mainKey, String fallbackKey, int fallback) {
        String value = System.getenv(mainKey);
        if (value == null || value.isBlank()) {
            value = System.getenv(fallbackKey);
        }
        if (value == null) {
            return fallback;
        }
        try {
            int parsed = Integer.parseInt(value.trim());
            return parsed > 0 ? parsed : fallback;
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    private static boolean supportsAnsi() {
        String choice = System.getenv("AERORESERVE_COLOR");
        if ("always".equalsIgnoreCase(choice)) return true;
        if ("never".equalsIgnoreCase(choice) || System.getenv().containsKey("NO_COLOR")) return false;
        String term = System.getenv("TERM");
        return System.console() != null
                || present("WT_SESSION")
                || present("ANSICON")
                || "ON".equalsIgnoreCase(System.getenv("ConEmuANSI"))
                || (term != null && !term.isBlank() && !"dumb".equalsIgnoreCase(term));
    }

    private static boolean present(String name) {
        String value = System.getenv(name);
        return value != null && !value.isBlank();
    }
}
