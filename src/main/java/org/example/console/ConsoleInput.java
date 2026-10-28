package org.example.console;

import java.util.Scanner;

public class ConsoleInput {
    private final Scanner scanner;

    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
    }

    public int readInt() {
        while (true) {
            String stringNumber = scanner.nextLine();
            try {
                return Integer.parseInt(stringNumber);
            } catch (NumberFormatException e) {
                System.out.println("Необходимо ввести число");
            }
        }
    }
}
