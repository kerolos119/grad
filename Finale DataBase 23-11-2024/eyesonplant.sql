CREATE DATABASE PlantCareSystem;

-- استخدام قاعدة البيانات
USE PlantCareSystem;

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

-- إنشاء جدول مفتاح الوعي (KeyAwareness)
CREATE TABLE KeyAwareness (
    key_id INT AUTO_INCREMENT PRIMARY KEY, -- معرف المفتاح
    interesting_story TEXT, -- القصة الشيقة
    care_guide ENUM('Water', 'Fertilizer') NOT NULL, -- دليل العناية (ماء أو سماد)
    plant_description TEXT -- وصف عام للنبات
);

-- إنشاء جدول التذكيرات
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