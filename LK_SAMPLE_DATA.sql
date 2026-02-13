-- =============================================================
-- Sri Lankan Demo Seed Data for Alariis Bookstore
-- Includes: roles, users, categories, books, orders, order_items
-- Currency: LKR (book prices start from LKR 1000)
-- Safe to run multiple times (uses NOT EXISTS / upsert-style inserts)
-- =============================================================

START TRANSACTION;

-- -------------------------------------------------------------
-- 1) Roles
-- -------------------------------------------------------------
INSERT INTO roles (name, created_at, updated_at)
SELECT 'ROLE_ADMIN', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_ADMIN');

INSERT INTO roles (name, created_at, updated_at)
SELECT 'ROLE_CUSTOMER', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_CUSTOMER');

-- -------------------------------------------------------------
-- 2) Users (Sri Lankan)
-- Password for all users below: Lanka@123
-- BCrypt hash: $2a$10$FkF0V8scAjZcsEWiHRjBuO0BdDgaGjfJE7YL3DskPAksz.esqHrKS
-- -------------------------------------------------------------
INSERT INTO users (email, password, full_name, phone_number, city, address, zip_code, created_at, updated_at)
SELECT 'admin@alariis.lk', '$2a$10$FkF0V8scAjZcsEWiHRjBuO0BdDgaGjfJE7YL3DskPAksz.esqHrKS',
       'Nimal Perera', '+94 77 100 1001', 'Colombo', '12, Galle Road, Colombo 03', '00300', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@alariis.lk');

INSERT INTO users (email, password, full_name, phone_number, city, address, zip_code, created_at, updated_at)
SELECT 'kasuni.silva@gmail.com', '$2a$10$FkF0V8scAjZcsEWiHRjBuO0BdDgaGjfJE7YL3DskPAksz.esqHrKS',
       'Kasuni Silva', '+94 71 245 6789', 'Kandy', '45, Peradeniya Road, Kandy', '20000', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'kasuni.silva@gmail.com');

INSERT INTO users (email, password, full_name, phone_number, city, address, zip_code, created_at, updated_at)
SELECT 'dilshan.jay@gmail.com', '$2a$10$FkF0V8scAjZcsEWiHRjBuO0BdDgaGjfJE7YL3DskPAksz.esqHrKS',
       'Dilshan Jayasuriya', '+94 76 556 2211', 'Galle', '88, Lighthouse Street, Galle', '80000', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'dilshan.jay@gmail.com');

INSERT INTO users (email, password, full_name, phone_number, city, address, zip_code, created_at, updated_at)
SELECT 'tharushi.fernando@gmail.com', '$2a$10$FkF0V8scAjZcsEWiHRjBuO0BdDgaGjfJE7YL3DskPAksz.esqHrKS',
       'Tharushi Fernando', '+94 70 778 9922', 'Negombo', '21, Beach Road, Negombo', '11500', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'tharushi.fernando@gmail.com');

INSERT INTO users (email, password, full_name, phone_number, city, address, zip_code, created_at, updated_at)
SELECT 'sahan.rajapaksha@gmail.com', '$2a$10$FkF0V8scAjZcsEWiHRjBuO0BdDgaGjfJE7YL3DskPAksz.esqHrKS',
       'Sahan Rajapaksha', '+94 75 334 1122', 'Kurunegala', '17, Kandy Road, Kurunegala', '60000', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'sahan.rajapaksha@gmail.com');

INSERT INTO users (email, password, full_name, phone_number, city, address, zip_code, created_at, updated_at)
SELECT 'anudi.weerakoon@gmail.com', '$2a$10$FkF0V8scAjZcsEWiHRjBuO0BdDgaGjfJE7YL3DskPAksz.esqHrKS',
       'Anudi Weerakoon', '+94 78 987 4455', 'Matara', '63, Main Street, Matara', '81000', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'anudi.weerakoon@gmail.com');

