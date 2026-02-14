package com.bookstore.service.impl;

import com.bookstore.dto.CartItem;
import com.bookstore.entity.Book;
import com.bookstore.entity.Order;
import com.bookstore.entity.OrderItem;
import com.bookstore.entity.User;
import com.bookstore.repository.OrderRepository;
import com.bookstore.service.BookService;
import com.bookstore.service.CartService;
import com.bookstore.service.OrderService;
import com.bookstore.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private CartService cartService;
    private BookService bookService;

    private UserService userService;

    public OrderServiceImpl(OrderRepository orderRepository, CartService cartService, BookService bookService,
            UserService userService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.bookService = bookService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public Order createOrder(User user, String paymentMethod, String shippingAddress) {
        List<CartItem> cartItems = cartService.getItems();
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setPaymentMethod(paymentMethod); // Set payment method

        if (shippingAddress != null && !shippingAddress.isEmpty()) {
            // Avoid full profile update here to prevent re-encoding existing password hash
            userService.updateUserAddress(user.getId(), shippingAddress);
            user.setAddress(shippingAddress);
        }

        double shippingFee = 5.00; // Flat rate shipping
        order.setShippingFee(shippingFee);
        order.setTotalAmount(cartService.getTotalAmount() + shippingFee);

        order.setEstimatedDeliveryDate(LocalDateTime.now().plusDays(7)); // Default 7 days delivery

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice()); // Freeze price
            orderItem.setOrder(order);
            order.getItems().add(orderItem);

            // Decrement stock
            bookService.decrementStock(cartItem.getBook().getId(), cartItem.getQuantity());
        }

        Order savedOrder = orderRepository.save(order);
        cartService.clearCart();
        return savedOrder;
    }

    @Override
    public List<Order> findOrdersByUser(User user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }

    @Override
    public Order findOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public void updateOrderStatus(Long orderId, String status) {
        Order order = findOrderById(orderId);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
        }
    }

    @Override
    public void updateOrderDetails(Long orderId, String status, String trackingNumber, String shippingCompany) {
        Order order = findOrderById(orderId);
        if (order != null) {
            order.setStatus(status);
            order.setTrackingNumber(trackingNumber);
            order.setShippingCompany(shippingCompany);
            orderRepository.save(order);
        }
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = findOrderById(orderId);
        if (order != null) {
            if ("SHIPPED".equalsIgnoreCase(order.getStatus()) || "DELIVERED".equalsIgnoreCase(order.getStatus())) {
                throw new RuntimeException("Cannot cancel order that has been shipped or delivered.");
            }
            order.setStatus("CANCELLED");
            orderRepository.save(order);

            // Restore stock
            for (OrderItem item : order.getItems()) {
                Book book = item.getBook();
                book.setStockQuantity(book.getStockQuantity() + item.getQuantity());
                // We need to save book. Since bookService isn't injected for update, we might
                // need repository or add method in bookService
                // actually bookService is injected
                bookService.updateBook(book);
            }
        }
    }

    @Override
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }
}
