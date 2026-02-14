-- ========================================
-- ALARIIS BOOKSTORE - REAL-WORLD BOOK DATA
-- ========================================
-- This script populates the database with popular real-world books
-- Run this after the application has created the tables

-- Insert Categories (if not already present)
INSERT INTO categories (name, description, created_at, updated_at) VALUES
('Fiction', 'Literary and contemporary fiction', NOW(), NOW()),
('Science Fiction', 'Sci-fi and futuristic novels', NOW(), NOW()),
('Fantasy', 'Fantasy and magical worlds', NOW(), NOW()),
('Mystery', 'Mystery and thriller novels', NOW(), NOW()),
('Romance', 'Romance and love stories', NOW(), NOW()),
('Non-Fiction', 'Educational and informative books', NOW(), NOW()),
('Biography', 'Life stories and memoirs', NOW(), NOW()),
('Self-Help', 'Personal development and motivation', NOW(), NOW()),
('Business', 'Business and entrepreneurship', NOW(), NOW()),
('History', 'Historical accounts and analysis', NOW(), NOW()),
('Science', 'Scientific discoveries and theories', NOW(), NOW()),
('Technology', 'Tech and programming books', NOW(), NOW()),
('Brown Collection', 'Classic and timeless literature', NOW(), NOW())
ON DUPLICATE KEY UPDATE name=name;


-- Get category IDs (assuming auto-increment starting from 1)
-- Fiction=1, Sci-Fi=2, Fantasy=3, Mystery=4, Romance=5, Non-Fiction=6
-- Biography=7, Self-Help=8, Business=9, History=10, Science=11, Technology=12

-- Insert Real-World Books
INSERT INTO books (title, author, description, price, isbn, publication_year, category_id, cover_image, stock_quantity, featured, created_at, updated_at) VALUES

-- FICTION
('To Kill a Mockingbird', 'Harper Lee', 'A gripping tale of racial injustice and childhood innocence in the American South. This Pulitzer Prize-winning novel follows young Scout Finch as her father defends a black man accused of a terrible crime.', 14.99, '978-0061120084', 1960, 1, 'to-kill-a-mockingbird.jpg', 45, true, NOW(), NOW()),
('1984', 'George Orwell', 'A dystopian masterpiece about totalitarianism and surveillance. Winston Smith struggles to maintain his humanity in a world where Big Brother watches everything and the Thought Police punish independent thinking.', 13.99, '978-0451524935', 1949, 1, '1984.jpg', 60, true, NOW(), NOW()),
('The Great Gatsby', 'F. Scott Fitzgerald', 'The quintessential American novel of the Jazz Age. Jay Gatsby''s obsessive pursuit of Daisy Buchanan unfolds against the backdrop of lavish parties and the dark underbelly of the American Dream.', 12.99, '978-0743273565', 1925, 1, 'great-gatsby.jpg', 50, true, NOW(), NOW()),
('Pride and Prejudice', 'Jane Austen', 'A timeless romance and social commentary. Elizabeth Bennet navigates love, family, and societal expectations in Regency England, encountering the proud Mr. Darcy along the way.', 11.99, '978-0141439518', 1813, 1, 'pride-prejudice.jpg', 40, false, NOW(), NOW()),
('The Catcher in the Rye', 'J.D. Salinger', 'Holden Caulfield''s iconic journey through New York City captures teenage angst and alienation. A controversial classic that continues to resonate with readers of all ages.', 13.99, '978-0316769174', 1951, 1, 'catcher-rye.jpg', 35, false, NOW(), NOW()),

