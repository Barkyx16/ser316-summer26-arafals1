import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.time.LocalDate;

import java.lang.reflect.Constructor;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class BlackBox {

    private Checkout checkout;

    static Stream<Class<? extends Checkout>> checkoutClassProvider() {
        return (Stream<Class<? extends Checkout>>) Stream.of(
                Checkout0.class,
                Checkout1.class,
                Checkout2.class,
                Checkout3.class
        );
    }

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

    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T6 Fine At Limit")
    public void testFineAtLimit(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        Book book = new Book("978-0-5555-5555-5",
                "Test Book", "Test Author", Book.BookType.FICTION, 1);

        Patron patron = new Patron("P005", "Fine Patron",
                "fine@example.com", Patron.PatronType.STUDENT);

        patron.addFine(10.00);

        double result = checkout.checkoutBook(book, patron);

        assertEquals(4.1, result, 0.01);
        assertEquals(1, book.getAvailableCopies());
        assertFalse(patron.hasBookCheckedOut(book.getIsbn()));
    }

    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T7 Fine Above Limit")
    public void testFineAboveLimit(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        Book book = new Book("978-0-6666-6666-6",
                "Test Book", "Test Author", Book.BookType.FICTION, 1);

        Patron patron = new Patron("P006", "Fine Patron",
                "fine2@example.com", Patron.PatronType.STUDENT);

        patron.addFine(10.01);

        double result = checkout.checkoutBook(book, patron);

        assertEquals(4.1, result, 0.01);
        assertEquals(1, book.getAvailableCopies());
        assertFalse(patron.hasBookCheckedOut(book.getIsbn()));
    }

    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T8 Fine Below Limit")
    public void testFineBelowLimit(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        Book book = new Book("978-0-7777-7777-7",
                "Test Book", "Test Author", Book.BookType.FICTION, 2);

        Patron patron = new Patron("P007", "Fine Patron",
                "fine3@example.com", Patron.PatronType.STUDENT);

        patron.addFine(9.99);

        double result = checkout.checkoutBook(book, patron);

        assertEquals(0.0, result, 0.01);
        assertTrue(patron.hasBookCheckedOut(book.getIsbn()));
        assertEquals(1, book.getAvailableCopies());
    }

    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T9 Unavailable Book")
    public void testUnavailableBook(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        Book book = new Book("978-0-8888-8888-8",
                "Test Book", "Test Author", Book.BookType.FICTION, 1);

        book.setAvailableCopies(0);

        Patron patron = new Patron("P008", "Test Patron",
                "test8@example.com", Patron.PatronType.STUDENT);

        double result = checkout.checkoutBook(book, patron);

        assertEquals(2.0, result, 0.01);
        assertFalse(patron.hasBookCheckedOut(book.getIsbn()));
        assertEquals(0, book.getAvailableCopies());
    }

    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T10 Reference Book")
    public void testReferenceBook(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        Book book = new Book("978-0-9999-9999-9",
                "Reference Book", "Test Author", Book.BookType.REFERENCE, 1);

        Patron patron = new Patron("P009", "Test Patron",
                "test9@example.com", Patron.PatronType.STUDENT);

        double result = checkout.checkoutBook(book, patron);

        assertEquals(5.0, result, 0.01);
        assertFalse(patron.hasBookCheckedOut(book.getIsbn()));
        assertEquals(0, book.getAvailableCopies());
    }

    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T11 Normal Checkout")
    public void testNormalCheckout(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        Book book = new Book("978-1-1111-1111-1",
                "Normal Book", "Test Author", Book.BookType.FICTION, 2);

        Patron patron = new Patron("P010", "Normal Patron",
                "normal@example.com", Patron.PatronType.STUDENT);

        double result = checkout.checkoutBook(book, patron);

        assertEquals(0.0, result, 0.01);
        assertTrue(patron.hasBookCheckedOut(book.getIsbn()));
        assertEquals(1, book.getAvailableCopies());
    }

    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T12 Renewal")
    public void testRenewal(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        Book book = new Book("978-1-2222-2222-2",
                "Renewal Book", "Test Author", Book.BookType.FICTION, 2);

        Patron patron = new Patron("P011", "Renewal Patron",
                "renewal@example.com", Patron.PatronType.STUDENT);

        patron.addCheckedOutBook(book.getIsbn(), LocalDate.now().minusDays(1));
        int copiesBefore = book.getAvailableCopies();

        double result = checkout.checkoutBook(book, patron);

        assertEquals(0.1, result, 0.01);
        assertTrue(patron.hasBookCheckedOut(book.getIsbn()));
        assertEquals(copiesBefore, book.getAvailableCopies());
        assertEquals(LocalDate.now().plusDays(patron.getLoanPeriodDays()),
                patron.getCheckedOutBooks().get(book.getIsbn()));
    }

    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T13 One Overdue Warning")
    public void testOneOverdueWarning(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        Book book = new Book("978-1-3333-3333-3",
                "Warning Book", "Test Author", Book.BookType.FICTION, 2);

        Patron patron = new Patron("P012", "Warning Patron",
                "warning@example.com", Patron.PatronType.STUDENT);

        patron.setOverdueCount(1);

        double result = checkout.checkoutBook(book, patron);

        assertEquals(1.0, result, 0.01);
        assertTrue(patron.hasBookCheckedOut(book.getIsbn()));
        assertEquals(1, book.getAvailableCopies());
    }

    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T14 Two Overdue Warning")
    public void testTwoOverdueWarning(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        Book book = new Book("978-1-4444-4444-4",
                "Warning Book", "Test Author", Book.BookType.FICTION, 2);

        Patron patron = new Patron("P013", "Warning Patron",
                "warning2@example.com", Patron.PatronType.STUDENT);

        patron.setOverdueCount(2);

        double result = checkout.checkoutBook(book, patron);

        assertEquals(1.0, result, 0.01);
        assertTrue(patron.hasBookCheckedOut(book.getIsbn()));
        assertEquals(1, book.getAvailableCopies());
    }

    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T15 Student At Max Limit")
    public void testStudentAtMaxLimit(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        Book book = new Book("978-1-5555-5555-5",
                "Limit Book", "Test Author", Book.BookType.FICTION, 1);

        Patron patron = new Patron("P014", "Limit Patron",
                "limit@example.com", Patron.PatronType.STUDENT);

        for (int i = 0; i < 10; i++) {
            patron.addCheckedOutBook("OLD-" + i, LocalDate.now().plusDays(30));
        }

        double result = checkout.checkoutBook(book, patron);

        assertEquals(3.2, result, 0.01);
        assertFalse(patron.hasBookCheckedOut(book.getIsbn()));
        assertEquals(1, book.getAvailableCopies());
    }

    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T16 Student Below Max Limit")
    public void testStudentBelowMaxLimit(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        Book book = new Book("978-1-6666-6666-6",
                "Limit Book", "Test Author", Book.BookType.FICTION, 1);

        Patron patron = new Patron("P015", "Limit Patron",
                "limit2@example.com", Patron.PatronType.STUDENT);

        for (int i = 0; i < 9; i++) {
            patron.addCheckedOutBook("OLD-" + i, LocalDate.now().plusDays(30));
        }

        double result = checkout.checkoutBook(book, patron);

        assertEquals(1.1, result, 0.01);
        assertTrue(patron.hasBookCheckedOut(book.getIsbn()));
        assertEquals(0, book.getAvailableCopies());
    }

    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T17 Student Near Limit Warning")
    public void testStudentNearLimitWarning(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        Book book = new Book("978-1-7777-7777-7",
                "Near Limit Book", "Test Author", Book.BookType.FICTION, 1);

        Patron patron = new Patron("P016", "Near Limit Patron",
                "near@example.com", Patron.PatronType.STUDENT);

        for (int i = 0; i < 7; i++) {
            patron.addCheckedOutBook("OLD-" + i, LocalDate.now().plusDays(30));
        }

        double result = checkout.checkoutBook(book, patron);

        assertEquals(1.1, result, 0.01);
        assertEquals(8, patron.getCheckoutCount());
        assertEquals(0, book.getAvailableCopies());
    }

    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T18 Student No Limit Warning")
    public void testStudentNoLimitWarning(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        Book book = new Book("978-1-8888-8888-8",
                "No Warning Book", "Test Author", Book.BookType.FICTION, 1);

        Patron patron = new Patron("P017", "No Warning Patron",
                "nowarning@example.com", Patron.PatronType.STUDENT);

        for (int i = 0; i < 6; i++) {
            patron.addCheckedOutBook("OLD-" + i, LocalDate.now().plusDays(30));
        }

        double result = checkout.checkoutBook(book, patron);

        assertEquals(0.0, result, 0.01);
        assertEquals(7, patron.getCheckoutCount());
        assertEquals(0, book.getAvailableCopies());
    }

    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T19 Faculty At Max Limit")
    public void testFacultyAtMaxLimit(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        Book book = new Book("978-1-9999-9999-9",
                "Faculty Book", "Test Author", Book.BookType.FICTION, 1);

        Patron patron = new Patron("P018", "Faculty Patron",
                "faculty@example.com", Patron.PatronType.FACULTY);

        for (int i = 0; i < 20; i++) {
            patron.addCheckedOutBook("OLD-" + i, LocalDate.now().plusDays(60));
        }

        double result = checkout.checkoutBook(book, patron);

        assertEquals(3.2, result, 0.01);
        assertFalse(patron.hasBookCheckedOut(book.getIsbn()));
        assertEquals(1, book.getAvailableCopies());
    }

    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T20 Public At Max Limit")
    public void testPublicAtMaxLimit(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        Book book = new Book("978-2-1111-1111-1",
                "Public Book", "Test Author", Book.BookType.FICTION, 1);

        Patron patron = new Patron("P019", "Public Patron",
                "public@example.com", Patron.PatronType.PUBLIC);

        for (int i = 0; i < 5; i++) {
            patron.addCheckedOutBook("OLD-" + i, LocalDate.now().plusDays(21));
        }

        double result = checkout.checkoutBook(book, patron);

        assertEquals(3.2, result, 0.01);
        assertFalse(patron.hasBookCheckedOut(book.getIsbn()));
        assertEquals(1, book.getAvailableCopies());
    }

    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T21 Child At Max Limit")
    public void testChildAtMaxLimit(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        Book book = new Book("978-2-2222-2222-2",
                "Child Book", "Test Author", Book.BookType.CHILDREN, 1);

        Patron patron = new Patron("P020", "Child Patron",
                "child@example.com", Patron.PatronType.CHILD);

        for (int i = 0; i < 3; i++) {
            patron.addCheckedOutBook("OLD-" + i, LocalDate.now().plusDays(14));
        }

        double result = checkout.checkoutBook(book, patron);

        assertEquals(3.2, result, 0.01);
        assertFalse(patron.hasBookCheckedOut(book.getIsbn()));
        assertEquals(1, book.getAvailableCopies());
    }

    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T22 Patron Checked Before Book")
    public void testValidationOrderPatronBeforeBook(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        double result = checkout.checkoutBook(null, null);

        assertEquals(3.1, result, 0.01);
    }

}