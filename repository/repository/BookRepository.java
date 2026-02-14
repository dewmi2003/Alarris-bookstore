package com.bookstore.repository;

import com.bookstore.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findByDeletedFalse(Pageable pageable);

    @Query("SELECT b FROM Book b WHERE b.deleted = false AND (LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(b.category.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Book> searchBooks(String keyword);

    List<Book> findByCategoryAndDeletedFalse(com.bookstore.entity.Category category);

    List<Book> findByFeaturedTrue();

    // Additional query methods for filtering
    @Query("SELECT b FROM Book b WHERE b.deleted = false AND b.price BETWEEN :minPrice AND :maxPrice ORDER BY b.price ASC")
    List<Book> findByPriceRange(Double minPrice, Double maxPrice);

    @Query("SELECT b FROM Book b WHERE b.deleted = false AND (LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Book> searchByKeyword(String keyword);
}
