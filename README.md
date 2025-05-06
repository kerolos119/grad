# GreenHub API

A Spring Boot RESTful API for a comprehensive plant e-commerce platform and gardening assistant.

## Core Functionalities

- **Plant & Product Management**: CRUD operations for plant products with detailed categorization
- **User Authentication**: Secure JWT-based authentication system
- **Plant Care Assistant**: Reminders, growth tracking, and care instructions
- **AI Plant Advisor**: AI-powered plant care recommendations and troubleshooting
- **Push Notifications**: Real-time notifications via Firebase Cloud Messaging
- **Shopping Experience**: Cart management, order processing, and payment integration
- **Reviews & Ratings**: Product review system
- **RESTful API Design**: Standardized responses and comprehensive error handling
- **API Documentation**: Swagger/OpenAPI integration

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
- `GET /api/v1/reminders` - Get all reminders
- `GET /api/v1/reminders/{reminderId}` - Get reminder by ID
- `POST /api/v1/reminders` - Create a new reminder
- `PUT /api/v1/reminders/{reminderId}` - Update reminder
- `DELETE /api/v1/reminders/{reminderId}` - Delete reminder
- `GET /api/v1/reminders/plant/{plantId}` - Get reminders for a plant
- `GET /api/v1/reminders/search` - Search reminders with filters and pagination
- `POST /api/v1/reminders/{reminderId}/send-notification` - Send notification for a specific reminder
- `POST /api/v1/reminders/send-due-reminders` - Process and send all due reminders

#### Notifications
- `POST /notifications/token` - Send notification to a specific device token (Admin only)
- `POST /notifications/topic` - Send notification to a topic (Admin only)
- `POST /notifications/subscribe` - Subscribe tokens to a topic
- `POST /notifications/unsubscribe` - Unsubscribe tokens from a topic
- `POST /notifications/tokens` - Send notification to multiple tokens (Admin only)
- `POST /notifications/reminder` - Send a plant care reminder notification to a user
- `POST /notifications/send-plant-reminder` - Send a plant reminder directly to a device token

### Reviews & Ratings
- `GET /api/reviews/product/{productId}` - Get reviews for a product
- `POST /api/reviews/product/{productId}` - Add a review
- `PUT /api/reviews/{reviewId}` - Update a review
- `DELETE /api/reviews/{reviewId}` - Delete a review
- `GET /api/reviews/user/{userId}` - Get reviews by user

### AI-Powered Services

#### Plant AI Assistant
- `POST /api/v1/ai/predict` - Get AI-generated response for any plant-related query
- `GET /api/v1/ai/plant-care/{plantName}` - Get AI-powered care advice for a specific plant

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
  "id": 1,
  "userId": 101,
  "userName": "plantlover",
  "plantId": 1,
  "plantName": "Monstera Deliciosa",
  "reminderType": "WATERING",
  "nextReminderDate": "2023-06-15",
  "frequency": 7
}
```

### Push Notification Request
```json
{
  "title": "Plant Care Reminder",
  "body": "Time to water your Monstera Deliciosa",
  "token": "device-token-123",
  "topic": "plant-care",
  "data": {
    "plantId": "1",
    "reminderType": "WATERING",
    "timestamp": "1623764345123"
  }
}
```

## Setup & Installation

### Requirements
- Java 17+
- Maven 3.6+
- MySQL/PostgreSQL
- Firebase project
- Local or remote AI service endpoint (default: http://127.0.0.1:5000/predict)

### Quick Start
1. Clone the repository
   ```
   git clone https://github.com/yourusername/greenhub-api.git
   ```

2. Configure database in `application.properties`:
   ```
   spring.datasource.url=jdbc:mysql://localhost:3306/greenhub_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. **Firebase Setup**:
   - Create a Firebase project in the [Firebase Console](https://console.firebase.google.com/)
   - Go to Project Settings > Service accounts
   - Click "Generate new private key" to download your service account credentials
   - Rename the downloaded file to `firebase-service-account.json`
   - Place this file in `src/main/resources/`
   - **IMPORTANT**: This file contains sensitive credentials and should never be committed to version control
   - Update `application.properties` with:
     ```
     firebase.service-account-file=classpath:firebase-service-account.json
     firebase.database-url=https://your-project-id.firebaseio.com
     ```

4. Build: `mvn clean install`
5. Run: `mvn spring-boot:run`
6. Access API at `http://localhost:8080`
7. View documentation at `http://localhost:8080/swagger-ui.html`

## Security & Integration

- **CORS enabled**: Frontend integration ready
- **JWT Authentication**: Secure API access
- **Response Standardization**: Consistent API behavior
- **Mobile Support**: Push notifications for apps

## Security Notes

- **Never commit sensitive credentials** to the repository
- Firebase service account files contain private keys and should be kept confidential
- The `.gitignore` file excludes `src/main/resources/firebase-service-account.json`
- For production deployment, consider using environment variables or a secure vault solution instead of file-based credentials

## AI Integration

The GreenHub API integrates with an AI service that provides plant care recommendations and answers to gardening questions. By default, the API communicates with a local AI service running at http://127.0.0.1:5000/predict.

### Configuration
To change the AI service endpoint, modify the `apiUrl` in the `AIService.java` file.

### Usage
Send plant care questions and receive AI-powered recommendations through the available endpoints:
- Use `/api/v1/ai/predict` with a JSON body containing a "prompt" field for general queries
- Use `/api/v1/ai/plant-care/{plantName}` to get specific care advice for a given plant

Example: