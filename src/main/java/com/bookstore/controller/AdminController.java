package com.bookstore.controller;

import com.bookstore.entity.Book;
import com.bookstore.entity.Category;
import com.bookstore.entity.PageContent;
import com.bookstore.service.BookService;
import com.bookstore.service.CategoryService;
import com.bookstore.service.OrderService;
import com.bookstore.service.PageContentService;
import com.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private BookService bookService;
    private CategoryService categoryService;
    private OrderService orderService;
    private UserService userService;
    private PageContentService pageContentService;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public AdminController(BookService bookService, CategoryService categoryService,
            OrderService orderService, UserService userService, PageContentService pageContentService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.orderService = orderService;
        this.userService = userService;
        this.pageContentService = pageContentService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<com.bookstore.entity.Order> orders = orderService.findAllOrders();
        double totalRevenue = orders.stream()
                .mapToDouble(order -> order.getTotalAmount() != null ? order.getTotalAmount() : 0.0)
                .sum();
        long totalOrders = orders.size();
        double avgOrderValue = totalOrders > 0 ? totalRevenue / totalOrders : 0.0;

        long pendingOrders = orders.stream().filter(order -> "PENDING".equalsIgnoreCase(order.getStatus())).count();
        long shippedOrders = orders.stream().filter(order -> "SHIPPED".equalsIgnoreCase(order.getStatus())).count();
        long cancelledOrders = orders.stream().filter(order -> "CANCELLED".equalsIgnoreCase(order.getStatus())).count();
        long deliveredOrders = orders.stream()
                .filter(order -> "DELIVERED".equalsIgnoreCase(order.getStatus())
                        || "COMPLETED".equalsIgnoreCase(order.getStatus()))
                .count();
        long statusTotal = Math.max(totalOrders, 1);
        double deliveredPct = (deliveredOrders * 100.0) / statusTotal;
        double shippedPct = (shippedOrders * 100.0) / statusTotal;
        double pendingPct = (pendingOrders * 100.0) / statusTotal;
        double cancelledPct = (cancelledOrders * 100.0) / statusTotal;
        double deliveredEnd = deliveredPct;
        double shippedEnd = deliveredEnd + shippedPct;
        double pendingEnd = shippedEnd + pendingPct;
        String pieChartStyle = String.format(Locale.ENGLISH,
                "background: conic-gradient(#10b981 0 %.2f%%, #3b82f6 %.2f%% %.2f%%, #f59e0b %.2f%% %.2f%%, #ef4444 %.2f%% 100%%);",
                deliveredEnd, deliveredEnd, shippedEnd, shippedEnd, pendingEnd, pendingEnd);

        LocalDate today = LocalDate.now();
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEE", Locale.ENGLISH);
        List<String> salesLabels = new ArrayList<>();
        List<Double> salesValues = new ArrayList<>();

        for (int i = 6; i >= 0; i--) {
            LocalDate targetDate = today.minusDays(i);
            salesLabels.add(dayFormatter.format(targetDate).toUpperCase(Locale.ENGLISH));
            double dayRevenue = orders.stream()
                    .filter(order -> order.getOrderDate() != null && order.getOrderDate().toLocalDate().equals(targetDate))
                    .mapToDouble(order -> order.getTotalAmount() != null ? order.getTotalAmount() : 0.0)
                    .sum();
            salesValues.add(dayRevenue);
        }

        double maxSales = salesValues.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
        List<Double> salesHeights = salesValues.stream()
                .map(value -> maxSales > 0 ? Math.max(15.0, (value / maxSales) * 100.0) : 15.0)
                .toList();

        double last7Revenue = salesValues.stream().mapToDouble(Double::doubleValue).sum();
        double previous7Revenue = 0.0;
        for (int i = 7; i <= 13; i++) {
            LocalDate targetDate = today.minusDays(i);
            previous7Revenue += orders.stream()
                    .filter(order -> order.getOrderDate() != null && order.getOrderDate().toLocalDate().equals(targetDate))
                    .mapToDouble(order -> order.getTotalAmount() != null ? order.getTotalAmount() : 0.0)
                    .sum();
        }

        double revenueTrendPct = previous7Revenue > 0
                ? ((last7Revenue - previous7Revenue) / previous7Revenue) * 100.0
                : (last7Revenue > 0 ? 100.0 : 0.0);

        long booksCount = bookService.findAllBooks().stream().filter(book -> !book.isDeleted()).count();
        int usersCount = userService.findAllUsers().size();
        int categoriesCount = categoryService.findAllCategories().size();

        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("avgOrderValue", avgOrderValue);
        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("shippedOrders", shippedOrders);
        model.addAttribute("cancelledOrders", cancelledOrders);
        model.addAttribute("deliveredOrders", deliveredOrders);
        model.addAttribute("deliveredPct", deliveredPct);
        model.addAttribute("shippedPct", shippedPct);
        model.addAttribute("pendingPct", pendingPct);
        model.addAttribute("cancelledPct", cancelledPct);
        model.addAttribute("deliveredEnd", deliveredEnd);
        model.addAttribute("shippedEnd", shippedEnd);
        model.addAttribute("pendingEnd", pendingEnd);
        model.addAttribute("pieChartStyle", pieChartStyle);
        model.addAttribute("salesLabels", salesLabels);
        model.addAttribute("salesValues", salesValues);
        model.addAttribute("salesHeights", salesHeights);
        model.addAttribute("last7Revenue", last7Revenue);
        model.addAttribute("revenueTrendPct", revenueTrendPct);
        model.addAttribute("booksCount", booksCount);
        model.addAttribute("usersCount", usersCount);
        model.addAttribute("categoriesCount", categoriesCount);
        return "admin/dashboard";
    }

    // --- Book Management ---

    @GetMapping("/books")
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.findAllBooks());
        return "admin/books";
    }

    @GetMapping("/books/new")
    public String createBookForm(Model model) {
        Book book = new Book();
        model.addAttribute("book", book);
        model.addAttribute("categories", categoryService.findAllCategories());
        return "admin/book_form";
    }

    @PostMapping("/books")
    public String saveBook(@ModelAttribute("book") Book book,
            @RequestParam("image") MultipartFile startImage) throws IOException {

        if (!startImage.isEmpty()) {
            String fileName = StringUtils.cleanPath(startImage.getOriginalFilename());
            String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (var inputStream = startImage.getInputStream()) {
                Path filePath = uploadPath.resolve(uniqueFileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                book.setCoverImage(uniqueFileName);
            } catch (IOException ioe) {
                throw new IOException("Could not save image file: " + fileName, ioe);
            }
        }

        bookService.saveBook(book);
        return "redirect:/admin/books";
    }

    @GetMapping("/books/edit/{id}")
    public String editBookForm(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.findBookById(id));
        model.addAttribute("categories", categoryService.findAllCategories());
        return "admin/book_form";
    }

    @PostMapping("/books/{id}")
    public String updateBook(@PathVariable Long id,
            @ModelAttribute("book") Book book,
            @RequestParam("image") MultipartFile startImage) throws IOException {

        Book existingBook = bookService.findBookById(id);
        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setPrice(book.getPrice());
        existingBook.setIsbn(book.getIsbn());
        existingBook.setDescription(book.getDescription());
        existingBook.setCategory(book.getCategory());

        if (!startImage.isEmpty()) {
            String fileName = StringUtils.cleanPath(startImage.getOriginalFilename());
            String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath))
                Files.createDirectories(uploadPath);
            try (var inputStream = startImage.getInputStream()) {
                Path filePath = uploadPath.resolve(uniqueFileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                existingBook.setCoverImage(uniqueFileName);
            }
        }

        bookService.updateBook(existingBook);
        return "redirect:/admin/books";
    }

    @GetMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/admin/books";
    }

    @GetMapping("/books/toggle-feature/{id}")
    public String toggleFeature(@PathVariable Long id) {
        Book book = bookService.findBookById(id);
        if (book != null) {
            book.setFeatured(!book.isFeatured());
            bookService.saveBook(book);
        }
        return "redirect:/admin/books";
    }

    // --- Category Management ---

    @GetMapping("/categories")
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.findAllCategories());
        return "admin/categories";
    }

    @GetMapping("/categories/new")
    public String createCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/category_form";
    }

    @PostMapping("/categories")
    public String saveCategory(@ModelAttribute("category") Category category) {
        categoryService.saveCategory(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/admin/categories";
    }

    // --- Order Management ---

    @GetMapping("/orders")
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.findAllOrders());
        return "admin/orders";
    }

    @PostMapping("/orders/{id}/status")
    public String updateOrderStatus(@PathVariable Long id, @RequestParam("status") String status) {
        orderService.updateOrderStatus(id, status);
        return "redirect:/admin/orders";
    }

    @GetMapping("/orders/edit/{id}")
    public String editOrderForm(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.findOrderById(id));
        return "admin/order_edit";
    }

    @PostMapping("/orders/update/{id}")
    public String updateOrderDetails(@PathVariable Long id,
            @RequestParam("status") String status,
            @RequestParam("trackingNumber") String trackingNumber,
            @RequestParam("shippingCompany") String shippingCompany) {
        orderService.updateOrderDetails(id, status, trackingNumber, shippingCompany);
        return "redirect:/admin/orders";
    }

    // --- User Management ---

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "admin/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/reset-password/{id}")
    public String resetUserPassword(@PathVariable Long id) {
        // Recovery endpoint for accounts affected by historical password re-hash issue.
        userService.resetUserPassword(id, "Password@123");
        return "redirect:/admin/users";
    }

    // --- Site Content Management ---

    @GetMapping("/content")
    public String listManagedContent(Model model) {
        model.addAttribute("pages", pageContentService.getManagedPages());
        return "admin/content_pages";
    }

    @GetMapping("/content/edit/{pageKey}")
    public String editPageContent(@PathVariable String pageKey, Model model) {
        model.addAttribute("pageContent", pageContentService.getPageContent(pageKey));
        return "admin/content_edit";
    }

    @PostMapping("/content/edit/{pageKey}")
    public String savePageContent(@PathVariable String pageKey, @ModelAttribute("pageContent") PageContent pageContent) {
        pageContentService.savePageContent(pageKey, pageContent);
        return "redirect:/admin/content?saved";
    }
}
