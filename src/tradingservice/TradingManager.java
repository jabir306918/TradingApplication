package tradingservice;

import orderservice.OrderDataProvider;
import orderservice.model.Order;

import java.util.Objects;
import java.util.PriorityQueue;

public class TradingManager {

    private final OrderDataProvider orderDataProvider;
    private final TradeDataProvider tradeDataProvider;

    public TradingManager(OrderDataProvider orderDataProvider, TradeDataProvider tradeDataProvider) {
        this.orderDataProvider = orderDataProvider;
        this.tradeDataProvider = tradeDataProvider;
    }

    public void executeTrade(String symbol) {
        PriorityQueue<Order> buyOrders = orderDataProvider.getBuyingOrderInQueueBySymbol(symbol);
        PriorityQueue<Order> sellOrders = orderDataProvider.getSellingOrderInQueueBySymbol(symbol);

        while (Objects.nonNull(buyOrders) && Objects.nonNull(sellOrders) &&
                !buyOrders.isEmpty() && !sellOrders.isEmpty()) {
            Order buy = buyOrders.peek();
            Order sell = sellOrders.peek();
            if (buy.getPrice() >= sell.getPrice()) {
                int tradeQuantity = Math.min(buy.getQuantity(), sell.getQuantity());
                tradeDataProvider.addTrade(buy.getOrderId(), sell.getOrderId(), buy.getStockSymbol(), tradeQuantity, sell.getPrice());
                buy.setQuantity(buy.getQuantity()-tradeQuantity);
                sell.setQuantity(sell.getQuantity()-tradeQuantity);

                if (buy.getQuantity() == 0) buyOrders.poll();
                if (sell.getQuantity() == 0) sellOrders.poll();
            } else {
                break;
            }
        }
    }



}
