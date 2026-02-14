package com.bookstore.service.impl;

import com.bookstore.entity.Book;
import com.bookstore.entity.Category;
import com.bookstore.exception.InsufficientStockException;
import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.repository.BookRepository;
import com.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public List<Book> findAllBooks() {
        log.debug("Finding all books");
        return bookRepository.findAll();
    }

    @Override
    public Page<Book> findPaginated(Pageable pageable) {
        log.debug("Finding paginated books: {}", pageable);
        return bookRepository.findByDeletedFalse(pageable);
    }

    @Override
    public Book findBookById(Long id) {
        log.debug("Finding book by id: {}", id);
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    @Override
    @Transactional
    public void saveBook(Book book) {
        log.info("Saving book: {}", book.getTitle());
        bookRepository.save(book);
    }

    @Override
    @Transactional
    public void updateBook(Book book) {
        log.info("Updating book with id: {}", book.getId());
        if (!bookRepository.existsById(book.getId())) {
            throw new ResourceNotFoundException("Cannot update. Book not found with id: " + book.getId());
        }
        bookRepository.save(book);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        log.info("Soft deleting book with id: {}", id);
        Book book = findBookById(id);
        book.setDeleted(true);
        bookRepository.save(book);
    }

    @Override
    public List<Book> searchBooks(String keyword) {
        log.debug("Searching books with keyword: {}", keyword);
        if (keyword != null) {
            return bookRepository.searchBooks(keyword);
        }
        return bookRepository.findAll();
    }

    @Override
    public List<Book> findFeaturedBooks() {
        log.debug("Finding featured books");
        return bookRepository.findByFeaturedTrue();
    }

    @Override
    @Transactional
    public void decrementStock(Long bookId, int quantity) {
        log.info("Decrementing stock for book id: {} by quantity: {}", bookId, quantity);
        Book book = findBookById(bookId);
        if (book.getStockQuantity() < quantity) {
            log.error("Insufficient stock for book: {}. Available: {}, Requested: {}",
                    book.getTitle(), book.getStockQuantity(), quantity);
            throw new InsufficientStockException("Not enough stock for book: " + book.getTitle());
        }
        book.setStockQuantity(book.getStockQuantity() - quantity);
        bookRepository.save(book);
    }

    @Override
    public List<Book> findBooksByCategory(Category category) {
        log.debug("Finding books by category: {}", category.getName());
        return bookRepository.findByCategoryAndDeletedFalse(category);
    }

    @Override
    public List<Book> findBooksByPriceRange(Double minPrice, Double maxPrice) {
        log.debug("Finding books by price range: {} - {}", minPrice, maxPrice);
        return bookRepository.findByPriceRange(minPrice, maxPrice);
    }

    @Override
    public List<Book> searchAndFilter(String keyword, String category, Double minPrice, Double maxPrice, String sortBy) {
        log.debug("Searching books with keyword: {}, category: {}, price range: {}-{}, sortBy: {}",
                keyword, category, minPrice, maxPrice, sortBy);
        
        // Start with all non-deleted books
        List<Book> books = bookRepository.findAll().stream()
                .filter(b -> !b.isDeleted())
                .collect(Collectors.toList());
        
        // Apply keyword search if provided
        if (keyword != null && !keyword.trim().isEmpty()) {
            String searchTerm = keyword.trim().toLowerCase();
            books = books.stream()
                    .filter(b -> b.getTitle().toLowerCase().contains(searchTerm) ||
                            b.getAuthor().toLowerCase().contains(searchTerm) ||
                            (b.getDescription() != null && b.getDescription().toLowerCase().contains(searchTerm)))
                    .collect(Collectors.toList());
            log.debug("Found {} books matching keyword: {}", books.size(), keyword);
        }
        
        // Apply category filter if provided.
        // "Brown" selection is treated as "All Books" per UI behavior request.
        if (category != null) {
            String categoryFilter = category.toLowerCase().trim();
            boolean isAllCategory = categoryFilter.equals("all")
                    || categoryFilter.equals("brown")
                    || categoryFilter.equals("brown books")
                    || categoryFilter.equals("brown collection");

            if (!isAllCategory) {
            books = books.stream()
                    .filter(b -> b.getCategory() != null &&
                            b.getCategory().getName().toLowerCase().equals(categoryFilter))
                    .collect(Collectors.toList());
            log.debug("Found {} books in category: {}", books.size(), category);
            }
        }
        
        // Apply price range filter
        if (minPrice != null && maxPrice != null) {
            books = books.stream()
                    .filter(b -> b.getPrice() >= minPrice && b.getPrice() <= maxPrice)
                    .collect(Collectors.toList());
            log.debug("Found {} books in price range: {} - {}", books.size(), minPrice, maxPrice);
        }
        
        // Apply sorting
        if (sortBy != null && !sortBy.isEmpty()) {
            String sortOption = sortBy.toLowerCase().trim();
            switch (sortOption) {
                case "price-low":
                    books.sort((a, b) -> Double.compare(a.getPrice(), b.getPrice()));
                    log.debug("Sorted by price: low to high");
                    break;
                case "price-high":
                    books.sort((a, b) -> Double.compare(b.getPrice(), a.getPrice()));
                    log.debug("Sorted by price: high to low");
                    break;
                case "title":
                    books.sort((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
                    log.debug("Sorted by title: A-Z");
                    break;
                case "newest":
                default:
                    books.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
                    log.debug("Sorted by newest first");
                    break;
            }
        }
        
        log.info("Search and filter returned {} books", books.size());
        return books;
    }
}
