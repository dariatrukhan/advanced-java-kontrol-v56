import java.util.Arrays;
import java.util.Objects;

public class Order {
    private final String id;
    private OrderCond condition;
    private final OrderItem[] items;
    private Money totalCost;

    public Order(String id, OrderItem[] items) {
        this(id, items, OrderCond.NEW);
    }

    public Order(String id, OrderItem[] items, OrderCond condition) {
        this.id = id;
        this.items = Arrays.copyOf(items, items.length);
        this.condition = condition;
        this.totalCost = calculateInitialTotal();
    }

    private Money calculateInitialTotal() {
        double sum = 0;
        for (OrderItem item : items) {
            sum += item.getPrice().amount();
        }
        return new Money(sum, "UAH");
    }


    public String getId() {
        return id;
    }
    public OrderCond getCondition() {
        return condition;
    }
    public void setCondition(OrderCond condition) {
        this.condition = condition;
    }
    public OrderItem[] getItems() {
        return Arrays.copyOf(items, items.length);
    }
    public Money getTotalCost() {
        return totalCost;
    }
    public void setTotalCost(Money totalCost) {
        this.totalCost = totalCost;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Objects.equals(id,order.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Order: id=" + id + ", status=" + condition + ", total=" + totalCost.amount() ;
    }
}

