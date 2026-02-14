package com.bookstore.controller;

import com.bookstore.entity.Order;
import com.bookstore.entity.User;
import com.bookstore.service.OrderService;
import com.bookstore.service.UserService;
import com.bookstore.service.PdfService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;
import java.util.List;

@Controller
public class OrderController {

    private OrderService orderService;
    private UserService userService;
    private PdfService pdfService;
    private com.bookstore.service.CartService cartService;

    public OrderController(OrderService orderService, UserService userService, PdfService pdfService,
            com.bookstore.service.CartService cartService) {
        this.orderService = orderService;
        this.userService = userService;
        this.pdfService = pdfService;
        this.cartService = cartService;
    }

    @GetMapping("/checkout")
    public String checkout(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (cartService.getItems().isEmpty()) {
            return "redirect:/cart?error=empty";
        }
        model.addAttribute("cartItems", cartService.getItems());
        model.addAttribute("totalAmount", cartService.getTotalAmount());
        return "checkout";
    }

    @PostMapping("/checkout/process")
    public String processCheckout(@RequestParam("address") String address,
            @RequestParam("paymentMethod") String paymentMethod,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findUserByEmail(userDetails.getUsername());

        try {
            // Simulate Payment Processing
            if ("Koko".equals(paymentMethod)) {
                // Simulate Koko redirection or processing
                Thread.sleep(1000);
            } else if ("Card".equals(paymentMethod)) {
                Thread.sleep(1500);
            }

            Order order = orderService.createOrder(user, paymentMethod, address);
            return "redirect:/order/confirmation/" + order.getId();
        } catch (RuntimeException | InterruptedException e) {
            return "redirect:/cart?error=empty";
        }
    }

    @GetMapping("/order/confirmation/{id}")
    public String orderConfirmation(@PathVariable Long id, Model model) {
        Order order = orderService.findOrderById(id);
        model.addAttribute("order", order);
        return "order_confirmation";
    }

    @GetMapping("/my-orders")
    public String myOrders(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findUserByEmail(userDetails.getUsername());
        List<Order> orders = orderService.findOrdersByUser(user);
        model.addAttribute("orders", orders);
        return "my_orders";
    }

    @PostMapping("/orders/{id}/cancel")
    public String cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return "redirect:/my-orders";
    }

    @GetMapping("/order/confirmation/{id}/pdf")
    public ResponseEntity<InputStreamResource> downloadInvoice(@PathVariable Long id) {
        Order order = orderService.findOrderById(id);
        ByteArrayInputStream bis = pdfService.generateOrderPdf(order);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=order_" + id + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
