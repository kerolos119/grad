-- Create the database
CREATE DATABASE IF NOT EXISTS EyesOnPlants;

-- Select the database
USE EyesOnPlants;

-- Create Users table
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    phone_number VARCHAR(20),
    address TEXT,
    role ENUM('USER', 'SELLER', 'ADMIN') NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create Plants table
CREATE TABLE plants (
    plant_id INT AUTO_INCREMENT PRIMARY KEY,
    plant_name VARCHAR(100) NOT NULL,
    scientific_name VARCHAR(100),
    type VARCHAR(50),
    description TEXT,
    plant_stage ENUM('SEED', 'LEAF', 'FLOWERS', 'FRUITS'),
    user_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
);

-- Create Products table with enhanced fields for plant e-commerce
CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    category ENUM('LIVE_PLANTS', 'DRIED_PLANTS', 'SEEDS', 'PLANTERS', 'GARDENING_TOOLS', 
                 'PLANT_CARE', 'FERTILIZERS', 'INDOOR_PLANTS', 'OUTDOOR_PLANTS', 
                 'EXOTIC_PLANTS', 'SUCCULENTS', 'HERBS') NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    description TEXT,
    stock_quantity INT NOT NULL DEFAULT 0,
    is_on_sale BOOLEAN DEFAULT FALSE,
    discount_price DECIMAL(10, 2),
    care_instructions TEXT,
    watering_frequency VARCHAR(100),
    sunlight_requirements VARCHAR(100),
    plant_height VARCHAR(50),
    plant_type VARCHAR(50),
    is_indoor BOOLEAN,
    seller_address VARCHAR(255) NOT NULL,
    seller_phone VARCHAR(20) NOT NULL,
    seller_id INT NOT NULL,
    seller_name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (seller_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Create Product Images table (for the ElementCollection in Products entity)
CREATE TABLE product_images (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

-- Create Reminders table
CREATE TABLE reminders (
    reminder_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    plant_id INT NOT NULL,
    reminder_type ENUM('WATERING', 'FERTILIZING', 'REPOTTING', 'PRUNING', 'PEST_CHECK') NOT NULL,
    next_reminder_date DATE,
    frequency INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (plant_id) REFERENCES plants(plant_id) ON DELETE CASCADE
);

-- Create Orders table for e-commerce functionality
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2) NOT NULL,
    shipping_address TEXT NOT NULL,
    billing_address TEXT,
    status ENUM('PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING',
    payment_method VARCHAR(50),
    payment_status ENUM('PENDING', 'PAID', 'FAILED', 'REFUNDED') DEFAULT 'PENDING',
    tracking_number VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Create Order Items table for e-commerce functionality
CREATE TABLE order_items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price_per_unit DECIMAL(10, 2) NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

-- Create Reviews table for product reviews
CREATE TABLE reviews (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    user_id INT NOT NULL,
    rating INT NOT NULL CHECK(rating BETWEEN 1 AND 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Create Cart table for shopping cart functionality
CREATE TABLE cart (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Create Cart Items table
CREATE TABLE cart_items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    cart_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cart_id) REFERENCES cart(cart_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

-- Index for common queries
CREATE INDEX idx_plants_stage ON plants(plant_stage);
CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_products_price ON products(price);
CREATE INDEX idx_products_is_on_sale ON products(is_on_sale);
CREATE INDEX idx_products_is_indoor ON products(is_indoor);
CREATE INDEX idx_products_stock ON products(stock_quantity);
CREATE INDEX idx_reminders_next_date ON reminders(next_reminder_date);
CREATE INDEX idx_orders_user ON orders(user_id);
CREATE INDEX idx_orders_status ON orders(status);


-- Example queries
-- SELECT * FROM products WHERE category = 'INDOOR_PLANTS' AND is_on_sale = TRUE;
-- SELECT * FROM products WHERE price BETWEEN 10 AND 50 ORDER BY price;
-- SELECT * FROM reminders WHERE user_id = 1 AND next_reminder_date <= CURRENT_DATE;
-- SELECT p.*, AVG(r.rating) as avg_rating FROM products p LEFT JOIN reviews r ON p.product_id = r.product_id GROUP BY p.product_id;

-- Create admin user
-- Note: Password is 'admin123' - in production, use a properly hashed password
-- INSERT INTO users (username, email, password, first_name, last_name, role) 
-- VALUES ('admin', 'admin@eyesonplants.com', '$2a$10$xJQx.e5deVC3zOLGRYcBWOO1PvY4pA0rZH6rEEwiBrY0QCVMWp9t2', 'System', 'Administrator', 'ADMIN');
--password: admin123