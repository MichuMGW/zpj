import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;


    public static void main(String[] args) {
        List<Book> books = List.of(
                new Book("Władca Pierścieni", "J.R.R. Tolkien", 1954, 49.99),
                new Book("Hobbit", "J.R.R. Tolkien", 1937, 39.99),
                new Book("Harry Potter", "J.K. Rowling", 1997, 45.50),
                new Book("Inferno", "Dan Brown", 2013, 32.00),
                new Book("Kod Leonarda da Vinci", "Dan Brown", 2003, 29.99)
        );

        Scanner sc = new Scanner(System.in);
        String fileName = "biblioteka.txt";

        while (true) {
            System.out.println("\n MENU:");
            System.out.println("1. Filtrowanie książek (autor zawiera 'Tolkien')");
            System.out.println("2. Liczenie powtórzeń autorów");
            System.out.println("3. Mapowanie tytułów na wielkie litery");
            System.out.println("4. Zapis i odczyt danych z pliku");
            System.out.println("5. Filtrowanie i sortowanie z pliku");
            System.out.println("0. Wyjście");
            System.out.print("Wybierz opcję: ");

            int opcja = sc.nextInt();
            sc.nextLine(); // czyszczenie bufora

            switch (opcja) {
                case 1 -> zadanie1(books);
                case 2 -> zadanie2(books);
                case 3 -> zadanie3(books);
                case 4 -> zadanie4(books, fileName);
                case 5 -> zadanie5(fileName);
                case 0 -> {
                    System.out.println("Koniec programu ");
                    return;
                }
                default -> System.out.println("Niepoprawna opcja!");
            }
        }
    }

    // Filtrowanie
    private static void zadanie1(List<Book> books) {
        System.out.println("\n Książki Tolkiena:");
        books.stream()
                .filter(b -> b.getAuthor().contains("Tolkien"))
                .forEach(System.out::println);
    }

    // Liczenie powtórzeń
    private static void zadanie2(List<Book> books) {
        System.out.println("\n Liczba książek każdego autora:");
        books.stream()
                .collect(Collectors.groupingBy(Book::getAuthor, Collectors.counting()))
                .forEach((a, c) -> System.out.println(a + " → " + c + " książki"));
    }

    // Mapowanie
    private static void zadanie3(List<Book> books) {
        System.out.println("\n Tytuły dużymi literami:");
        books.stream()
                .map(b -> b.getTitle().toUpperCase())
                .forEach(System.out::println);
    }

    // Zapis i odczyt pliku
    private static void zadanie4(List<Book> books, String fileName) {
        // Zapis przy użyciu java.io
        try (FileWriter writer = new FileWriter(fileName)) {
            for (Book b : books) {
                writer.write(b.getTitle() + ";" + b.getAuthor() + ";" + b.getYear() + ";" + b.getPrice() + "\n");
            }
            System.out.println(" Dane zapisane do pliku " + fileName);
        } catch (IOException e) {
            System.out.println(" Błąd zapisu: " + e.getMessage());
        }

        // Odczyt przy użyciu java.nio
        try {
            System.out.println("\n Odczytane dane z pliku:");
            Files.readAllLines(Paths.get(fileName)).forEach(System.out::println);
        } catch (IOException e) {
            System.out.println(" Błąd odczytu: " + e.getMessage());
        }
    }

    // Filtrowanie i sortowanie z pliku
    private static void zadanie5(String fileName) {
        try {
            List<Book> books = Files.lines(Paths.get(fileName))
                    .map(line -> line.split(";"))
                    .filter(p -> p.length == 4)
                    .map(p -> new Book(p[0], p[1], Integer.parseInt(p[2]), Double.parseDouble(p[3])))
                    .collect(Collectors.toList());

            System.out.println("\n Książki droższe niż 40 zł (posortowane malejąco):");
            books.stream()
                    .filter(b -> b.getPrice() > 40)
                    .sorted(Comparator.comparing(Book::getPrice).reversed())
                    .forEach(System.out::println);
        } catch (IOException e) {
            System.out.println(" Błąd! Nie udało się wczytać danych z pliku!");
        }
    }

