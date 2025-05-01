create database EyesOnPlants;

use EyesOnPlants;
alter table users modify column gender enum ('FEMALE','MALE') not null
drop TABLE users;
SHOW COLUMNS FROM users LIKE 'gender';
ALTER TABLE users 
MODIFY COLUMN gender ENUM('MALE', 'FEMALE') NOT NULL;
drop database EyesOnPlants;
show tables
-- إنشاء جدول المستخدمين
CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY, -- معرف المستخدم
    name VARCHAR(50) NOT NULL, -- اسم المستخدم
    email VARCHAR(50) UNIQUE NOT NULL, -- البريد الإلكتروني
    password VARCHAR(50) NOT NULL, -- كلمة المرور
    phone_number VARCHAR(15), -- رقم الهاتف
    gender ENUM('Male', 'Female') NOT NULL -- الجنس
);

-- إنشاء جدول النباتات مع إضافة عمود user_id
CREATE TABLE Plants (
    plant_id INT AUTO_INCREMENT PRIMARY KEY, -- معرف النبات
    plant_name VARCHAR(50) NOT NULL, -- اسم النبات
    type VARCHAR(50), -- نوع النبات
    plant_stage ENUM('Seed', 'Leaf', 'Flower', 'Fruit') NOT NULL, -- مرحلة نمو النبات
    growth_time INT, -- الوقت اللازم للنمو بالأيام
    optimal_conditions TEXT, -- الظروف المثالية
    common_diseases TEXT, -- الأمراض الشائعة
    user_id INT, -- معرف المستخدم الذي يمتلك هذا النبات
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE -- ربط النباتات بالمستخدم
);

-- إنشاء جدول الأمراض
CREATE TABLE Diseases (
    disease_id INT AUTO_INCREMENT PRIMARY KEY, -- معرف المرض
    disease_name VARCHAR(50) NOT NULL, -- اسم المرض
    affected_parts TEXT, -- الأجزاء المتأثرة
    recommended_action TEXT, -- الإجراءات الموصى بها
    symptoms TEXT, -- الأعراض
    treatment TEXT, -- العلاج
    user_id INT, -- معرف المستخدم الذي قام بالتشخيص أو الذي يتعامل مع المرض
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE -- ربط الأمراض بالمستخدم
);

CREATE TABLE KeyAwareness (
    key_id INT AUTO_INCREMENT PRIMARY KEY, -- معرف المفتاح
    interesting_story TEXT, -- القصة الشيقة
    care_guide ENUM('Water', 'Fertilizer') NOT NULL, -- دليل العناية (ماء أو سماد)
    plant_description TEXT -- وصف عام للنبات
);-- Create the database
  CREATE DATABASE EyesOnPlants;

  -- Select the database
  USE EyesOnPlants;

  -- Create Users table first (as other tables depend on it)
  CREATE TABLE Users (
      user_id INT AUTO_INCREMENT PRIMARY KEY,
      name VARCHAR(50) NOT NULL,
      email VARCHAR(50) UNIQUE NOT NULL,
      password VARCHAR(50) NOT NULL,
      phone_number VARCHAR(15),
      gender ENUM('Male', 'Female') NOT NULL
  );

  -- Create Plants table with foreign key reference
  CREATE TABLE Plants (
      plant_id INT AUTO_INCREMENT PRIMARY KEY,
      plant_name VARCHAR(50) NOT NULL,
      type VARCHAR(50),
      plant_stage ENUM('Seed', 'Leaf', 'Flower', 'Fruit') NOT NULL,
      growth_time INT,
      optimal_conditions TEXT,
      common_diseases TEXT,
      user_id INT,
      FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
  );

  -- Create Diseases table with foreign key reference
  CREATE TABLE Diseases (
      disease_id INT AUTO_INCREMENT PRIMARY KEY,
      disease_name VARCHAR(50) NOT NULL,
      affected_parts TEXT,
      recommended_action TEXT,
      symptoms TEXT,
      treatment TEXT,
      user_id INT,
      FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
  );

  -- Create KeyAwareness table
  CREATE TABLE KeyAwareness (
      key_id INT AUTO_INCREMENT PRIMARY KEY,
      interesting_story TEXT,
      care_guide ENUM('Water', 'Fertilizer') NOT NULL,
      plant_description TEXT
  );

  -- Create Reminders table with foreign key references
  CREATE TABLE Reminders (
      reminder_id INT AUTO_INCREMENT PRIMARY KEY,
      user_id INT,
      plant_id INT,
      reminder_type ENUM('Watering', 'Fertilizing') NOT NULL,
      next_reminder_date DATE,
      frequency INT,
      FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
      FOREIGN KEY (plant_id) REFERENCES Plants(plant_id) ON DELETE CASCADE
  );

  -- Create Products table with foreign key reference
  CREATE TABLE Products (
      product_id INT AUTO_INCREMENT PRIMARY KEY,
      product_name VARCHAR(50) NOT NULL,
      category ENUM('Fertilizer', 'Dried Fruits', 'Fruits', 'Gardening Tools') NOT NULL,
      price DECIMAL(10, 2) NOT NULL,
      seller_address TEXT NOT NULL,
      seller_phone VARCHAR(15) NOT NULL,
      user_id INT,
      FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
  );

  -- Example queries to verify tables
  -- SELECT * FROM Users;
  -- SELECT * FROM Plants;
  -- SELECT * FROM Diseases;
  -- SELECT * FROM KeyAwareness;
  -- SELECT * FROM Reminders;
  -- SELECT * FROM Products;

CREATE TABLE Reminders (
    reminder_id INT AUTO_INCREMENT PRIMARY KEY, -- معرف التذكير
    user_id INT, -- معرف المستخدم
    plant_id INT, -- معرف النبات
    reminder_type ENUM('Watering', 'Fertilizing') NOT NULL, -- نوع التذكير (ري أو سماد)
    next_reminder_date DATE, -- تاريخ التذكير التالي
    frequency INT, -- عدد الأيام بين التذكيرات
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE, -- الربط مع جدول المستخدمين
    FOREIGN KEY (plant_id) REFERENCES Plants(plant_id) ON DELETE CASCADE -- الربط مع جدول النباتات
);

-- إنشاء جدول المنتجات
CREATE TABLE Products (
    product_id INT AUTO_INCREMENT PRIMARY KEY, -- معرف المنتج
    product_name VARCHAR(50) NOT NULL, -- اسم المنتج
    category ENUM('Fertilizer', 'Dried Fruits', 'Fruits', 'Gardening Tools') NOT NULL, -- الفئة
    price DECIMAL(10, 2) NOT NULL, -- السعر
    seller_address TEXT NOT NULL, -- عنوان البائع
    seller_phone VARCHAR(15) NOT NULL, -- رقم هاتف البائع
    user_id INT, -- معرف المستخدم (البائع)
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE -- الربط بجدول المستخدمين
);
-- عرض بيانات جدول المستخدمين
SELECT * FROM Users;

-- عرض بيانات جدول النباتات
SELECT * FROM Plants;

-- عرض بيانات جدول الأمراض
SELECT * FROM Diseases;

-- عرض بيانات جدول مفتاح الوعي
SELECT * FROM KeyAwareness;

-- عرض بيانات جدول التذكيرات
SELECT * FROM Reminders;

-- عرض بيانات جدول المنتجات
SELECT * FROM Products;
