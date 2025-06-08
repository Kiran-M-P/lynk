# Lynk - URL Shortener Backend

A URL shortening service built with Spring Boot that provides user authentication, URL management, and analytics tracking.

## 🚀 Technologies Used

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Security** - JWT-based authentication
- **Spring Data JPA** - Data persistence layer
- **Hibernate** - ORM framework
- **MySQL** - Primary database
- **Maven** - Dependency management
- **JWT** - Token-based authentication

## 📋 Features

- **User Authentication** - Register and login with JWT tokens
- **URL Shortening** - Convert long URLs into short, manageable links
- **User Management** - Personal URL dashboard
- **Analytics** - Track URL clicks and usage statistics
- **Date Range Filtering** - View analytics for specific time periods
- **Secure Access** - Role-based access control

## 📚 API Documentation

### Authentication Endpoints

#### Register User
- **POST** `/api/auth/public/register`
- **Description**: Register a new user account
- **Request Body**:
  ```json
  {
    "username": "john_doe",
    "email": "john.doe@example.com",
    "role": ["ROLE_USER"],
    "password": "securePassword123"
  }
  ```

#### Login User
- **POST** `/api/auth/public/login`
- **Description**: Authenticate user and receive JWT token
- **Request Body**:
  ```json
  {
    "username": "john_doe",
    "password": "securePassword123"
  }
  ```
- **Response**: JWT token for authorization

### URL Management Endpoints

> **Note**: All URL endpoints require JWT authentication. Include the token in the Authorization header:
> ```
> Authorization: Bearer <your-jwt-token>
> ```

#### Shorten URL
- **POST** `/api/urls/shorten`
- **Description**: Create a shortened URL
- **Request Body**:
  ```json
  {
    "originalUrl": "https://claude.ai"
  }
  ```
- **Response**: Shortened URL details

#### Get My URLs
- **GET** `/api/urls/myurls`
- **Description**: Retrieve all URLs created by the authenticated user
- **Response**: List of user's shortened URLs

#### URL Analytics
- **GET** `/api/urls/analytics/{shortCode}`
- **Description**: Get detailed analytics for a specific shortened URL
- **Parameters**:
  - `shortCode`: The short code of the URL (e.g., bt132GmG)
  - `startDate`: Start date for analytics (format: 2025-06-01T00:00:00)
  - `endDate`: End date for analytics (format: 2025-06-09T00:00:00)
- **Example**: `/api/urls/analytics/bt132GmG?startDate=2025-06-01T00:00:00&endDate=2025-06-09T00:00:00`

#### Total URL Clicks
- **GET** `/api/urls/totalClicks`
- **Description**: Get total clicks for all user's URLs within a date range
- **Parameters**:
  - `startDate`: Start date (format: 2025-06-01)
  - `endDate`: End date (format: 2025-06-08)
- **Example**: `/api/urls/totalClicks?startDate=2025-06-01&endDate=2025-06-08`

### URL Redirection

#### Redirect to Original URL
- **GET** `/{shortCode}`
- **Description**: Redirect to the original URL using the short code
- **Example**: `http://localhost:8080/bt132GmG`
- **Response**: HTTP 302 redirect to original URL


## 📊 Usage Examples

### Complete Flow Example

1. **Register a user**:
   ```bash
   curl -X POST http://localhost:8080/api/auth/public/register \
   -H "Content-Type: application/json" \
   -d '{"username":"testuser","email":"test@example.com","role":["ROLE_USER"],"password":"password123"}'
   ```

2. **Login to get JWT token**:
   ```bash
   curl -X POST http://localhost:8080/api/auth/public/login \
   -H "Content-Type: application/json" \
   -d '{"username":"testuser","password":"password123"}'
   ```

3. **Shorten a URL**:
   ```bash
   curl -X POST http://localhost:8080/api/urls/shorten \
   -H "Authorization: Bearer YOUR_JWT_TOKEN" \
   -H "Content-Type: application/json" \
   -d '{"originalUrl":"https://www.google.com"}'
   ```

4. **Access shortened URL**:
   ```bash
   curl -L http://localhost:8080/GENERATED_SHORT_CODE
   ```
