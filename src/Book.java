public class Book {
    private String title;
    private String author;
    private int year;
    private double price;

    public Book(String title, String author, int year, double price) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.price = price;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return year; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return title + " (" + author + ", " + year + ") - " + price + " zł";
    }
}
