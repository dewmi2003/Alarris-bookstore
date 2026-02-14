package com.bookstore.service;

import com.bookstore.entity.Order;
import com.bookstore.entity.User;
import java.util.List;

public interface OrderService {
    Order createOrder(User user, String paymentMethod, String shippingAddress);

    List<Order> findOrdersByUser(User user);

    Order findOrderById(Long id);

    void updateOrderStatus(Long orderId, String status);

    void updateOrderDetails(Long orderId, String status, String trackingNumber, String shippingCompany);

    void cancelOrder(Long orderId);

    List<Order> findAllOrders();
}
