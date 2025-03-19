package orderservice.model;

import orderservice.OrderDataProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class InMemOrderDataProvider implements OrderDataProvider {

    private final HashMap<String, PriorityQueue<Order>> buyOrdersMap = new HashMap<>();;
    private final HashMap<String, PriorityQueue<Order>> sellOrdersMap = new HashMap<>();
    private final List<Order> cancelledOrder = new ArrayList<>();
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private static final long EXPIRY_TIME = 60000; // 60 seconds

    public InMemOrderDataProvider() {
        startOrderExpiryTask();
    }

    private void startOrderExpiryTask() {
        executor.scheduleAtFixedRate(() -> {
            long currentTime = System.currentTimeMillis();
            for (String key: buyOrdersMap.keySet()) {
                buyOrdersMap.get(key).removeIf(order -> (currentTime - order.getTimestamp()) > EXPIRY_TIME);
            }
            for (String key: sellOrdersMap.keySet()) {
                sellOrdersMap.get(key).removeIf(order -> (currentTime - order.getTimestamp()) > EXPIRY_TIME);
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    @Override
    synchronized
    public void addBuyingOrder(Order order) {
        PriorityQueue<Order> queue = buyOrdersMap.get(order.getStockSymbol());
        if (Objects.isNull(queue)) {
            queue = initBuyingOrderPriorityQueue();
            buyOrdersMap.put(order.getStockSymbol(), queue);
        }
        queue.offer(order);
    }

    @Override
    public void addSellingOrder(Order order) {
        PriorityQueue<Order> queue = sellOrdersMap.get(order.getStockSymbol());
        if (Objects.isNull(queue)) {
            queue = initSellingOrderPriorityQueue();
            sellOrdersMap.put(order.getStockSymbol(), queue);
        }
        queue.offer(order);
    }

    @Override
    public PriorityQueue<Order> getBuyingOrderInQueueBySymbol(String symbol) {
        return buyOrdersMap.get(symbol);
    }

    @Override
    public PriorityQueue<Order> getSellingOrderInQueueBySymbol(String symbol) {
        return sellOrdersMap.get(symbol);
    }

    @Override
    public void updateOrder(String symbol, int orderId, int newQuantity, double newPrice) {
        Order order = findOrder(symbol, orderId);
        if (order != null) {
            order.setQuantity(newQuantity);
            order.setPrice(newPrice);
            order.setTimestamp(System.currentTimeMillis());
        }
    }

    @Override
    public void cancellOrder(String symbol, int orderId) {
        Order order = findOrder(symbol, orderId);
        if (order != null) {
            order.setStatus(Status.CANCELLED);
            PriorityQueue<Order> buyQueue = buyOrdersMap.get(symbol);
            while (Objects.nonNull(buyQueue) && !buyQueue.isEmpty() && buyQueue.peek().getStatus().equals(Status.CANCELLED)) {
                cancelledOrder.add(buyQueue.poll());
            }
            while (Objects.nonNull(buyQueue) && !buyQueue.isEmpty() && buyQueue.peek().getStatus().equals(Status.CANCELLED)) {
                cancelledOrder.add(buyQueue.poll());
            }
        }
    }

    @Override
    public Order findOrderById(int orderId) {
        Optional<Order> order;
        order = cancelledOrder.stream().filter(o -> o.getOrderId() == orderId).findFirst();
        if (order.isPresent()) {
            return order.get();
        }
        for (String key : buyOrdersMap.keySet()) {
            PriorityQueue<Order> queue = buyOrdersMap.get(key);
            order = queue.stream().filter(o -> o.getOrderId() == orderId).findFirst();
            if (order.isPresent()) {
                return order.get();
            }
        }
        for (String key : sellOrdersMap.keySet()) {
            PriorityQueue<Order> queue = sellOrdersMap.get(key);
            order = queue.stream().filter(o -> o.getOrderId() == orderId).findFirst();
            if (order.isPresent()) {
                return order.get();
            }
        }
        return null;
    }

    private Order findOrder(String symbol, int orderId) {
        PriorityQueue<Order> buyQueue = buyOrdersMap.get(symbol);
        if (Objects.nonNull(buyQueue)) {
            Optional<Order> order = buyQueue.stream().filter(o -> o.getOrderId() == orderId).findFirst();
            if (order.isPresent()) {
                return order.get();
            }
        }
        PriorityQueue<Order> sellQueue = sellOrdersMap.get(symbol);
        if (Objects.nonNull(sellQueue)) {
            Optional<Order> order = sellQueue.stream().filter(o -> o.getOrderId() == orderId).findFirst();
            if (order.isPresent()) {
                return order.get();
            }
        }
        return null;
    }

    private PriorityQueue<Order> initBuyingOrderPriorityQueue() {
        return new PriorityQueue<>((o1, o2) -> o1.getPrice() == o2.getPrice() ? Long.compare(o1.getTimestamp(), o2.getTimestamp()) : Double.compare(o2.getPrice(), o1.getPrice()));
    }

    private PriorityQueue<Order> initSellingOrderPriorityQueue() {
        return new PriorityQueue<>((o1, o2) -> o1.getPrice() == o2.getPrice() ? Long.compare(o1.getTimestamp(), o2.getTimestamp()) : Double.compare(o1.getPrice(), o2.getPrice()));
    }

}