-- -------------------------------------------------------------
-- 3) User Roles
-- -------------------------------------------------------------
INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.name = 'ROLE_ADMIN'
WHERE u.email = 'admin@alariis.lk'
  AND NOT EXISTS (
      SELECT 1 FROM users_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id
  );

INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.name = 'ROLE_CUSTOMER'
WHERE u.email IN (
    'admin@alariis.lk',
    'kasuni.silva@gmail.com',
    'dilshan.jay@gmail.com',
    'tharushi.fernando@gmail.com',
    'sahan.rajapaksha@gmail.com',
    'anudi.weerakoon@gmail.com'
)
AND NOT EXISTS (
    SELECT 1 FROM users_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id
);

-- -------------------------------------------------------------
-- 4) Categories
-- -------------------------------------------------------------
INSERT INTO categories (name, description, created_at, updated_at)
SELECT 'Fiction', 'Contemporary and literary fiction', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Fiction');

INSERT INTO categories (name, description, created_at, updated_at)
SELECT 'Science Fiction', 'Futuristic and science-based stories', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Science Fiction');

INSERT INTO categories (name, description, created_at, updated_at)
SELECT 'Fantasy', 'Magic, epic worlds, and adventures', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Fantasy');

INSERT INTO categories (name, description, created_at, updated_at)
SELECT 'Mystery', 'Crime, thrillers, and detective books', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Mystery');

INSERT INTO categories (name, description, created_at, updated_at)
SELECT 'Non-Fiction', 'Real-world knowledge and experiences', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Non-Fiction');

INSERT INTO categories (name, description, created_at, updated_at)
SELECT 'Business', 'Leadership and entrepreneurship', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Business');

-- -------------------------------------------------------------
-- 5) Books (All LKR, starting from 1000)
-- -------------------------------------------------------------
INSERT INTO books (title, author, description, price, isbn, publication_year, stock_quantity, cover_image, featured, deleted, category_id, created_at, updated_at)
SELECT 'Madol Doova', 'Martin Wickramasinghe', 'Classic Sinhala coming-of-age novel.', 1000.00, '9789553101001', 1947, 35, NULL, true, false,
       c.id, NOW(), NOW()
FROM categories c
WHERE c.name = 'Fiction'
  AND NOT EXISTS (SELECT 1 FROM books WHERE isbn = '9789553101001');

INSERT INTO books (title, author, description, price, isbn, publication_year, stock_quantity, cover_image, featured, deleted, category_id, created_at, updated_at)
SELECT 'Gamperaliya', 'Martin Wickramasinghe', 'Landmark Sri Lankan social novel.', 1250.00, '9789553101002', 1944, 28, NULL, false, false,
       c.id, NOW(), NOW()
FROM categories c
WHERE c.name = 'Fiction'
  AND NOT EXISTS (SELECT 1 FROM books WHERE isbn = '9789553101002');

INSERT INTO books (title, author, description, price, isbn, publication_year, stock_quantity, cover_image, featured, deleted, category_id, created_at, updated_at)
SELECT 'The Great Gatsby', 'F. Scott Fitzgerald', 'A novel of wealth, love, and illusion.', 1750.00, '9780743273565', 1925, 22, NULL, true, false,
       c.id, NOW(), NOW()
FROM categories c
WHERE c.name = 'Fiction'
  AND NOT EXISTS (SELECT 1 FROM books WHERE isbn = '9780743273565');

INSERT INTO books (title, author, description, price, isbn, publication_year, stock_quantity, cover_image, featured, deleted, category_id, created_at, updated_at)
SELECT '1984', 'George Orwell', 'Dystopian classic about surveillance and control.', 1900.00, '9780451524935', 1949, 30, NULL, true, false,
       c.id, NOW(), NOW()
FROM categories c
WHERE c.name = 'Fiction'
  AND NOT EXISTS (SELECT 1 FROM books WHERE isbn = '9780451524935');

INSERT INTO books (title, author, description, price, isbn, publication_year, stock_quantity, cover_image, featured, deleted, category_id, created_at, updated_at)
SELECT 'Dune', 'Frank Herbert', 'Epic science fiction on Arrakis.', 2400.00, '9780441172719', 1965, 18, NULL, true, false,
       c.id, NOW(), NOW()
