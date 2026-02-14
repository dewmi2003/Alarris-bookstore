package com.bookstore.repository;

import com.bookstore.entity.Cart;
import com.bookstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);
}
