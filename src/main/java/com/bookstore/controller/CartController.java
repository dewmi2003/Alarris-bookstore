package com.bookstore.controller;

import com.bookstore.entity.Book;
import com.bookstore.service.BookService;
import com.bookstore.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    private CartService cartService;
    private BookService bookService;

    public CartController(CartService cartService, BookService bookService) {
        this.cartService = cartService;
        this.bookService = bookService;
    }

    private void refreshCartCount(jakarta.servlet.http.HttpSession session) {
        int count = cartService.getItems().stream().mapToInt(com.bookstore.dto.CartItem::getQuantity).sum();
        session.setAttribute("cartItemCount", count);
    }

    @GetMapping
    public String viewCart(Model model, jakarta.servlet.http.HttpSession session,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();

        if (auth == null || auth instanceof org.springframework.security.authentication.AnonymousAuthenticationToken) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please login to view your shopping cart.");
            return "redirect:/login";
        }

        refreshCartCount(session);
        model.addAttribute("cartItems", cartService.getItems());
        model.addAttribute("totalAmount", cartService.getTotalAmount());
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam("bookId") Long bookId, @RequestParam("quantity") int quantity,
            jakarta.servlet.http.HttpSession session,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

        // Check if current user is guest or admin
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();

        if (auth == null || auth instanceof org.springframework.security.authentication.AnonymousAuthenticationToken) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please login to add items to your basket.");
            return "redirect:/login";
        }
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "You are currently in Admin view. Admins are strictly prohibited from payment processing.");
            return "redirect:/books";
        }

        Book book = bookService.findBookById(bookId);
        if (book != null) {
            cartService.addItem(book, quantity);
            refreshCartCount(session);
            redirectAttributes.addFlashAttribute("successMessage", "Added " + book.getTitle() + " to basket.");
        }
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateCart(@RequestParam("bookId") Long bookId, @RequestParam("quantity") int quantity,
            jakarta.servlet.http.HttpSession session) {
        cartService.updateItem(bookId, quantity);
        refreshCartCount(session);
        return "redirect:/cart";
    }

    @GetMapping("/remove/{bookId}")
    public String removeFromCart(@PathVariable Long bookId, jakarta.servlet.http.HttpSession session) {
        cartService.removeItem(bookId);
        refreshCartCount(session);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(jakarta.servlet.http.HttpSession session) {
        cartService.clearCart();
        refreshCartCount(session);
        return "redirect:/cart";
    }
}
