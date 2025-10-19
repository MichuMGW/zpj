import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean isMenuEnabled = true;

        while (isMenuEnabled) {
            System.out.println("MENU:");
            System.out.println("1. Laboratorium 1");
            System.out.println("2. Laboratorium 2");
            System.out.println("0. Wyjscie");


            switch (scanner.nextInt()) {
                case 1 -> Laboratorium1.Run();
                case 2 -> Laboratorium2.Run();
                case 0 -> isMenuEnabled = false;
                default -> System.out.println("Niepoprawna opcja!");
            }
        }
    }
}


