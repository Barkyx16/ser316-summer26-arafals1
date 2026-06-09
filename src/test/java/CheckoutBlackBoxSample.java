import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Constructor;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CheckoutBlackBoxSample {

    private Checkout checkout;

    static Stream<Class<? extends Checkout>> checkoutClassProvider() {
        return Stream.of(Checkout.class);
    }

    private Checkout createCheckout(Class<? extends Checkout> checkoutClass) throws Exception {
        Constructor<? extends Checkout> constructor = checkoutClass.getConstructor();
        return constructor.newInstance();
    }

    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("Successful checkout returns 0.0")
    public void testBookAvailable(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        Book book = new Book(
                "978-0-123456-78-9",
                "Test Book",
                "Test Author",
                Book.BookType.FICTION,
                1
        );

        Patron patron = new Patron(
                "P001",
                "Test Patron",
                "test@example.com",
                Patron.PatronType.STUDENT
        );

        checkout.addBook(book);
        checkout.registerPatron(patron);

        double result = checkout.checkoutBook(book, patron);

        assertEquals(0.0, result, 0.01);
        assertFalse(book.isAvailable());
        assertTrue(patron.hasBookCheckedOut(book.getIsbn()));
        assertEquals(1, patron.getCheckoutCount());
    }

    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("Unavailable book returns 2.0")
    public void testUnavailableBook(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        Book book = new Book(
                "978-0-123456-78-9",
                "Test Book",
                "Test Author",
                Book.BookType.FICTION,
                5
        );

        book.setAvailableCopies(0);

        Patron patron = new Patron(
                "P001",
                "Test Patron",
                "test@example.com",
                Patron.PatronType.STUDENT
        );

        checkout.addBook(book);
        checkout.registerPatron(patron);

        double result = checkout.checkoutBook(book, patron);

        assertEquals(2.0, result, 0.01);
        assertFalse(patron.hasBookCheckedOut(book.getIsbn()));
    }
}