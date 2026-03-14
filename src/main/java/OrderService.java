import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderService {
    private final List<Order> repository = new ArrayList<>();
    private final OrderProcessorTemplate processor;

    public OrderService(OrderProcessorTemplate processor) {
        this.processor = processor;
    }

    public Optional<Order> findById(String id) {
        for (Order order : repository) {
            if (order.getId().equals(id)) {
                return java.util.Optional.of(order);
            }
        }

        return Optional.empty();
    }
    }


