# Authentication System with OTP Verification (Email/Phone)

A **production-ready authentication and authorization system** for any application.
It comes with OTP-based verification, JWT login, and role-based access control for **Customers** and **Sellers**.

---

### ✨ Features

* **OTP Verification** (Email / Phone)
* **OTP-based Login & Registration**

    * New users → prompted to register as Customer or Seller
    * Existing users → logged in directly with JWT token
* **JWT Authentication**
* **Email & Phone Verification**

---

### 🛠️ Tech Stack

* **Backend**: Spring Boot (Web, Security, Data JPA, Lombok)
* **Database**: PostgreSQL (via Docker Compose)
* **Security**: Spring Security, JWT
* **Email**: Java Mail Sender + Thymeleaf
* **SMS**: Twilio
* **Docs**: Swagger UI

---

### 🚀 Setup

1. Clone the repo:

   ```bash
   git clone https://github.com/ripanbaidya/Auth-Flow.git
   ```
2. Open in your IDE (IntelliJ / Eclipse).
3. Configure environment variables (for `docker-compose` and `application.yml`).
4. Install Docker if not already installed → [Download Docker](https://www.docker.com/products/docker-desktop).
5. Start PostgreSQL with Docker:

   ```bash
   docker compose up -d
   ```
6. Run the Spring Boot app (`./mvnw spring-boot:run` or via IDE).
7. Visit: `http://localhost:8000`
8. API docs: [Swagger UI](http://localhost:8000/api/v1/swagger-ui/index.html)

---

### 📌 API Endpoints

### 1. Send OTP

**POST** `/api/v1/auth/send-otp`

Request:

```json
{
  "email": "string",
  "phoneNumber": "string"
}
```

Response:

```json
{
  "success": true,
  "message": "OTP sent successfully",
  "data": {
    "expiresIn": 300
  }
}
```

---

### 2. Verify OTP

**POST** `/api/v1/auth/verify-otp`

Request:

```json
{
  "email": "user@example.com",
  "otp": "761736"
}
```

#### ✅ Success (existing user → login)

```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "jwt-token",
    "role": "ROLE_SELLER"
  }
}
```

#### ❌ Fail (new user → must register)

```json
{
  "success": false,
  "message": "User not found, please register",
  "data": {}
}
```

---

### 3. Register User / Seller

**POST** `/api/v1/auth/signup/customer` or `/api/v1/auth/signup/seller` 

Request (role-based):

```json
{
  "fullName": "John Doe",
  "email": "john@example.com",
  "phoneNumber": "9999999999"
}
```

#### Response

```json
{
  "success": true,
  "message": "Registration successful",
  "data": {
    "token": "jwt-token",
    "role": "ROLE_CUSTOMER" // or ROLE_SELLER
  }
}
```

---

### ✅ Summary

* 🔑 OTP-first flow for **email/phone verification**
* 🆕 Register as **Customer** or **Seller**
* 🔒 Secure login with **JWT tokens**
* 📜 Swagger docs for easy testing

---

### ☝️One Request

If you found this repository helpful, please consider giving it a star 🌟. It's free, and it will motivate me to create more such content.