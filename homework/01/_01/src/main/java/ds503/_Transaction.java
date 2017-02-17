package ds503;


public final class _Transaction {

    private int id;
    private int customerId;
    private float total;
    private int itemCount;
    private String description;

    public _Transaction(int id, int customerId, float total, int itemCount, String description) {
        this.id = id;
        this.customerId = customerId;
        this.total = total;
        this.itemCount = itemCount;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public float getTotal() {
        return total;
    }

    public int getItemCount() {
        return itemCount;
    }

    public String getDescription() {
        return description;
    }
}
