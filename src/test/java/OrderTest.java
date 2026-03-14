import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class OrderProcessingTest {
    private StandardOrderProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new StandardOrderProcessor();
    }

    // Positive test: Successful payment
    @Test
    void testSuccessfulCardPayment() {
        OrderItem[] items = {
                new OrderItem("Bread", "Food", new Money(50, "UAH")),
                new OrderItem("Milk", "Food", new Money(50, "UAH"))
        };
        Order order = new Order("10", items);
        assertDoesNotThrow(() -> processor.processOrder(order, new CardPayment()));
        assertEquals(OrderCond.PAID, order.getCondition());
    }

    // Negative test: Order must have at least 2 items
    @Test
    void testR10Constraint() {
        OrderItem[] items = { new OrderItem("Solo", "Misc", new Money(100, "UAH")) };
        Order order = new Order("33", items);
        assertThrows(AppException.class, () -> processor.processOrder(order, new CardPayment()));
    }

    // Negative test: Category mix not allowed
    @Test
    void testCategoryMixException() {
         StandardOrderProcessor processor = new StandardOrderProcessor();
        OrderItem[] items = {
                new OrderItem("Wine", "ALCOHOL", new Money(300, "UAH")),
                new OrderItem("Toy", "KIDS", new Money(150, "UAH"))
        };
        Order order = new Order("ORD-MIX", items);
        assertThrows(CategoryMixException.class, () -> processor.processOrder(order, new CardPayment()));
    }

    // Positive test: Discount application
    @Test
    void testDiscountApplication() {
        OrderItem[] items = {
                new OrderItem("A", "CAT1", new Money(100, "UAH")),
                new OrderItem("B", "CAT2", new Money(100, "UAH")),
                new OrderItem("C", "CAT3", new Money(100, "UAH"))
        };
        Order order = new Order("56", items);
        processor.processOrder(order, new BankTransferPayment());
        assertEquals(285.0, order.getTotalCost().amount(), 0.01);
    }

    // Negative test: Double payment
    @Test
    void testProhibitDoublePayment() {
        Order order = new Order("ORD-PAID", new OrderItem[]{
                new OrderItem("I1", "C1", new Money(100, "UAH")),
                new OrderItem("I2", "C1", new Money(100, "UAH"))
        }, OrderCond.PAID);

        assertThrows(PaymentException.class, () -> processor.processOrder(order, new CardPayment()));
    }

    // PayPal limit 200
    @ParameterizedTest
    @ValueSource(doubles = {50.0, 150.0, 199.99})
    void testPayPalMinLimit(double amount) {
        OrderItem[] items = {
                new OrderItem("Low", "C1", new Money(amount/2, "UAH")),
                new OrderItem("Price", "C1", new Money(amount/2, "UAH"))
        };
        Order order = new Order("ORD-LOW", items);
        assertThrows(PaymentException.class, () -> processor.processOrder(order, new PayPalPayment()));
    }

    // Negative Card limit 25000
    @Test
    void testCardMaxLimit() {
        OrderItem[] items = {
                new OrderItem("Luxury", "C1", new Money(26000, "UAH")),
                new OrderItem("Luxury 2", "C1", new Money(100, "UAH"))
        };
        Order order = new Order("ORD-HIGH", items);
        assertThrows(PaymentException.class, () -> processor.processOrder(order, new CardPayment()));
    }

    // equals/hashCode
    @Test
    void testOrderEquality() {
        Order o1 = new Order("123", new OrderItem[]{});
        Order o2 = new Order("123", new OrderItem[]{});
        assertEquals(o1, o2);
        assertEquals(o1.hashCode(), o2.hashCode());
    }

    // Defensive Copy
    @Test
    void testDefensiveCopy() {
        OrderItem item = new OrderItem("A", "B", new Money(10, "UAH"));
        OrderItem[] items = {item, item};
        Order order = new Order("ID", items);
        items[0] = null;
        assertNotNull(order.getItems()[0]);
    }

    // Positive test: Bank transfer payment
    @Test
    void testBankTransferProcess() {
        OrderItem[] items = {
                new OrderItem("Item1", "C1", new Money(1000, "UAH")),
                new OrderItem("Item2", "C1", new Money(1000, "UAH"))
        };
        Order order = new Order("33", items);
        assertDoesNotThrow(() -> processor.processOrder(order, new BankTransferPayment()));
    }
}
