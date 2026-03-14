import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class OrderProcessorTemplate {
    private static final Logger log = LogManager.getLogger(OrderProcessorTemplate.class);
    public final void processOrder(Order order, PaymentMethod paymentMethod) {
        log.info("Starting process for order{}" ,order.getId());

        validateOrder(order);
        validateCategoryMix(order);
        calculateDiscount(order);
        executePayment(order, paymentMethod);
        notifyCustomer(order);

        log.info("Process finished for order{}" ,order.getId());
    }

    protected void validateOrder(Order order) {
        if (order.getItems().length < 2) {
            log.warn("Validation failed for order{}" ,order.getId());
            throw new AppException("Order must have at least 2 items");
        }
    }

    protected abstract void validateCategoryMix(Order order);
    protected void calculateDiscount(Order order) {
        OrderItem[] items = order.getItems();
        java.util.List<String> categories = new java.util.ArrayList<>();

        for (OrderItem item : items) {
            String category = item.getCategory();
            if (!categories.contains(category)) {
                categories.add(category);
            }
        }
        int uniqueCount = categories.size();
        if (uniqueCount >= 3) {
            Money discounted = order.getTotalCost().multiply(0.95);
            order.setTotalCost(discounted);
            log.info("5 percent discount applied, unique categories{}" ,uniqueCount);
        }
    }

    private void executePayment(Order order, PaymentMethod paymentMethod) {
        if (order.getCondition() != OrderCond.NEW) {
            throw new PaymentException("Double payment prohibited! Current status: " + order.getCondition());
        }
        paymentMethod.pay(order.getTotalCost());
        order.setCondition(OrderCond.PAID);
    }
    protected abstract void notifyCustomer(Order order);
}
 class StandardOrderProcessor extends OrderProcessorTemplate {
     private static final Logger log = LogManager.getLogger(StandardOrderProcessor.class);
    @Override

    protected void validateCategoryMix(Order order) {
        java.util.List<String> cats = new java.util.ArrayList<>();
        for (OrderItem item : order.getItems()) {
            cats.add(item.getCategory());
        }
        if (cats.contains("Alcohol") && cats.contains("Kids")) {
            throw new CategoryMixException("Cannot mix Alcohol and Kids!");
        }
    }

    @Override
    protected void notifyCustomer(Order order) {
        Email email = new Email(
                order.getId(),
                String.valueOf(order.getTotalCost().amount())
        );
        log.info("Email sent for order{}" ,order.getId());

    }
}
