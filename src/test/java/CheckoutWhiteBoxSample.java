import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CheckoutWhiteBoxSample {

    private Checkout checkout;

    @BeforeEach
    public void setUp() {
        checkout = new Checkout();
    }

    @Test
    public void testNullType() {
        assertEquals(0, checkout.countBooksByType(null, false));
    }

    @Test
    public void testAvailableBookOnly() {
        Book book = new Book("111", "Test", "Author", Book.BookType.FICTION, 1);

        checkout.addBook(book);

        assertEquals(1, checkout.countBooksByType(Book.BookType.FICTION, true));
    }

    @Test
    public void testUnavailableBookOnly() {
        Book book = new Book("222", "Test", "Author", Book.BookType.FICTION, 1);
        book.setAvailableCopies(0);

        checkout.addBook(book);

        assertEquals(0, checkout.countBooksByType(Book.BookType.FICTION, true));
    }

    @Test
    public void testCountAllBooksOfType() {
        Book book = new Book("333", "Test", "Author", Book.BookType.FICTION, 1);

        checkout.addBook(book);

        assertEquals(1, checkout.countBooksByType(Book.BookType.FICTION, false));
    }

    @Test
    public void testDifferentBookTypeDoesNotCount() {
        Book book = new Book("444", "Test", "Author", Book.BookType.TEXTBOOK, 1);

        checkout.addBook(book);

        assertEquals(0, checkout.countBooksByType(Book.BookType.FICTION, false));
    }
}