FROM categories c
WHERE c.name = 'Science Fiction'
  AND NOT EXISTS (SELECT 1 FROM books WHERE isbn = '9780441172719');

INSERT INTO books (title, author, description, price, isbn, publication_year, stock_quantity, cover_image, featured, deleted, category_id, created_at, updated_at)
SELECT 'Foundation', 'Isaac Asimov', 'Classic galactic saga of decline and renewal.', 2100.00, '9780553293357', 1951, 16, NULL, false, false,
       c.id, NOW(), NOW()
FROM categories c
WHERE c.name = 'Science Fiction'
  AND NOT EXISTS (SELECT 1 FROM books WHERE isbn = '9780553293357');

INSERT INTO books (title, author, description, price, isbn, publication_year, stock_quantity, cover_image, featured, deleted, category_id, created_at, updated_at)
SELECT 'The Hobbit', 'J.R.R. Tolkien', 'Bilbo''s unforgettable adventure.', 2300.00, '9780547928227', 1937, 21, NULL, true, false,
       c.id, NOW(), NOW()
FROM categories c
WHERE c.name = 'Fantasy'
  AND NOT EXISTS (SELECT 1 FROM books WHERE isbn = '9780547928227');

INSERT INTO books (title, author, description, price, isbn, publication_year, stock_quantity, cover_image, featured, deleted, category_id, created_at, updated_at)
SELECT 'Harry Potter and the Sorcerer''s Stone', 'J.K. Rowling', 'The beginning of the wizarding world.', 2600.00, '9780439708180', 1997, 26, NULL, true, false,
       c.id, NOW(), NOW()
FROM categories c
WHERE c.name = 'Fantasy'
  AND NOT EXISTS (SELECT 1 FROM books WHERE isbn = '9780439708180');

INSERT INTO books (title, author, description, price, isbn, publication_year, stock_quantity, cover_image, featured, deleted, category_id, created_at, updated_at)
SELECT 'Sherlock Holmes: A Study in Scarlet', 'Arthur Conan Doyle', 'The first Sherlock Holmes mystery.', 1450.00, '9780140439083', 1887, 20, NULL, false, false,
       c.id, NOW(), NOW()
FROM categories c
WHERE c.name = 'Mystery'
  AND NOT EXISTS (SELECT 1 FROM books WHERE isbn = '9780140439083');

INSERT INTO books (title, author, description, price, isbn, publication_year, stock_quantity, cover_image, featured, deleted, category_id, created_at, updated_at)
SELECT 'The Da Vinci Code', 'Dan Brown', 'A mystery thriller across Europe.', 2200.00, '9780307474278', 2003, 25, NULL, false, false,
       c.id, NOW(), NOW()
FROM categories c
WHERE c.name = 'Mystery'
  AND NOT EXISTS (SELECT 1 FROM books WHERE isbn = '9780307474278');

INSERT INTO books (title, author, description, price, isbn, publication_year, stock_quantity, cover_image, featured, deleted, category_id, created_at, updated_at)
SELECT 'Sapiens', 'Yuval Noah Harari', 'A brief history of humankind.', 3200.00, '9780062316110', 2011, 19, NULL, true, false,
       c.id, NOW(), NOW()
FROM categories c
WHERE c.name = 'Non-Fiction'
  AND NOT EXISTS (SELECT 1 FROM books WHERE isbn = '9780062316110');

INSERT INTO books (title, author, description, price, isbn, publication_year, stock_quantity, cover_image, featured, deleted, category_id, created_at, updated_at)
SELECT 'Educated', 'Tara Westover', 'Memoir of resilience and education.', 2800.00, '9780399590504', 2018, 14, NULL, false, false,
       c.id, NOW(), NOW()
FROM categories c
WHERE c.name = 'Non-Fiction'
  AND NOT EXISTS (SELECT 1 FROM books WHERE isbn = '9780399590504');

