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


}
