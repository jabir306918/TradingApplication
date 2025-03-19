package orderservice;

import orderservice.model.Order;

import java.util.PriorityQueue;

public interface OrderDataProvider {

    public void addBuyingOrder(Order order);
    public void addSellingOrder(Order order);
    public PriorityQueue<Order> getBuyingOrderInQueueBySymbol(String symbol);
    public PriorityQueue<Order> getSellingOrderInQueueBySymbol(String symbol);
    public void updateOrder(String symbol, int orderId, int newQuantity, double newPrice);
    public void cancellOrder(String symbol, int order);
    public Order findOrderById(int orderId);

}