INSERT INTO books (title, author, description, price, isbn, publication_year, stock_quantity, cover_image, featured, deleted, category_id, created_at, updated_at)
SELECT 'Atomic Habits', 'James Clear', 'Practical system for building good habits.', 3000.00, '9780735211292', 2018, 27, NULL, true, false,
       c.id, NOW(), NOW()
FROM categories c
WHERE c.name = 'Business'
  AND NOT EXISTS (SELECT 1 FROM books WHERE isbn = '9780735211292');

INSERT INTO books (title, author, description, price, isbn, publication_year, stock_quantity, cover_image, featured, deleted, category_id, created_at, updated_at)
SELECT 'The Lean Startup', 'Eric Ries', 'How to build modern startups effectively.', 2900.00, '9780307887894', 2011, 15, NULL, false, false,
       c.id, NOW(), NOW()
FROM categories c
WHERE c.name = 'Business'
  AND NOT EXISTS (SELECT 1 FROM books WHERE isbn = '9780307887894');

INSERT INTO books (title, author, description, price, isbn, publication_year, stock_quantity, cover_image, featured, deleted, category_id, created_at, updated_at)
SELECT 'Rich Dad Poor Dad', 'Robert Kiyosaki', 'Popular personal finance and mindset guide.', 2500.00, '9781612680194', 1997, 24, NULL, true, false,
       c.id, NOW(), NOW()
FROM categories c
WHERE c.name = 'Business'
  AND NOT EXISTS (SELECT 1 FROM books WHERE isbn = '9781612680194');

-- -------------------------------------------------------------
-- 6) Orders
-- -------------------------------------------------------------
INSERT INTO orders (order_date, status, total_amount, shipping_fee, tracking_number, shipping_company, estimated_delivery_date, payment_method, user_id, created_at, updated_at)
SELECT '2026-01-15 10:45:00', 'DELIVERED', 5500.00, 0.00, 'LKPOST-TRK-1001', 'Sri Lanka Post', '2026-01-18 00:00:00', 'Cash on Delivery',
       u.id, NOW(), NOW()
FROM users u
WHERE u.email = 'kasuni.silva@gmail.com'
  AND NOT EXISTS (SELECT 1 FROM orders WHERE tracking_number = 'LKPOST-TRK-1001');

INSERT INTO orders (order_date, status, total_amount, shipping_fee, tracking_number, shipping_company, estimated_delivery_date, payment_method, user_id, created_at, updated_at)
SELECT '2026-01-22 16:20:00', 'SHIPPED', 4300.00, 0.00, 'PICKME-TRK-2002', 'PickMe Flash', '2026-01-25 00:00:00', 'Card',
       u.id, NOW(), NOW()
FROM users u
WHERE u.email = 'dilshan.jay@gmail.com'
  AND NOT EXISTS (SELECT 1 FROM orders WHERE tracking_number = 'PICKME-TRK-2002');

INSERT INTO orders (order_date, status, total_amount, shipping_fee, tracking_number, shipping_company, estimated_delivery_date, payment_method, user_id, created_at, updated_at)
SELECT '2026-02-03 09:10:00', 'PENDING', 3000.00, 0.00, 'DARAZ-TRK-3003', 'Daraz Express', '2026-02-06 00:00:00', 'Cash on Delivery',
       u.id, NOW(), NOW()
FROM users u
WHERE u.email = 'tharushi.fernando@gmail.com'
  AND NOT EXISTS (SELECT 1 FROM orders WHERE tracking_number = 'DARAZ-TRK-3003');

INSERT INTO orders (order_date, status, total_amount, shipping_fee, tracking_number, shipping_company, estimated_delivery_date, payment_method, user_id, created_at, updated_at)
SELECT '2026-02-10 14:35:00', 'DELIVERED', 5100.00, 0.00, 'UBER-TRK-4004', 'Uber Courier', '2026-02-13 00:00:00', 'Card',
       u.id, NOW(), NOW()
