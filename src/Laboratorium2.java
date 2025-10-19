import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Scanner;

public class Laboratorium2 {
    //Przygotuj model danych (klasę) i zapis oraz odczyt jego instancji
    // do/z plików XML. Przygotuj listę instancji zapisz ją do XML-a,
    // wyświetl i odczytaj dane do listy z XML-a. - 6 p.
    private static final String BOOKS = "books";
    private static final String BOOK = "book";
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String YEAR = "year";
    private static final String PRICE = "price";

    private static List<Book> books = List.of(
            new Book("Władca Pierścieni", "J.R.R. Tolkien", 1954, 49.99),
            new Book("Hobbit", "J.R.R. Tolkien", 1937, 39.99),
            new Book("Harry Potter", "J.K. Rowling", 1997, 45.50),
            new Book("Inferno", "Dan Brown", 2013, 32.00),
            new Book("Kod Leonarda da Vinci", "Dan Brown", 2003, 29.99)
    );

    public static void Run()
    {

        Scanner sc = new Scanner(System.in);
        String fileName = "biblioteka.txt";

        while (true) {
            System.out.println("\n LABORATORIUM 2:");
            System.out.println("1. Zapis do pliku XML");
            System.out.println("2. Odczyt z pliku XML");
            System.out.println("0. Wyjście");
            System.out.print("Wybierz opcję: ");

            int opcja = sc.nextInt();
            sc.nextLine();

            switch (opcja) {
                case 1 -> writeToXML();
                case 2 -> readFromXML();
                case 0 -> {
                    System.out.println("Koniec programu ");
                    return;
                }
                default -> System.out.println("Niepoprawna opcja!");
            }
        }

    }

//    private static XMLEvent getXMLEvent(XMLStreamReader reader)
//        throws XMLStreamException {
//        return allocator.allocate(reader);
//    }

    private static void readFromXML()
    {
        String inputFile = "biblioteka.xml";
        List<Book> books = new ArrayList<>();

        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLStreamReader reader = null;

        String tag = null;

        String currentTitle = null;
        String currentAuthor = null;
        String currentYearStr = null;
        String currentPriceStr = null;

        try {
            reader = inputFactory.createXMLStreamReader(new FileInputStream(inputFile));
             while (reader.hasNext()){
                 int event = reader.next();

                 switch (event){
                     case XMLStreamConstants.START_ELEMENT -> {
                         String localName = reader.getLocalName();
                         if (BOOK.equals(localName)){
                             currentTitle = currentAuthor = currentYearStr = currentPriceStr = null;
                         }
                     }
                     case XMLStreamConstants.CHARACTERS -> {
                         tag = reader.getText().trim();
                     }
                     case XMLStreamConstants.END_ELEMENT -> {
                         String endLocalName = reader.getLocalName();
                         if (tag != null && !tag.isEmpty()){
                             switch (endLocalName) {
                                 case TITLE:
                                     currentTitle = tag;
                                     break;
                                 case AUTHOR:
                                     currentAuthor = tag;
                                     break;
                                 case PRICE:
                                     currentPriceStr = tag;
                                     break;
                                 case YEAR:
                                     currentYearStr = tag;
                                     break;
                             }

                             if (BOOK.equals(endLocalName)) {
                                 try {
                                     Book book = CreateBookFromXML(currentTitle, currentAuthor, currentYearStr, currentPriceStr);
                                     books.add(book);
                                 } catch (InvalidBookException ex) {
                                     System.err.println("[POMINIĘTO] " + ex.getMessage());
                                 }
                             }
                         }
                     }
                 }
             }
             for(Book book : books){
                 System.out.println(book);
             }
        } catch (XMLStreamException e) {
            System.err.println("Błąd w pliku xml");
        } catch (FileNotFoundException e) {
            System.err.println("Nie znaleziono pliku - spróbuj najpierw zapisać dane do pliku");
        }
    }

    private static Book CreateBookFromXML(String title, String author, String yearStr, String priceStr){
        if (title == null || title.isBlank())
            throw new InvalidBookException("Tytuł pusty – obiekt nie został utworzony");

        if (author == null || author.isBlank())
            throw new InvalidBookException("Autor pusty – obiekt nie został utworzony");

        int year;
        double price;

        try { year = Integer.parseInt(yearStr); }
        catch (Exception e) { throw new InvalidBookException("Błędny rok: '" + yearStr + "'", e); }

        try { price = Double.parseDouble(priceStr); }
        catch (Exception e) { throw new InvalidBookException("Błędna cena: '" + priceStr + "'", e); }

        if (price < 0)
            throw new InvalidBookException("Cena ujemna: " + price);

        return new Book(title, author, year, price);
    }


    private static void writeToXML(){
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

        String fileOutput = "biblioteka.xml";

        try {
            XMLStreamWriter writer = outputFactory.createXMLStreamWriter(new FileOutputStream(fileOutput));
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeCharacters("\n");

            writer.writeStartElement(BOOKS);
            writer.writeCharacters("\n");

            for (Book book : books){
                writer.writeStartElement(BOOK);

                writeElement(writer, TITLE, book.getTitle());
                writeElement(writer, AUTHOR, book.getAuthor());
                writeElement(writer, PRICE, Double.toString(book.getPrice()));
                writeElement(writer, YEAR, String.valueOf(book.getYear()));

                writer.writeEndElement();
                writer.writeCharacters("\n");
            }

            writer.writeEndElement();
            writer.writeEndDocument();

            System.out.println("Pomyślnie zapisano do pliku " + fileOutput);

        } catch (XMLStreamException e) {
            System.err.println("Błąd strumienia");
        } catch (FileNotFoundException e) {
            System.err.println("Nie znaleziono pliku");
        }
    }

    private static void writeElement(XMLStreamWriter writer, String name, String value) throws XMLStreamException {
        writer.writeStartElement(name);
        writer.writeCharacters(value);
        writer.writeEndElement();
    }

    private static class InvalidBookException extends RuntimeException {
        public InvalidBookException(String msg) { super(msg); }
        public InvalidBookException(String msg, Throwable cause) { super(msg, cause); }
    }
}