-- SCIENCE FICTION
('Dune', 'Frank Herbert', 'An epic tale of politics, religion, and ecology on the desert planet Arrakis. Paul Atreides must navigate deadly conspiracies and fulfill his destiny as the prophesied messiah.', 18.99, '978-0441172719', 1965, 2, 'dune.jpg', 55, true, NOW(), NOW()),
('The Hitchhiker''s Guide to the Galaxy', 'Douglas Adams', 'A hilarious cosmic adventure begins when Earth is demolished to make way for a hyperspace bypass. Arthur Dent travels the galaxy with his alien friend Ford Prefect, armed only with a towel.', 14.99, '978-0345391803', 1979, 2, 'hitchhikers-guide.jpg', 48, true, NOW(), NOW()),
('Ender''s Game', 'Orson Scott Card', 'Brilliant young Ender Wiggin is recruited to Battle School to prepare for an alien invasion. But the games he plays may have consequences far beyond what anyone imagined.', 15.99, '978-0812550702', 1985, 2, 'enders-game.jpg', 42, false, NOW(), NOW()),
('Foundation', 'Isaac Asimov', 'The Galactic Empire is dying, and mathematician Hari Seldon creates the Foundation to preserve knowledge and shorten the coming dark age. The first book in Asimov''s legendary series.', 16.99, '978-0553293357', 1951, 2, 'foundation.jpg', 38, false, NOW(), NOW()),
('Neuromancer', 'William Gibson', 'The cyberpunk classic that defined a genre. Hacker Case is hired for one last job: break into the most secure computer system in the world. A mind-bending journey through cyberspace.', 15.99, '978-0441569595', 1984, 2, 'neuromancer.jpg', 30, false, NOW(), NOW()),

-- FANTASY
('The Hobbit', 'J.R.R. Tolkien', 'Bilbo Baggins is swept into an epic quest to reclaim the lost Dwarf Kingdom of Erebor from the dragon Smaug. A timeless adventure filled with magic, danger, and unexpected heroism.', 16.99, '978-0547928227', 1937, 3, 'hobbit.jpg', 65, true, NOW(), NOW()),
('Harry Potter and the Sorcerer''s Stone', 'J.K. Rowling', 'An orphaned boy discovers he''s a wizard and begins his magical education at Hogwarts School of Witchcraft and Wizardry. The first book in the beloved series that captivated millions.', 17.99, '978-0439708180', 1997, 3, 'harry-potter-1.jpg', 80, true, NOW(), NOW()),
('The Name of the Wind', 'Patrick Rothfuss', 'Kvothe tells the story of his transformation from a gifted child to the most notorious wizard his world has ever seen. A beautifully written tale of magic, music, and mystery.', 18.99, '978-0756404741', 2007, 3, 'name-of-wind.jpg', 45, true, NOW(), NOW()),
('A Game of Thrones', 'George R.R. Martin', 'In the Seven Kingdoms of Westeros, noble families vie for control of the Iron Throne. A sweeping epic of power, betrayal, and the coming of winter.', 19.99, '978-0553103540', 1996, 3, 'game-of-thrones.jpg', 50, false, NOW(), NOW()),
('The Way of Kings', 'Brandon Sanderson', 'The first book in the Stormlight Archive. On the shattered plains of Roshar, warriors fight for honor while dark forces gather. An epic fantasy with intricate magic systems.', 21.99, '978-0765326355', 2010, 3, 'way-of-kings.jpg', 40, false, NOW(), NOW()),

-- MYSTERY/THRILLER
('The Girl with the Dragon Tattoo', 'Stieg Larsson', 'Journalist Mikael Blomkvist and hacker Lisbeth Salander investigate a decades-old disappearance in this gripping Swedish thriller. Dark secrets and dangerous enemies await.', 16.99, '978-0307454546', 2005, 4, 'girl-dragon-tattoo.jpg', 52, true, NOW(), NOW()),
('Gone Girl', 'Gillian Flynn', 'On their fifth anniversary, Amy Dunne disappears. Her husband Nick becomes the prime suspect. But nothing is as it seems in this twisted psychological thriller.', 15.99, '978-0307588371', 2012, 4, 'gone-girl.jpg', 48, true, NOW(), NOW()),
('The Da Vinci Code', 'Dan Brown', 'Harvard symbologist Robert Langdon is pulled into a deadly race to uncover a secret that could shake the foundations of Christianity. A page-turning mystery through art and history.', 14.99, '978-0307474278', 2003, 4, 'da-vinci-code.jpg', 44, false, NOW(), NOW()),
('Big Little Lies', 'Liane Moriarty', 'Three women''s seemingly perfect lives unravel to the point of murder. A darkly comedic tale of friendship, secrets, and the dangers of suburban life.', 15.99, '978-0399167065', 2014, 4, 'big-little-lies.jpg', 38, false, NOW(), NOW()),

