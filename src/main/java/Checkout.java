import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Checkout {
    public static final double MAX_FINE_AMOUNT = 25.0;
    private static final int OVERDUE_LIMIT = 3;
    private static final double FINE_LIMIT = 10.0;
    private static final int FIRST_WEEK = 7;
    private static final int SECOND_WEEK = 14;
    private static final double FIRST_RATE = 0.25;
    private static final double SECOND_RATE = 0.50;
    private static final int ISBN10_LENGTH = 10;
    private static final int ISBN13_LENGTH = 13;

    private Map<String, Book> bookList; 
    private Map<String, Patron> patrons; 
    private List<Transaction> history; 

    private static class Transaction {
        Patron patron;
        Book book;
        LocalDate checkoutDate;
        LocalDate dueDate;
        LocalDate returnDate;

                Transaction(Patron patron, Book book, LocalDate checkoutDate, LocalDate dueDate) {
            this.patron = patron;
            this.book = book;
            this.checkoutDate = checkoutDate;
            this.dueDate = dueDate;
            this.returnDate = null;
        }

        boolean hasDueDate() {
            return checkoutDate != null && dueDate != null;
        }
    }
   

    public Checkout() {
        this.bookList = new HashMap<>();
        this.patrons = new HashMap<>();
        this.history = new ArrayList<>();
    }
    public void addBook(Book book) {
        bookList.put(book.getIsbn(), book);
    }


    public void registerPatron(Patron patron) {
        patrons.put(patron.getPatronId(), patron);
    }

    public double validatePatronEligibility(Patron patron) {
        if (patron == null) {
            return 3.1;
        }
        if (patron.isAccountSuspended()) {
            return 3.0;
        }
        if (patron.getOverdueCount() >= OVERDUE_LIMIT) {
            return 4.0;
        }
        if (patron.getFineBalance() >= FINE_LIMIT) {
            return 4.1;
        }
        return 0.0; 
    }

    public double checkoutBook(Book book, Patron patron) {
    double patronCheck = validatePatronEligibility(patron);

    if (patronCheck != 0.0) {
        return patronCheck;
    }

    if (book == null) {
        return 2.1;
    }

    if (book.isReferenceOnly()) {
        return 5.0;
    }

    LocalDate dueDate =
        LocalDate.now().plusDays(patron.getLoanPeriodDays());

    if (patron.hasBookCheckedOut(book.getIsbn())) {
        patron.addCheckedOutBook(book.getIsbn(), dueDate);
        return 0.1;
    }

    if (book.getAvailableCopies() <= 0) {
    return 2.0;
}

    if (patron.getCheckoutCount() >= patron.getMaxCheckoutLimit()) {
        return 3.2;
    }

    patron.addCheckedOutBook(book.getIsbn(), dueDate);
    book.checkout();
    history.add(
        new Transaction(patron, book, LocalDate.now(), dueDate));

    if (patron.getOverdueCount() >= 1
        && patron.getOverdueCount() <= 2) {
    return 1.0;
}

if (patron.getCheckoutCount()
        >= patron.getMaxCheckoutLimit() - 2
        && patron.getCheckoutCount()
        < patron.getMaxCheckoutLimit()) {
    return 1.1;
}

    return 0.0;
}


    public double calculateFine(int numOfDays, Book.BookType bookType) {
        if (numOfDays <= 0) {
            return 0.0;
        }

        double fine = 0.0;

        
        int days1 = Math.min(numOfDays, FIRST_WEEK);
         fine += days1 * FIRST_RATE;

        
        if (numOfDays > FIRST_WEEK) {
            int days2 = Math.min(numOfDays - FIRST_WEEK, FIRST_WEEK);
             fine += days2 * SECOND_RATE;
        }

        
       if (numOfDays > SECOND_WEEK) {
          int days3 = numOfDays - SECOND_WEEK;
            fine += days3 * 1.00;
        }

        
        if (bookType == Book.BookType.REFERENCE || bookType == Book.BookType.TEXTBOOK) {
            fine *= 2.0;
        }

        
        return Math.min(fine, MAX_FINE_AMOUNT);
    }

    public boolean isValidISBN(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            return false;
        }


        String numbers = isbn.replace("-", "");

        
        if (!numbers.matches("\\d+")) {
            return false;
        }

        
        int length = numbers.length();
        return length == ISBN10_LENGTH
        || length == ISBN13_LENGTH;
    }

    public boolean isPatronType(String typeString, Patron.PatronType expectedType) {
        if (typeString == null || expectedType == null) {
            return false;
        }

        return typeString.equals(expectedType.toString());
    }

    public double returnBook(String isbn, Patron patron) {
        if (patron == null || !patron.hasBookCheckedOut(isbn)) {
            return -1.0;
        }

        Book book = bookList.get(isbn);
        if (book == null) {
            return -1.0;
        }

        LocalDate dueDate = patron.getCheckedOutBooks().get(isbn);
        LocalDate today = LocalDate.now();
        long daysOverdue = ChronoUnit.DAYS.between(dueDate, today);

        double fine = 0.0;
        if (daysOverdue > 0) {
            fine = calculateFine((int) daysOverdue, book.getType());
            patron.addFine(fine);
        }

        
        patron.removeCheckedOutBook(isbn);
        book.returnBook();

        
        for (Transaction t : history) {
            if (t.patron.equals(patron) && t.book.equals(book) && t.returnDate == null) {
                t.returnDate = today;
                break;
            }
        }

        return fine;
    }

    public int countBooksByType(Book.BookType type, boolean onlyAvailable) {

        if (type == null) {
            return 0;
        }

        int looped = 0;

        
        for (Book b : bookList.values()) {

            if (b == null) {
                continue;
            }

            
            if (b.getType() == type) {
                
                if (onlyAvailable) {
                    
                    if (b.isAvailable()) {
                        looped++;
                    }
                } else {
                    
                    looped++;
                }
            }
        }

        return looped;
    }

    public Map<String, Book> getInventory() {
    return new HashMap<>(bookList);
}
    public Map<String, Patron> getPatrons() {
    return new HashMap<>(patrons);
}
}
