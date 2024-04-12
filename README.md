# Library Management System

## Overview

The Library Management System is a software application designed to help manage the operations of a library. It provides functionalities for managing books, patrons, borrowing and returning books, and handling exceptions that may occur during these operations.

## Features

### 1. Book Management
- Add new books to the library.
- Update existing book information.
- Get details of a specific book by its ID.
- Get a list of all books available in the library.
- Delete books from the library.

### 2. Patron Management
- Add new patrons to the library.
- Update existing patron information.
- Get details of a specific patron by their ID.
- Get a list of all patrons registered in the library.
- Delete patrons from the library.

### 3. Borrowing and Returning Books
- Borrow a book from the library.
- Return a borrowed book to the library.

## Technologies Used

- **Spring Boot**: Framework for building Java applications.
- **JUnit**: Unit testing framework for Java.
- **Mockito**: Mocking framework for unit tests in Java.
- **Spring Data JPA**: Simplifies data access using the JPA specification.
- **H2 Database**: Lightweight in-memory database for testing.
- **Maven**: Dependency management tool for Java projects.

## Project Structure

The project is organized into the following packages:

- **Controllers**: Contains the controllers responsible for handling HTTP requests and responses.
- **DTO**: Data Transfer Object classes used to transfer data between layers.
- **Exceptions**: Custom exception classes for handling errors gracefully.
- **Services**: Contains the service classes that implement business logic.
- **Repositories**: Interfaces for database interaction using Spring Data JPA.
- **Tests**: Unit and integration tests for controllers , repositories and services.

## How to Run

1. Clone the repository to your local machine.
2. Open the project in your preferred IDE.
3. Make sure you have Maven installed.
4. Run `mvn clean install` to build the project.
5. Run `mvn spring-boot:run` to start the application.
6. Access the endpoints using a REST client like Postman or cURL.

## Testing

The project includes unit tests for controllers, repositories and services. To run the tests:

1. Navigate to the project directory.
2. Run `mvn test` to execute all tests.

- Mamoun Mohamed
- Mamounmohamed711@gmail.com
