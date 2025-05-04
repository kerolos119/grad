# Plant E-Commerce API

A Spring Boot RESTful API for an e-commerce platform specializing in live plants, dried plants, and gardening supplies.

## Features

- **Product Management**: Complete CRUD operations for plant products
- **Product Categories**: Support for various plant types (live plants, dried plants, seeds, etc.)
- **Search & Filtering**: Advanced search capabilities with multiple filters
- **User Management**: User registration, authentication, and profile management
- **Plant Care**: Reminder system for watering and plant care
- **Growth Tracking**: Track plant growth stages (seed, leaf, flowers, fruits)
- **Standardized API Responses**: Consistent response format for easy frontend integration
- **Error Handling**: Comprehensive exception handling with meaningful messages
- **Documentation**: Swagger API documentation
- **Security**: JWT authentication for secure access

## API Endpoints

### Authentication & User Management

#### Authentication
- `POST /api/v1/auth/login` - Login with email and password
- `POST /api/v1/auth/register` - Register a new user with authentication
- `POST /api/v1/auth/refresh` - Refresh an access token using refresh token

#### User Management
- `POST /api/v1/users` - Register a new user
- `GET /api/v1/users` - Search users (with pagination)
- `GET /api/v1/users/{userId}` - Get user by ID
- `GET /api/v1/users/username/{username}` - Get user by username
- `GET /api/v1/users/email/{email}` - Get user by email
- `PUT /api/v1/users/{userId}` - Update user information
- `DELETE /api/v1/users/{userId}` - Delete user

### Products & Categories

#### Products
- `GET /api/products` - Get all products (with filtering and pagination)
- `GET /api/products/{productId}` - Get product by ID
- `POST /api/products` - Create a new product
- `PUT /api/products/{productId}` - Update product
- `DELETE /api/products/{productId}` - Delete product
- `GET /api/products/search` - Advanced product search with multiple parameters
- `GET /api/products/category/{category}` - Find products by category
- `GET /api/products/seller/{sellerId}` - Find products by seller
- `GET /api/products/sale` - Find products on sale
- `GET /api/products/indoor` - Find indoor plants
- `GET /api/products/outdoor` - Find outdoor plants
- `GET /api/products/price` - Find products by price range
- `GET /api/products/in-stock` - Find products in stock

#### Categories
- `GET /api/categories` - Get all product categories
- `GET /api/categories/{categoryId}` - Get category by ID

### Shopping & Orders

#### Cart Management
- `GET /api/cart/user/{userId}` - Get user's shopping cart
- `POST /api/cart/user/{userId}/items` - Add item to cart
- `PUT /api/cart/user/{userId}/items/{itemId}` - Update cart item
- `DELETE /api/cart/user/{userId}/items/{itemId}` - Remove item from cart
- `DELETE /api/cart/user/{userId}` - Clear cart

#### Order Management
- `GET /api/orders` - Get all orders (admin)
- `GET /api/orders/{orderId}` - Get order by ID
- `GET /api/orders/user/{userId}` - Get orders by user
- `POST /api/orders` - Create a new order
- `PUT /api/orders/{orderId}/status` - Update order status
- `GET /api/orders/track/{trackingNumber}` - Track order by tracking number

#### Payments
- `POST /api/payments/process` - Process payment
- `POST /api/payments/webhook` - Payment service webhook
- `GET /api/payments/status/{paymentId}` - Check payment status

### Plants & Plant Care

#### Plants
- `GET /api/plants` - Get all plants
- `GET /api/plants/{plantId}` - Get plant by ID
- `POST /api/plants` - Add a new plant
- `PUT /api/plants/{plantId}` - Update plant
- `DELETE /api/plants/{plantId}` - Delete plant
- `GET /api/plants/user/{userId}` - Get user's plants
- `GET /api/plants/stage/{stage}` - Find plants by growth stage

#### Reminders
- `GET /api/reminders` - Get all reminders
- `GET /api/reminders/{reminderId}` - Get reminder by ID
- `POST /api/reminders` - Create a new reminder
- `PUT /api/reminders/{reminderId}` - Update reminder
- `DELETE /api/reminders/{reminderId}` - Delete reminder
- `GET /api/reminders/user/{userId}` - Get user's reminders
- `GET /api/reminders/plant/{plantId}` - Get reminders for a plant
- `GET /api/reminders/due` - Get due reminders
- `POST /api/reminders/complete/{reminderId}` - Mark reminder as completed

### Reviews & Ratings

- `GET /api/reviews/product/{productId}` - Get reviews for a product
- `POST /api/reviews/product/{productId}` - Add a review
- `PUT /api/reviews/{reviewId}` - Update a review
- `DELETE /api/reviews/{reviewId}` - Delete a review
- `GET /api/reviews/user/{userId}` - Get reviews by user

## Data Models

### Product

```json
{
  "id": 1,
  "productName": "Monstera Deliciosa",
  "category": "INDOOR_PLANTS",
  "price": 29.99,
  "description": "The Swiss Cheese Plant, known for its unique leaf holes",
  "stockQuantity": 15,
  "imageUrls": ["url1", "url2"],
  "isOnSale": true,
  "discountPrice": 24.99,
  "careInstructions": "Keep soil moist but not wet, place in indirect light",
  "wateringFrequency": "Weekly",
  "sunlightRequirements": "Bright indirect light",
  "plantHeight": "40-60cm",
  "plantType": "Tropical",
  "isIndoor": true,
  "sellerAddress": "123 Plant St.",
  "sellerPhone": "+1234567890",
  "sellerId": 101,
  "sellerName": "Plant Paradise"
}
```

### Plant

```json
{
  "plantId": 1,
  "plantName": "Monstera Deliciosa",
  "scientificName": "Monstera deliciosa",
  "type": "Tropical",
  "description": "The Swiss Cheese Plant, known for its unique leaf holes",
  "plantStage": "LEAF",
  "user": {
    "userId": 101,
    "username": "plantlover"
  },
  "createdAt": "2023-06-15T10:30:45.123Z",
  "updatedAt": "2023-06-15T10:30:45.123Z"
}
```

### Reminder

```json
{
  "user": { "userId": 101, "username": "plantlover" },
  "plant": { "plantId": 1, "name": "Monstera Deliciosa" },
  "plantId": "1",
  "reminderType": "WATERING",
  "nextReminderDate": "2023-06-15",
  "frequency": 7
}
```


## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL/PostgreSQL database

### Installation

1. Clone the repository
   ```
   git clone https://github.com/yourusername/plant-ecommerce-api.git
   ```

2. Configure database in `application.properties`
   ```
   spring.datasource.url=jdbc:mysql://localhost:3306/plant_store
   spring.datasource.username=YOUR_USERNAME
   spring.datasource.password=YOUR_PASSWORD
   ```

3. Build the project
   ```
   mvn clean install
   ```

4. Run the application
   ```
   mvn spring-boot:run
   ```

5. Access the API at `http://localhost:8080`
   
6. View API documentation at `http://localhost:8080/swagger-ui.html`

## Frontend Integration

The API is designed for easy integration with web and mobile frontends:

- **Standardized Response Format**: All responses follow a consistent structure
- **CORS Support**: Built-in cross-origin support for web applications
- **Detailed Error Messages**: Specific validation feedback for forms

Example response:

```json
{
  "success": true,
  "status": 200,
  "message": "Products retrieved successfully",
  "timestamp": "2023-06-15T10:30:45.123Z",
  "data": [
    {
      "id": 1,
      "productName": "Monstera Deliciosa",
      "category": "INDOOR_PLANTS",
      "price": 29.99,
      /* other product fields */
    }
  ]
}
``` 