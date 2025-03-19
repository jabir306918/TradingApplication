import orderservice.OrderDataProvider;
import orderservice.OrderManager;
import orderservice.model.InMemOrderDataProvider;
import orderservice.model.Order;
import orderservice.model.OrderType;
import tradingservice.InMemTradeDataProvider;
import tradingservice.TradeDataProvider;
import tradingservice.TradingManager;
import tradingservice.model.Trade;

import javax.annotation.processing.SupportedSourceVersion;

public class TradingSystem {
    public static void main(String[] args) {
        OrderDataProvider orderDataProvider = new InMemOrderDataProvider();
        TradeDataProvider tradeDataProvider = new InMemTradeDataProvider();
        OrderManager orderManager = new OrderManager(orderDataProvider, new TradingManager(orderDataProvider, tradeDataProvider));

        // Usecase 1: Placing the order

        Order order1 = new Order(1, OrderType.BUY, "RELIANCE", 17, 1000);
        Order order2 = new Order(2, OrderType.SELL, "RELIANCE", 5, 1000);
        Order order3 = new Order(3, OrderType.SELL, "RELIANCE", 5, 950);
        Order order4 = new Order(4, OrderType.SELL, "RELIANCE", 5, 1200);
        orderManager.placeOrder(order1);
        orderManager.placeOrder(order2);
        orderManager.placeOrder(order3);
        orderManager.placeOrder(order4);

        for (Trade trade : tradeDataProvider.getAllTrade()) {
            System.out.println("Trade Executed: " + trade.getTradeId());
            System.out.println(" Buyer: " + trade.getBuyerOrderId());
            System.out.println(" Seller: " + trade.getSellerOrderId());
            System.out.println(" Price: " + trade.getPrice());
            System.out.println(" Quantity: " + trade.getQuantity());
        }

        // Usecase : 2 Getting status of order
        Order order1Status = orderManager.findOrder(order1.getOrderId());
        System.out.println("====================================");
        System.out.println("Order Id : "+ order1Status.getOrderId());
        System.out.println("Order Status : "+ order1Status.getStatus()); // This should be ACCEPTED
        System.out.println("Order Quantity : "+ order1Status.getQuantity()); // This should be 7 because 10 out 17 is executed.
        System.out.println("====================================");


        // Usecase : 3 Modifing the order
        // modifing order 4 price to lower so that remaining quantity of order 1 can be executed
        orderManager.modifyOrder(order4.getStockSymbol(), order4.getOrderId(), order4.getQuantity(), 970); // changing price from 1200 to 970
        order1Status = orderManager.findOrder(order1.getOrderId());
        System.out.println("====================================");
        System.out.println("Order Id : "+ order1Status.getOrderId());
        System.out.println("Order Status : "+ order1Status.getStatus()); // This should be ACCEPTED
        System.out.println("Order Quantity : "+ order1Status.getQuantity()); // This should be 2 because 5 more quantity is executed.
        System.out.println("====================================");


        // Usecase : 4 Cancel the order
        orderManager.cancelOrder(order1.getStockSymbol(), order1.getOrderId()); // changing price from 1200 to 970
        order1Status = orderManager.findOrder(order1.getOrderId());
        System.out.println("====================================");
        System.out.println("Order Id : "+ order1Status.getOrderId());
        System.out.println("Order Status : "+ order1Status.getStatus()); // This should be Cencelled
        System.out.println("Order Quantity : "+ order1Status.getQuantity()); // This should be 2 because no trade executed.
        System.out.println("====================================");

        
    }
}