FROM users u
WHERE u.email = 'anudi.weerakoon@gmail.com'
  AND NOT EXISTS (SELECT 1 FROM orders WHERE tracking_number = 'UBER-TRK-4004');

-- -------------------------------------------------------------
-- 7) Order Items
-- -------------------------------------------------------------
-- Order: LKPOST-TRK-1001
INSERT INTO order_items (order_id, book_id, quantity, price, created_at, updated_at)
SELECT o.id, b.id, 1, 2600.00, NOW(), NOW()
FROM orders o
JOIN books b ON b.isbn = '9780439708180'
WHERE o.tracking_number = 'LKPOST-TRK-1001'
  AND NOT EXISTS (SELECT 1 FROM order_items oi WHERE oi.order_id = o.id AND oi.book_id = b.id);

INSERT INTO order_items (order_id, book_id, quantity, price, created_at, updated_at)
SELECT o.id, b.id, 1, 2900.00, NOW(), NOW()
FROM orders o
JOIN books b ON b.isbn = '9780307887894'
WHERE o.tracking_number = 'LKPOST-TRK-1001'
  AND NOT EXISTS (SELECT 1 FROM order_items oi WHERE oi.order_id = o.id AND oi.book_id = b.id);

-- Order: PICKME-TRK-2002
INSERT INTO order_items (order_id, book_id, quantity, price, created_at, updated_at)
SELECT o.id, b.id, 1, 2400.00, NOW(), NOW()
FROM orders o
JOIN books b ON b.isbn = '9780441172719'
WHERE o.tracking_number = 'PICKME-TRK-2002'
  AND NOT EXISTS (SELECT 1 FROM order_items oi WHERE oi.order_id = o.id AND oi.book_id = b.id);

INSERT INTO order_items (order_id, book_id, quantity, price, created_at, updated_at)
SELECT o.id, b.id, 1, 1900.00, NOW(), NOW()
FROM orders o
JOIN books b ON b.isbn = '9780451524935'
WHERE o.tracking_number = 'PICKME-TRK-2002'
  AND NOT EXISTS (SELECT 1 FROM order_items oi WHERE oi.order_id = o.id AND oi.book_id = b.id);

-- Order: DARAZ-TRK-3003
INSERT INTO order_items (order_id, book_id, quantity, price, created_at, updated_at)
SELECT o.id, b.id, 1, 3000.00, NOW(), NOW()
FROM orders o
JOIN books b ON b.isbn = '9780735211292'
WHERE o.tracking_number = 'DARAZ-TRK-3003'
  AND NOT EXISTS (SELECT 1 FROM order_items oi WHERE oi.order_id = o.id AND oi.book_id = b.id);

-- Order: UBER-TRK-4004
INSERT INTO order_items (order_id, book_id, quantity, price, created_at, updated_at)
SELECT o.id, b.id, 1, 3200.00, NOW(), NOW()
FROM orders o
JOIN books b ON b.isbn = '9780062316110'
WHERE o.tracking_number = 'UBER-TRK-4004'
  AND NOT EXISTS (SELECT 1 FROM order_items oi WHERE oi.order_id = o.id AND oi.book_id = b.id);

INSERT INTO order_items (order_id, book_id, quantity, price, created_at, updated_at)
SELECT o.id, b.id, 1, 1900.00, NOW(), NOW()
FROM orders o
JOIN books b ON b.isbn = '9780451524935'
WHERE o.tracking_number = 'UBER-TRK-4004'
  AND NOT EXISTS (SELECT 1 FROM order_items oi WHERE oi.order_id = o.id AND oi.book_id = b.id);

COMMIT;

-- -------------------------------------------------------------
-- Quick checks
-- -------------------------------------------------------------
SELECT COUNT(*) AS users_count FROM users;
SELECT COUNT(*) AS categories_count FROM categories;
SELECT COUNT(*) AS books_count FROM books WHERE deleted = false;
SELECT COUNT(*) AS orders_count FROM orders;
SELECT MIN(price) AS min_book_price_lkr, MAX(price) AS max_book_price_lkr FROM books;
