# 🎓 Student Management System

A **Spring Boot Web Application** for managing students, courses, teachers, and departments with **secure login and role-based access control**.

This project demonstrates a **layered architecture**, **database integration**, and **Spring Security authentication & authorization**.

---

## 🚀 Features

### 👤 Authentication & Security
- User registration and login
- Passwords encrypted using **BCrypt**
- Role-based access control (**STUDENT**, **TEACHER**)
- Secure session handling with Spring Security

### 🎓 Student Management
- Add new students *(Teacher only)*
- Edit student information *(Student role)*
- Delete students *(Teacher only)*
- View all students *(Any logged-in user)*

### 📚 Course Management
- Add courses
- View available courses

### 🏫 Department & Teacher Management
- Manage departments and teachers in the system

---

## 🧱 Project Architecture

The project follows a **standard Spring Boot layered architecture**:


| Layer | Responsibility |
|------|----------------|
| **Controller** | Handles HTTP requests and returns views |
| **Service** | Business logic & security integration |
| **Repository** | Database operations using Spring Data JPA |
| **Model (Entity)** | Maps Java classes to database tables |
| **Security Config** | Authentication & authorization rules |

---

## 🛠️ Technologies Used

- **Java 17+**
- **Spring Boot**
- **Spring Security**
- **Spring Data JPA (Hibernate)**
- **Thymeleaf (Frontend Templates)**
- **MySQL / PostgreSQL** (Configurable)
- **Maven**

---

## 📁 Project Structure
src/main/java/com/example/student_management_system

├── controller → Web request handlers

├── service → Business logic & UserDetailsService

├── repository → JPA repositories

├── model → Entity classes (tables)

├── config → Security configuration


│src/main/resources

├── templates → Thymeleaf HTML pages

├── static → CSS / JS files

└── application.properties → Database & app config
