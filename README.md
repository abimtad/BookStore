# Online Bookstore System

A Java-based Online Bookstore System built with Spring Boot, featuring user authentication, book browsing, shopping cart functionality, and admin dashboard.

## Features

- User authentication (login/registration)
- Book browsing and searching
- Shopping cart functionality
- Admin dashboard for inventory management
- MySQL database integration
- RESTful API endpoints
- JWT-based authentication
- Role-based access control

## Technical Stack

- Java 17+
- Spring Boot 3.2.3
- Spring Security
- Spring Data JPA
- MySQL 8.0+
- Maven
- JWT for authentication
- Lombok

## Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

## Setup Instructions

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd online-bookstore-java
   ```

2. Configure MySQL:
   - Create a MySQL database named `bookstore`
   - Update `application.properties` with your MySQL credentials

3. Build the project:
   ```bash
   mvn clean install
   ```

4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## API Endpoints

### Authentication
- POST `/api/auth/register` - Register a new user
- POST `/api/auth/login` - Login user

### Books
- GET `/api/books` - Get all books
- GET `/api/books/{id}` - Get book by ID
- GET `/api/books/search` - Search books
- POST `/api/books` - Add new book (Admin only)
- PUT `/api/books/{id}` - Update book (Admin only)
- DELETE `/api/books/{id}` - Delete book (Admin only)

### Shopping Cart
- GET `/api/cart` - Get user's cart
- POST `/api/cart/items` - Add item to cart
- PUT `/api/cart/items/{id}` - Update cart item
- DELETE `/api/cart/items/{id}` - Remove item from cart

### Orders
- GET `/api/orders` - Get user's orders
- POST `/api/orders` - Place new order
- GET `/api/orders/{id}` - Get order details

## Database Schema

The application uses the following main entities:
- User
- Book
- Order
- OrderItem
- ShoppingCart
- CartItem

## Security

- Passwords are hashed using BCrypt
- JWT-based authentication
- Role-based access control (USER, ADMIN)
- SQL injection prevention
- XSS protection

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
