package com.bookstore.service.impl;

import com.bookstore.dto.CartItem;
import com.bookstore.entity.Book;
import com.bookstore.entity.Cart;
import com.bookstore.entity.User;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.CartItemRepository;
import com.bookstore.repository.CartRepository;
import com.bookstore.service.CartService;
import com.bookstore.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@SessionScope
@org.springframework.transaction.annotation.Transactional
public class CartServiceImpl implements CartService {

    private List<CartItem> items = new ArrayList<>();

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserService userService;
    private final BookRepository bookRepository;

    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository,
            UserService userService, BookRepository bookRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userService = userService;
        this.bookRepository = bookRepository;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication.getPrincipal() instanceof String
                        && "anonymousUser".equals(authentication.getPrincipal()))) {
            String email = authentication.getName();
            return userService.findUserByEmail(email);
        }
        return null;
    }

    @Override
    public void addItem(Book book, int quantity) {
        User user = getCurrentUser();
        if (user != null) {
            Cart cart = cartRepository.findByUser(user);
            if (cart == null) {
                cart = new Cart();
                cart.setUser(user);
                cart = cartRepository.save(cart);
            }

            com.bookstore.entity.CartItem existingItem = null;
            for (com.bookstore.entity.CartItem item : cart.getItems()) {
                if (item.getBook().getId().equals(book.getId())) {
                    existingItem = item;
                    break;
                }
            }

            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                cartItemRepository.save(existingItem);
            } else {
                com.bookstore.entity.CartItem newItem = new com.bookstore.entity.CartItem();
                newItem.setCart(cart);
                newItem.setBook(book);
                newItem.setQuantity(quantity);
                cartItemRepository.save(newItem);
            }
        } else {
            for (CartItem item : items) {
                if (item.getBook().getId().equals(book.getId())) {
                    item.setQuantity(item.getQuantity() + quantity);
                    return;
                }
            }
            items.add(new CartItem(book, quantity));
        }
    }

    @Override
    public void updateItem(Long bookId, int quantity) {
        User user = getCurrentUser();
        if (user != null) {
            Cart cart = cartRepository.findByUser(user);
            if (cart != null) {
                for (com.bookstore.entity.CartItem item : cart.getItems()) {
                    if (item.getBook().getId().equals(bookId)) {
                        item.setQuantity(quantity);
                        cartItemRepository.save(item);
                        return;
                    }
                }
            }
        } else {
            for (CartItem item : items) {
                if (item.getBook().getId().equals(bookId)) {
                    item.setQuantity(quantity);
                    return;
                }
            }
        }
    }

    @Override
    public void removeItem(Long bookId) {
        User user = getCurrentUser();
        if (user != null) {
            Cart cart = cartRepository.findByUser(user);
            if (cart != null) {
                cart.getItems().removeIf(item -> {
                    if (item.getBook().getId().equals(bookId)) {
                        cartItemRepository.delete(item);
                        return true;
                    }
                    return false;
                });
            }
        } else {
            Iterator<CartItem> iterator = items.iterator();
            while (iterator.hasNext()) {
                CartItem item = iterator.next();
                if (item.getBook().getId().equals(bookId)) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    @Override
    public void clearCart() {
        User user = getCurrentUser();
        if (user != null) {
            Cart cart = cartRepository.findByUser(user);
            if (cart != null) {
                cartItemRepository.deleteAll(cart.getItems());
                cart.getItems().clear();
            }
        } else {
            items.clear();
        }
    }

    @Override
    public List<CartItem> getItems() {
        User user = getCurrentUser();
        if (user != null) {
            Cart cart = cartRepository.findByUser(user);
            if (cart != null) {
                List<CartItem> dtoList = new ArrayList<>();
                for (com.bookstore.entity.CartItem entityItem : cart.getItems()) {
                    dtoList.add(new CartItem(entityItem.getBook(), entityItem.getQuantity()));
                }
                return dtoList;
            }
            return new ArrayList<>();
        } else {
            return items;
        }
    }

    @Override
    public double getTotalAmount() {
        return getItems().stream().mapToDouble(CartItem::getTotalPrice).sum();
    }
}
