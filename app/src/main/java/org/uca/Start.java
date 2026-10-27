package org.uca;

import org.uca.demo.BookingDemo;
import org.uca.demo.InteractiveBookingApp;

/** Application entry point; domain and UI behavior live in other classes. */
public final class Start {

    private Start() {
    }

    public static void main(String[] args) {
        if (args.length == 1 && "--demo".equalsIgnoreCase(args[0])) {
            BookingDemo.runInConsole();
            return;
        }
        InteractiveBookingApp.runInConsole();
    }
}
