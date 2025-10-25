# 🛍️ RESTful APIs for Online eCommerce Platform using Spring Boot and Spring Security with JWT-based Authentication.

The Ecommerce RESTful API is a production-grade backend system built with Spring Boot.
It powers a modern online shopping platform, featuring secure JWT-based authentication, role-based authorization, and seamless payment & media integrations.

Designed with scalability, security, and clean architecture in mind, this API serves as a solid foundation for any eCommerce system — from startups to enterprise-grade applications.

## 🚀 Key Features

### ✅ User Authentication & Authorization 

- Secure login/registration using JWT
- Role-based access control (Admin, User)
- Password encryption using BCrypt

### ✅ Admin Capabilities

- Bulk product upload via Excel
- Bulk image upload and Cloudinary integration
- Create, update, delete, and manage all products, users, shippers, orders and many more.

### ✅ User Capabilities

- Browse, search, filter, and sort products
- Add products to cart and place orders
- Secure payment through Razorpay
- Rate products and write or read reviews

### ✅ System Features

- Pagination, Sorting & Filtering for all lists
- Custom access denied & exception handling
- Centralized response structure and error messages
- Integrated Swagger UI for API documentation
- Media management through Cloudinary
- Payment gateway integration with Razorpay

## 🧠 Tech Stack

| Category              | Technologies Used            |
| --------------------- | ---------------------------- |
| **Language**          | Java 17+                     |
| **Framework**         | Spring Boot                  |
| **Security**          | Spring Security, JWT, BCrypt |
| **ORM & Data Layer**  | Hibernate, Spring Data JPA   |
| **Database**          | MySQL                        |
| **Build Tool**        | Maven                        |
| **API Documentation** | Swagger / OpenAPI            |
| **Payment Gateway**   | Razorpay                     |
| **File Storage**      | Cloudinary                   |
| **Version Control**   | Git & GitHub                 |

## 🧩 Architecture Overview

A layered monolithic architecture following industry standards:

Controller → Service → Repository → Entity (Model)
          ↓
     DTOs & Mappers
          ↓
     Global Exception Handler

- Spring Security + JWT → ensures secure access across roles
- MySQL → serves as persistent data layer
- Cloudinary → handles all product & user media files
- Razorpay → enables real-time payment processing

## 🗃️ Database Schema (ER Diagram)
![ER_Diagram](https://github.com/NirbanPal/Shoppella-Ecommerce-Website/blob/main/ER_Diagram.png)


The schema includes entities like Users, Products, Orders, Order Items, Cart, Cart Items, Payments, Reviews, Address, Shipping, Shipper with well-defined relationships between them.

## ⚙️ Getting Started

### 🧰 Prerequisites
Ensure you have the following installed:

- ☕ JDK 17+

- 🛠️ Maven 3+

- 🗄️ MySQL Database

### 💻 Installation & Setup

- #### Clone the repository
  ```bash
  git clone https://github.com/NirbanPal/Shoppella-Ecommerce-Website.git
  ```

- #### Navigate to project directory
  ```bash
  cd ecommerceAppBackend
  ```

- #### Build and run the application
  ```bash
  mvnw spring-boot:run
  ```

### 🗄️ Configure the Database
  Update your .env (or .yml) file located at
src/main/resources/.env
  ```bash
  DB_HOST=
  DB_PORT=
  DB_NAME=
  DB_USERNAME=
  DB_PASSWORD=
  ```

### ☁️ Cloudinary Configuration
To enable image storage:
- Sign up at <a href="https://cloudinary.com/users/login">Cloudinary</a>.
- Obtain credentials from your Cloudinary dashboard.
- Add the following to your .env file:
```bash
CLOUDINARY_NAME=your-cloud-name
CLOUDINARY_KEY=your-api-key
CLOUDINARY_SECRET=your-api-secret
```
### 💳 Razorpay Payment Integration
To enable online payments:
- Register at <a href="https://razorpay.com/docs/payments/dashboard/account-settings/api-keys/#test-mode-api-keys">Razorpay</a>.
- Get your credentials under Settings → API Keys.
- Add them to your configuration:
```bash
RAZORPAY_KEY_ID=your-key-id
RAZORPAY_SECRET_ID=your-secret-id
RAZORPAY_WEBHOOK_SECRET=your-webhook-secret
```

### 👨‍💻 Admin Access Setup

Before using the Admin APIs, create an admin account manually in the MySQL database:
```bash
# Suppose your DB_NAME is ecomdb. So put your db name accordingly.
USE ecomdb;

INSERT INTO users (email, password, first_name, last_name, phone_number,user_role,user_account_status)
VALUES ('admin@gmail.com', '$2a$10$6mMR41Be5L4ofFPvHmaNq.fFt9taTYT.T0UQda3R2bWhVmKCuRA.a', 'Admin', 'Admin', '1234567890','ROLE_ADMIN','ACTIVE');
```

Username/email: admin@gmail.com

Password: admin#880

### 📘 API Documentation

Swagger UI is integrated for easy API testing and visualization.
After running the application, open your browser and visit:

👉 <a href="http://localhost:8080/swagger-ui/index.html">http://localhost:8080/swagger-ui/index.html</a>

### Endpoints

![cartAndAdminEndpoints](https://github.com/NirbanPal/Shoppella-Ecommerce-Website/blob/main/endPoints-SS/customerAndAddressEndpoints.png)
![customerAndAddressEndpoints](https://github.com/NirbanPal/Shoppella-Ecommerce-Website/blob/main/endPoints-SS/customerAndAddressEndpoints.png)
![orderAndShipperEndpoints](https://github.com/NirbanPal/Shoppella-Ecommerce-Website/blob/main/endPoints-SS/orderAndShipperEndpoints.png)
![paymentAndLoginEndpoints](https://github.com/NirbanPal/Shoppella-Ecommerce-Website/blob/main/endPoints-SS/paymentAndLoginEndpoints.png)
![productEndpoints](https://github.com/NirbanPal/Shoppella-Ecommerce-Website/blob/main/endPoints-SS/productEndpoints.png)
![reviewAndShippingEndpoints](https://github.com/NirbanPal/Shoppella-Ecommerce-Website/blob/main/endPoints-SS/reviewAndShippingEndpoints.png)


