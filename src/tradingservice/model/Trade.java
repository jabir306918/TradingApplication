package tradingservice.model;

public class Trade {
    static int tradeCounter = 0;
    int tradeId;
    int buyerOrderId;
    int sellerOrderId;
    String stockSymbol;
    int quantity;
    double price;
    long timestamp;

    public Trade(int buyerOrderId, int sellerOrderId, String stockSymbol, int quantity, double price) {
        this.tradeId = ++tradeCounter;
        this.buyerOrderId = buyerOrderId;
        this.sellerOrderId = sellerOrderId;
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.price = price;
        this.timestamp = System.currentTimeMillis();
    }

    public int getTradeId() {
        return tradeId;
    }

    public int getBuyerOrderId() {
        return buyerOrderId;
    }

    public int getSellerOrderId() {
        return sellerOrderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}