-- ROMANCE
('The Notebook', 'Nicholas Sparks', 'An elderly man reads to a woman with Alzheimer''s from a faded notebook, telling the story of a summer romance that transformed their lives forever. A heartbreaking love story.', 13.99, '978-1455582877', 1996, 5, 'notebook.jpg', 42, true, NOW(), NOW()),
('Me Before You', 'Jojo Moyes', 'Louisa Clark becomes a caregiver for Will Traynor, a wealthy young banker left paralyzed after an accident. Their unlikely relationship will change both their lives.', 14.99, '978-0143124542', 2012, 5, 'me-before-you.jpg', 40, false, NOW(), NOW()),
('The Fault in Our Stars', 'John Green', 'Hazel and Augustus are two teenagers who meet at a cancer support group. Their love story is funny, tragic, and unforgettable.', 13.99, '978-0142424179', 2012, 5, 'fault-in-stars.jpg', 55, true, NOW(), NOW()),

-- NON-FICTION
('Sapiens', 'Yuval Noah Harari', 'A Brief History of Humankind explores how Homo sapiens came to dominate the world. From the Stone Age to the modern era, Harari examines the forces that shaped our species.', 19.99, '978-0062316110', 2011, 6, 'sapiens.jpg', 70, true, NOW(), NOW()),
('Educated', 'Tara Westover', 'A memoir about a young woman who grows up in a strict survivalist family in Idaho and eventually escapes to learn about the wider world through education.', 17.99, '978-0399590504', 2018, 6, 'educated.jpg', 60, true, NOW(), NOW()),
('Thinking, Fast and Slow', 'Daniel Kahneman', 'Nobel Prize winner Daniel Kahneman explains the two systems that drive the way we think: fast, intuitive thinking and slow, deliberate thinking.', 18.99, '978-0374533557', 2011, 6, 'thinking-fast-slow.jpg', 45, false, NOW(), NOW()),

-- BIOGRAPHY
('Steve Jobs', 'Walter Isaacson', 'The authorized biography of Apple''s co-founder, based on over forty interviews with Jobs and hundreds of interviews with family, friends, and colleagues.', 19.99, '978-1451648539', 2011, 7, 'steve-jobs.jpg', 50, true, NOW(), NOW()),
('Becoming', 'Michelle Obama', 'The intimate memoir of the former First Lady of the United States. Michelle Obama invites readers into her world, chronicling her journey from childhood to the White House.', 18.99, '978-1524763138', 2018, 7, 'becoming.jpg', 65, true, NOW(), NOW()),
('The Diary of a Young Girl', 'Anne Frank', 'The powerful diary of a Jewish teenager hiding from the Nazis in Amsterdam. Anne Frank''s words continue to inspire and educate readers around the world.', 12.99, '978-0553296983', 1947, 7, 'anne-frank.jpg', 40, false, NOW(), NOW()),

-- SELF-HELP
('Atomic Habits', 'James Clear', 'An Easy & Proven Way to Build Good Habits & Break Bad Ones. James Clear reveals practical strategies for forming good habits, breaking bad ones, and mastering tiny behaviors.', 16.99, '978-0735211292', 2018, 8, 'atomic-habits.jpg', 75, true, NOW(), NOW()),
('The 7 Habits of Highly Effective People', 'Stephen R. Covey', 'A holistic approach to solving personal and professional problems. Covey presents a principle-centered approach for solving problems and achieving success.', 15.99, '978-1982137274', 1989, 8, '7-habits.jpg', 55, true, NOW(), NOW()),
('How to Win Friends and Influence People', 'Dale Carnegie', 'The timeless classic on interpersonal relations. Carnegie''s principles have helped millions of people achieve success in business and personal relationships.', 14.99, '978-0671027032', 1936, 8, 'win-friends.jpg', 48, false, NOW(), NOW()),
('The Power of Now', 'Eckhart Tolle', 'A guide to spiritual enlightenment that teaches readers to live in the present moment and find peace within themselves.', 15.99, '978-1577314806', 1997, 8, 'power-of-now.jpg', 42, false, NOW(), NOW()),

