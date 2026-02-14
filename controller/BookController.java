package com.bookstore.controller;

import com.bookstore.entity.Book;
import com.bookstore.entity.PageContent;
import com.bookstore.service.BookService;
import com.bookstore.service.PageContentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BookController {

    private com.bookstore.service.BookService bookService;
    private com.bookstore.service.CategoryService categoryService;
    private final PageContentService pageContentService;

    public BookController(BookService bookService,
                          com.bookstore.service.CategoryService categoryService,
                          PageContentService pageContentService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.pageContentService = pageContentService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Book> books = bookService.findAllBooks();
        List<Book> featured = bookService.findFeaturedBooks();

        
        if (featured.isEmpty()) {
            featured = books.stream().limit(4).toList();
        }

        model.addAttribute("featuredBooks", featured);
        model.addAttribute("recentBooks", books.stream().skip(4).limit(4).toList());
        model.addAttribute("categories", categoryService.findAllCategories());
        PageContent homeContent = pageContentService.getPageContent("home");
        model.addAttribute("homeContent", homeContent);
        model.addAttribute("announcementItems", homeContent.getContent() != null
                ? homeContent.getContent().split("\\|")
                : new String[0]);
        return "home";
    }

    @GetMapping("/books")
    public String listBooks(Model model,
                            @RequestParam(value = "query", required = false) String query,
                            @RequestParam(value = "category", required = false, defaultValue = "all") String category,
                            @RequestParam(value = "minPrice", required = false) Double minPrice,
                            @RequestParam(value = "maxPrice", required = false, defaultValue = "1.7976931348623157E308") Double maxPrice,
                            @RequestParam(value = "sortBy", required = false, defaultValue = "newest") String sortBy) {
        if (minPrice == null) {
            minPrice = 0.0;
        }
        String normalizedCategory = category == null ? "all" : category.trim().toLowerCase();
        if (normalizedCategory.equals("brown")
                || normalizedCategory.equals("brown books")
                || normalizedCategory.equals("brown collection")) {
            normalizedCategory = "all";
        }

        List<Book> books = bookService.searchAndFilter(query, normalizedCategory, minPrice, maxPrice, sortBy);
        model.addAttribute("books", books);
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("query", query);
        model.addAttribute("selectedCategory", normalizedCategory);
        model.addAttribute("selectedSort", sortBy);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        return "book_list";
    }

    @GetMapping("/book/{id}")
    public String bookDetails(@PathVariable Long id, Model model) {
        Book book = bookService.findBookById(id);
        if (book == null) {
            return "redirect:/books";
        }
        model.addAttribute("book", book);
        return "book_details";
    }
}

