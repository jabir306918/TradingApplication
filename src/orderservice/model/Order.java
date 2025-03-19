package orderservice.model;

public class Order {
    static int orderCounter = 0;
    int orderId;
    int userId;
    OrderType type;
    String stockSymbol;
    int quantity;
    double price;
    long timestamp;
    Status status;

    public Order() {
        this.orderId = ++orderCounter;
    }

    public Order(int userId, OrderType type, String stockSymbol, int quantity, double price) {
        this.orderId = ++orderCounter;
        this.userId = userId;
        this.type = type;
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.price = price;
        this.timestamp = System.currentTimeMillis();
        this.status = Status.ACCEPTED;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public OrderType getType() {
        return type;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Status getStatus() {
        return status;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }
}
