package com.onlinestore.event;

public class CartEvent {

    private String userId;
    private String productId;
    private int quantity;
    private String type; // ADD, REMOVE, UPDATE

    public CartEvent() {}

    public CartEvent(String userId, String productId, int quantity, String type) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
