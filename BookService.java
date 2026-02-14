package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface BookService {
    List<Book> findAllBooks();

    Page<Book> findPaginated(Pageable pageable);

    Book findBookById(Long id);

    void saveBook(Book book);

    void updateBook(Book book);

    void deleteBook(Long id);

    List<Book> searchBooks(String keyword);

    List<Book> findFeaturedBooks();

    void decrementStock(Long bookId, int quantity);

    // New methods for filtering and sorting
    List<Book> findBooksByCategory(Category category);

    List<Book> findBooksByPriceRange(Double minPrice, Double maxPrice);

    List<Book> searchAndFilter(String keyword, String category, Double minPrice, Double maxPrice, String sortBy);
}
