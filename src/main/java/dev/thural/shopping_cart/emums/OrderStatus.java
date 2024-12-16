package dev.thural.shopping_cart.emums;

public enum OrderStatus {
    PENDING,       // Order just created
    PROCESSING,    // Payment confirmed, preparing for shipment
    SHIPPED,       // Order has been sent out
    DELIVERED,     // Order received by customer
    CANCELLED,     // Order cancelled by customer or system
    REFUNDED       // Order returned and refunded
}