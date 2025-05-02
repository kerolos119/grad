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

### Products

- `GET /api/v1/products` - Get all products (with filtering and pagination)
- `GET /api/v1/products/{productId}` - Get product by ID
- `POST /api/v1/products` - Create a new product
- `PUT /api/v1/products/{productId}` - Update product
- `DELETE /api/v1/products/{productId}` - Delete product
- `GET /api/v1/products/name/{productName}` - Find product by name
- `GET /api/v1/products/category/{category}` - Find products by category
- `GET /api/v1/products/seller/{sellerId}` - Find products by seller
- `GET /api/v1/products/sale` - Find products on sale
- `GET /api/v1/products/indoor` - Find indoor or outdoor plants
- `GET /api/v1/products/price-range` - Find products by price range
- `GET /api/v1/products/keyword` - Search products by keyword
- `GET /api/v1/products/in-stock` - Find in-stock products
- `GET /api/v1/products/search` - Advanced product search
- `GET /api/v1/products/stage/{stage}` - Find plants by growth stage

### Plant Care Reminders

- `GET /api/v1/reminders` - Get all reminders
- `GET /api/v1/reminders/{reminderId}` - Get reminder by ID
- `POST /api/v1/reminders` - Create a new reminder
- `PUT /api/v1/reminders/{reminderId}` - Update reminder
- `DELETE /api/v1/reminders/{reminderId}` - Delete reminder
- `GET /api/v1/reminders/plant/{plantId}` - Find reminder by plant ID
- `GET /api/v1/reminders/search` - Search reminders

### Users

- `POST /api/v1/users` - Register a new user
- `POST /api/v1/users/login` - Authenticate and receive access token
- `GET /api/v1/users/{userId}` - Get user by ID
- `PUT /api/v1/users/{userId}` - Update user information
- `DELETE /api/v1/users/{userId}` - Delete user

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