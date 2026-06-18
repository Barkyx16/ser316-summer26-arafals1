import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class CheckoutWhiteBoxSample {

    private Checkout checkout;

    @BeforeEach
    void setUp() {
        checkout = new Checkout();
    }

    private Book book(String isbn, Book.BookType type, int copies) {
        return new Book(isbn, "Book", "Author", type, copies);
    }

    private Patron student(String id) {
        return new Patron(id, "Student", "student@email.com", Patron.PatronType.STUDENT);
    }

@Test
    void testNullType() {
        assertEquals(0, checkout.countBooksByType(null, false));
    }

@Test
    void testAvailableBookCounted() {
        checkout.addBook(book("111", Book.BookType.FICTION, 1));

        assertEquals(1, checkout.countBooksByType(Book.BookType.FICTION, true));
    }

@Test
    void testUnavailableBookNotCounted() {
        Book b = book("222", Book.BookType.FICTION, 1);
        b.setAvailableCopies(0);

        checkout.addBook(b);

        assertEquals(0, checkout.countBooksByType(Book.BookType.FICTION, true));
    }

@Test
    void testCountAllMatchingBooks() {
        checkout.addBook(book("333", Book.BookType.FICTION, 1));

        assertEquals(1, checkout.countBooksByType(Book.BookType.FICTION, false));
    }

@Test
    void testWrongTypeNotCounted() {
        checkout.addBook(book("444", Book.BookType.TEXTBOOK, 1));

        assertEquals(0, checkout.countBooksByType(Book.BookType.FICTION, false));
    }

@Test
    void testFiveBooksInLoop() {
        checkout.addBook(book("501", Book.BookType.FICTION, 1));
        checkout.addBook(book("502", Book.BookType.FICTION, 1));
        checkout.addBook(book("503", Book.BookType.TEXTBOOK, 1));
        checkout.addBook(book("504", Book.BookType.CHILDREN, 1));
        checkout.addBook(book("505", Book.BookType.FICTION, 1));

        assertEquals(3, checkout.countBooksByType(Book.BookType.FICTION, false));
    }

@Test
    void testNullBookInInventory() {
        checkout.getInventory().put("bad", null);
        checkout.addBook(book("601", Book.BookType.FICTION, 1));

        assertEquals(1, checkout.countBooksByType(Book.BookType.FICTION, false));
    }

@Test
    void testValidateNullPatron() {
        assertEquals(3.1, checkout.validatePatronEligibility(null), 0.01);
    }

@Test
    void testValidateSuspendedPatron() {
        Patron p = student("P1");
        p.setAccountSuspended(true);

        assertEquals(3.0, checkout.validatePatronEligibility(p), 0.01);
    }

@Test
    void testValidateOverduePatron() {
        Patron p = student("P2");
        p.setOverdueCount(3);

        assertEquals(4.0, checkout.validatePatronEligibility(p), 0.01);
    }

@Test
    void testValidateFineLimit() {
        Patron p = student("P3");
        p.addFine(10.0);

        assertEquals(4.1, checkout.validatePatronEligibility(p), 0.01);
    }

@Test
    void testValidateGoodPatron() {
        Patron p = student("P4");

        assertEquals(0.0, checkout.validatePatronEligibility(p), 0.01);
    }

@Test
    void testCheckoutNullBook() {
        Patron p = student("P5");

        assertEquals(2.1, checkout.checkoutBook(null, p), 0.01);
    }

@Test
    void testCheckoutReferenceBook() {
        Book b = book("701", Book.BookType.REFERENCE, 1);
        Patron p = student("P6");

        assertEquals(5.0, checkout.checkoutBook(b, p), 0.01);
    }

@Test
    void testCheckoutUnavailableBook() {
        Book b = book("702", Book.BookType.FICTION, 1);
        b.setAvailableCopies(0);

        Patron p = student("P7");

        assertEquals(2.0, checkout.checkoutBook(b, p), 0.01);
    }

@Test
    void testCheckoutRenewal() {
        Book b = book("703", Book.BookType.FICTION, 1);
        Patron p = student("P8");

        p.addCheckedOutBook(b.getIsbn(), LocalDate.now().minusDays(1));

        assertEquals(0.1, checkout.checkoutBook(b, p), 0.01);
    }

@Test
    void testCheckoutAtLimit() {
        Book b = book("704", Book.BookType.FICTION, 1);
        Patron p = student("P9");

        for (int i = 0; i < p.getMaxCheckoutLimit(); i++) {
            p.addCheckedOutBook("book" + i, LocalDate.now().plusDays(30));
        }

        assertEquals(3.2, checkout.checkoutBook(b, p), 0.01);
    }

@Test
    void testCheckoutOverdueWarning() {
        Book b = book("705", Book.BookType.FICTION, 1);
        Patron p = student("P10");

        p.setOverdueCount(1);

        assertEquals(1.0, checkout.checkoutBook(b, p), 0.01);
    }

@Test
    void testCheckoutNearLimitWarning() {
        Book b = book("706", Book.BookType.FICTION, 1);
        Patron p = student("P11");

        for (int i = 0; i < 7; i++) {
            p.addCheckedOutBook("near" + i, LocalDate.now().plusDays(30));
        }

        assertEquals(1.1, checkout.checkoutBook(b, p), 0.01);
    }

@Test
    void testCheckoutSuccess() {
        Book b = book("707", Book.BookType.FICTION, 1);
        Patron p = student("P12");

        assertEquals(0.0, checkout.checkoutBook(b, p), 0.01);
        assertTrue(p.hasBookCheckedOut(b.getIsbn()));
    }

@Test
    void testFineNoDays() {
        assertEquals(0.0, checkout.calculateFine(0, Book.BookType.FICTION), 0.01);
    }

@Test
    void testFineFirstWeek() {
        assertEquals(1.25, checkout.calculateFine(5, Book.BookType.FICTION), 0.01);
    }

@Test
    void testFineSecondWeek() {
        assertEquals(3.25, checkout.calculateFine(10, Book.BookType.NONFICTION), 0.01);
    }

@Test
    void testFineAfterTwoWeeks() {
        assertEquals(6.25, checkout.calculateFine(15, Book.BookType.FICTION), 0.01);
    }

@Test
    void testFineForTextbook() {
        assertEquals(6.50, checkout.calculateFine(10, Book.BookType.TEXTBOOK), 0.01);
    }

@Test
    void testFineMax() {
        assertEquals(25.0, checkout.calculateFine(50, Book.BookType.FICTION), 0.01);
    }

@Test
    void testGoodIsbn10() {
        assertTrue(checkout.isValidISBN("0123456789"));
    }

@Test
    void testGoodIsbn13() {
        assertTrue(checkout.isValidISBN("9780123456789"));
    }

@Test
    void testBadIsbnLetters() {
        assertFalse(checkout.isValidISBN("978ABC456789"));
    }

@Test
    void testBadIsbnLength() {
        assertFalse(checkout.isValidISBN("12345"));
    }

@Test
    void testBadIsbnNull() {
        assertFalse(checkout.isValidISBN(null));
    }

@Test
    void testPatronTypeMatch() {
        assertTrue(checkout.isPatronType("STUDENT", Patron.PatronType.STUDENT));
    }

@Test
    void testPatronTypeNoMatch() {
        assertFalse(checkout.isPatronType("PUBLIC", Patron.PatronType.STUDENT));
    }

@Test
    void testPatronTypeNull() {
        assertFalse(checkout.isPatronType(null, Patron.PatronType.STUDENT));
    }

@Test
    void testReturnNullPatron() {
        assertEquals(-1.0, checkout.returnBook("900", null), 0.01);
    }

@Test
    void testReturnBookNotCheckedOut() {
        Patron p = student("P13");

        assertEquals(-1.0, checkout.returnBook("901", p), 0.01);
    }

@Test
    void testReturnBookNotInInventory() {
        Patron p = student("P14");

        p.addCheckedOutBook("902", LocalDate.now().plusDays(1));

        assertEquals(-1.0, checkout.returnBook("902", p), 0.01);
    }

@Test
    void testReturnBookOnTime() {
        Book b = book("903", Book.BookType.FICTION, 1);
        Patron p = student("P15");

        checkout.addBook(b);
        p.addCheckedOutBook(b.getIsbn(), LocalDate.now().plusDays(1));

        assertEquals(0.0, checkout.returnBook(b.getIsbn(), p), 0.01);
        assertFalse(p.hasBookCheckedOut(b.getIsbn()));
    }

@Test
    void testReturnBookLate() {
        Book b = book("904", Book.BookType.FICTION, 1);
        Patron p = student("P16");

        checkout.addBook(b);
        p.addCheckedOutBook(b.getIsbn(), LocalDate.now().minusDays(5));

        assertEquals(1.25, checkout.returnBook(b.getIsbn(), p), 0.01);
    }
    
@Test
    void testCheckoutNullPatron() {
        Book b = book("999", Book.BookType.FICTION, 1);

        assertEquals(3.1, checkout.checkoutBook(b, null), 0.01);
    }
}