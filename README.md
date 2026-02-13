# 📚 Context-Aware Bookstore Application

A full-stack Spring Boot E-Commerce application for managing and selling books.

## 🚀 Quick Start Guide

### Prerequisites
Ensure you have the following installed:
- **Java 17** or higher
- **Maven 3.6** or higher
- **MySQL 8.0**

### 1️⃣ Database Setup
1. Open your MySQL client (Workbench, CLI, etc.).
2. Create the database:
   ```sql
   CREATE DATABASE bookstoredb;
   ```
3. Update `src/main/resources/application.properties` with your credentials:
   ```properties
   spring.datasource.username=root
   spring.datasource.password=YOUR_PASSWORD
   ```

### 2️⃣ Run the Application
Open a terminal in this folder and run:
```bash
mvn spring-boot:run
```
*The app will start at [http://localhost:8080](http://localhost:8080)*

### 3️⃣ Login
- **Admin**: `admin@bookstore.com` / `admin123`
- **User**: Register a new account or use `user@bookstore.com` / `password` (if seeded)

---

## 🛠️ Troubleshooting

### ❌ "Port 8080 is already in use"
**Fix:** Open `src/main/resources/application.properties` and add:
```properties
server.port=8081
```
Then restart and go to `http://localhost:8081`.

### ❌ "Access denied for user 'root'@'localhost'"
**Fix:**
1. Check if your MySQL password in `application.properties` is correct.
2. If you forgot your root password, you may need to reset it.

### ❌ "Command 'mvn' not found"
**Fix:**
1. Ensure Maven is installed and added to your system `PATH`.
2. Windows users: Search "Edit the system environment variables" -> Environment Variables -> Path -> Edit -> New -> Paste path to `apache-maven/bin`.
3. Restart your terminal.

### ❌ Images not saving/loading
**Fix:**
Ensure the upload directory exists or update `app.upload.dir` in `application.properties`:
```properties
# Example for Windows
app.upload.dir=C:/bookstore/uploads
```
Create the folder: `mkdir C:\bookstore\uploads`

---

## 📦 Project Structure
```
├── src/main/java          # Backend Source Code
│   ├── controller         # API & View Controllers
│   ├── service            # Business Logic
│   ├── repository         # Database Interaction
│   ├── entity             # Database Models
│   └── config             # Security & App Config
├── src/main/resources     # Assets & Config
│   ├── static             # CSS, JS, Images
│   ├── templates          # HTML Views (Thymeleaf)
│   └── application.properties # App Config
└── pom.xml                # Maven Dependencies
```

## 🤝 Contributing
1. Fork the repo.
2. Create a branch: `git checkout -b feature/MyFeature`
3. Commit: `git commit -m "Add MyFeature"`
4. Push: `git push origin feature/MyFeature`
5. Create a Pull Request.
