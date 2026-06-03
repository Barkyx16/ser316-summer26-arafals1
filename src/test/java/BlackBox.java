import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Constructor;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class BlackBox {

  private Checkout checkout;

/**
 * Runs the same tests on all checkout implementations.
 */
@SuppressWarnings("unchecked")
static Stream<Class<? extends Checkout>> checkoutClassProvider() {
    return (Stream<Class<? extends Checkout>>) Stream.of(
            Checkout0.class,
            Checkout1.class,
            Checkout2.class,
            Checkout3.class
    );
}

/**
 * Creates a checkout object from the class being tested.
 */
private Checkout createCheckout(Class<? extends Checkout> clazz) throws Exception {
    Constructor<? extends Checkout> constructor = clazz.getConstructor();
    return constructor.newInstance();
}

@ParameterizedTest
@MethodSource("checkoutClassProvider")
@DisplayName("T1: Null patron")
public void testNullPatron(Class<? extends Checkout> checkoutClass) throws Exception {

    checkout = createCheckout(checkoutClass);

    Book book = new Book(
            "978-0-1111-1111-1",
            "Test Book",
            "Test Author",
            Book.BookType.FICTION,
            1
    );

    double result = checkout.checkoutBook(book, null);

    assertEquals(3.1, result, 0.01);
    assertEquals(1, book.getAvailableCopies());
}

@ParameterizedTest
@MethodSource("checkoutClassProvider")
@DisplayName("T2 Null Book")
public void testNullBook(Class<? extends Checkout> checkoutClass) throws Exception {


checkout = createCheckout(checkoutClass);

Patron patron = new Patron(
        "P001",
        "Test Patron",
        "test@example.com",
        Patron.PatronType.STUDENT
);

double result = checkout.checkoutBook(null, patron);

assertEquals(2.1, result, 0.01);
assertEquals(0, patron.getCheckoutCount());


}

@ParameterizedTest
@MethodSource("checkoutClassProvider")
@DisplayName("T3 Suspended Patron")
public void testSuspendedPatron(Class<? extends Checkout> checkoutClass) throws Exception {


checkout = createCheckout(checkoutClass);

Book book = new Book(
        "978-0-2222-2222-2",
        "Test Book",
        "Test Author",
        Book.BookType.FICTION,
        1
);

Patron patron = new Patron(
        "P002",
        "Suspended Patron",
        "suspended@example.com",
        Patron.PatronType.STUDENT
);

patron.setAccountSuspended(true);

double result = checkout.checkoutBook(book, patron);

assertEquals(3.0, result, 0.01);
assertEquals(1, book.getAvailableCopies());
assertFalse(patron.hasBookCheckedOut(book.getIsbn()));


}

@ParameterizedTest
@MethodSource("checkoutClassProvider")
@DisplayName("T4 Three Overdue Books")
public void testThreeOverdueBooks(Class<? extends Checkout> checkoutClass) throws Exception {

checkout = createCheckout(checkoutClass);

Book book = new Book(
        "978-0-3333-3333-3",
        "Test Book",
        "Test Author",
        Book.BookType.FICTION,
        1
);

Patron patron = new Patron(
        "P003",
        "Overdue Patron",
        "overdue@example.com",
        Patron.PatronType.STUDENT
);

patron.setOverdueCount(3);

double result = checkout.checkoutBook(book, patron);

assertEquals(4.0, result, 0.01);
assertEquals(1, book.getAvailableCopies());
assertFalse(patron.hasBookCheckedOut(book.getIsbn()));

}

@ParameterizedTest
@MethodSource("checkoutClassProvider")
@DisplayName("T5 Four Overdue Books")
public void testFourOverdueBooks(Class<? extends Checkout> checkoutClass) throws Exception {

checkout = createCheckout(checkoutClass);

Book book = new Book(
        "978-0-4444-4444-4",
        "Test Book",
        "Test Author",
        Book.BookType.FICTION,
        1
);

Patron patron = new Patron(
        "P004",
        "Overdue Patron",
        "overdue2@example.com",
        Patron.PatronType.STUDENT
);

patron.setOverdueCount(4);

double result = checkout.checkoutBook(book, patron);

assertEquals(4.0, result, 0.01);
assertEquals(1, book.getAvailableCopies());
assertFalse(patron.hasBookCheckedOut(book.getIsbn()));

}



}
