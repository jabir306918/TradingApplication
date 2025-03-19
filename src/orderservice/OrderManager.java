package orderservice;

import orderservice.model.Order;
import orderservice.model.OrderType;
import tradingservice.TradingManager;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OrderManager {
    private final Lock lock = new ReentrantLock();
    private final OrderDataProvider orderDataProvider;
    private final TradingManager tradingManager;

    public OrderManager(OrderDataProvider orderDataProvider, TradingManager tradingManager) {
        this.orderDataProvider = orderDataProvider;
        this.tradingManager = tradingManager;
    }

    public void placeOrder(Order order) {
        lock.lock();
        try {
            if (order.getType() == OrderType.BUY) {
                orderDataProvider.addBuyingOrder(order);
            } else {
                orderDataProvider.addSellingOrder(order);
            }
            tradingManager.executeTrade(order.getStockSymbol());
        } finally {
            lock.unlock();
        }
    }

    public void modifyOrder(String symbol, int orderId, int newQuantity, double newPrice) {
        lock.lock();
        try {
            orderDataProvider.updateOrder(symbol, orderId, newQuantity, newPrice);
            tradingManager.executeTrade(symbol);
        } finally {
            lock.unlock();
        }
    }

    public void cancelOrder(String symbol, int orderId) {
        lock.lock();
        try {
            orderDataProvider.cancellOrder(symbol, orderId);
        } finally {
            lock.unlock();
        }
    }

    public Order findOrder(int orderId) {
        return orderDataProvider.findOrderById(orderId);
    }

}
