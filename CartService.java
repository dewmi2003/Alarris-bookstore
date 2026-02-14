package com.bookstore.service;

import com.bookstore.dto.CartItem;
import com.bookstore.entity.Book;
import java.util.List;

public interface CartService {
    void addItem(Book book, int quantity);

    void updateItem(Long bookId, int quantity);

    void removeItem(Long bookId);

    void clearCart();

    List<CartItem> getItems();

    double getTotalAmount();
}
