package tradingservice;

import tradingservice.model.Trade;

import java.util.List;

public interface TradeDataProvider {
    public void addTrade(int buyerOrderId, int sellerOrderId, String stockSymbol, int quantity, double price);
    public List<Trade> getAllTrade();
}
