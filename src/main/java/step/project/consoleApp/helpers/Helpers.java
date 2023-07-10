package step.project.consoleApp.helpers;

import java.util.Scanner;

public class Helpers {

    private final Scanner sc;


    public Helpers(Scanner sc) {
        this.sc = sc;
    }


    // Убирает пробелы в начале и конце строки, заменяет повторяющиеся пробелы на одинарный пробел
    public String spaceTrim(String s) {
        s = s.trim();
        while (s.contains(" ".repeat(2)))
            s = s.replace(" ".repeat(2), " ");

        return s;
    }


    public String inputString(String requestText) {
        System.out.print("(вiдмiна - '/q') " + requestText);

        String s = sc.nextLine();
        if (s.equalsIgnoreCase("/q"))
            return null;

        return spaceTrim(s);
    }


    public Integer inputInt(String requestText) {

        while (true) {
            System.out.print("('/q' для отмены) " + requestText);

            if (sc.hasNextInt()) {
                int inputInt = sc.nextInt();
                sc.nextLine();
                return inputInt;
            }

            String s = sc.nextLine();
            if (s.equalsIgnoreCase("/q"))
                return null;
        }
    }
}
