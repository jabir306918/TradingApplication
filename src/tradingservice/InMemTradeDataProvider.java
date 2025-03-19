package tradingservice;

import tradingservice.model.Trade;

import java.util.ArrayList;
import java.util.List;

public class InMemTradeDataProvider implements TradeDataProvider{

    List<Trade> trades = new ArrayList<>();

    @Override
    public void addTrade(int buyerOrderId, int sellerOrderId, String stockSymbol, int quantity, double price) {
        trades.add(new Trade(buyerOrderId, sellerOrderId, stockSymbol, quantity, price));
    }

    @Override
    public List<Trade> getAllTrade() {
        return trades;
    }
}