-- BUSINESS
('Zero to One', 'Peter Thiel', 'Notes on Startups, or How to Build the Future. PayPal co-founder Peter Thiel shares his contrarian views on innovation and building successful companies.', 17.99, '978-0804139298', 2014, 9, 'zero-to-one.jpg', 50, true, NOW(), NOW()),
('The Lean Startup', 'Eric Ries', 'How Today''s Entrepreneurs Use Continuous Innovation to Create Radically Successful Businesses. A revolutionary approach to building and launching new products.', 16.99, '978-0307887894', 2011, 9, 'lean-startup.jpg', 45, false, NOW(), NOW()),
('Good to Great', 'Jim Collins', 'Why Some Companies Make the Leap and Others Don''t. Collins and his team analyzed companies that made the leap to great results and sustained those results for at least fifteen years.', 18.99, '978-0066620992', 2001, 9, 'good-to-great.jpg', 40, false, NOW(), NOW()),

-- HISTORY
('A Short History of Nearly Everything', 'Bill Bryson', 'Bill Bryson takes readers on a journey through science and history, explaining how we got from nothing to everything in an entertaining and accessible way.', 19.99, '978-0767908184', 2003, 10, 'short-history.jpg', 38, true, NOW(), NOW()),
('The Guns of August', 'Barbara W. Tuchman', 'A dramatic account of the first month of World War I. Tuchman''s masterpiece brings to life the personalities and events that led to the Great War.', 17.99, '978-0345476098', 1962, 10, 'guns-of-august.jpg', 30, false, NOW(), NOW()),

-- SCIENCE
('A Brief History of Time', 'Stephen Hawking', 'From the Big Bang to black holes, Stephen Hawking explains complex cosmological concepts in a way that non-scientists can understand.', 18.99, '978-0553380163', 1988, 11, 'brief-history-time.jpg', 42, true, NOW(), NOW()),
('The Selfish Gene', 'Richard Dawkins', 'A gene-centered view of evolution that revolutionized our understanding of natural selection. Dawkins explains how genes shape behavior and survival.', 16.99, '978-0198788607', 1976, 11, 'selfish-gene.jpg', 35, false, NOW(), NOW()),

-- TECHNOLOGY
('Clean Code', 'Robert C. Martin', 'A Handbook of Agile Software Craftsmanship. Uncle Bob teaches programmers how to write code that is readable, maintainable, and elegant.', 24.99, '978-0132350884', 2008, 12, 'clean-code.jpg', 55, true, NOW(), NOW()),
('The Pragmatic Programmer', 'David Thomas & Andrew Hunt', 'Your Journey to Mastery. A classic guide to software development that covers topics from personal responsibility to architectural techniques.', 23.99, '978-0135957059', 1999, 12, 'pragmatic-programmer.jpg', 48, false, NOW(), NOW()),
('Algorithms to Live By', 'Brian Christian & Tom Griffiths', 'The Computer Science of Human Decisions. Discover how algorithms used by computers can untangle human problems and help make better decisions.', 17.99, '978-1627790369', 2016, 12, 'algorithms-live-by.jpg', 40, false, NOW(), NOW());

-- Verify data
SELECT COUNT(*) as total_books FROM books;
SELECT c.name, COUNT(b.id) as book_count 
FROM category c 
LEFT JOIN books b ON c.id = b.category_id 
GROUP BY c.id, c.name 
ORDER BY book_count DESC;

