# EcomStore — E-Commerce Backend API

A Spring Boot REST API for an e-commerce store with admin-managed catalog, guest checkout, JWT authentication, and email notifications.

## Tech Stack

- **Java 21**, **Spring Boot 4.1.0**
- **Spring Data JPA** (Hibernate) + **MySQL**
- **Spring Security** with **JWT** authentication
- **Spring Mail** (Gmail SMTP) for email notifications
- **Spring Async** (`@Async`) for non-blocking email sends
- **Lombok**, **Jakarta Validation**
- **Swagger / OpenAPI** for API documentation

## Features

### Catalog Management (Admin)
- Full CRUD on Categories and Products
- Soft delete (`active` flag) — deactivating a category cascades to deactivate its products; reactivating restores them
- Product stock (`quantityInStock`) auto-syncs `ProductStatus` (`IN_STOCK` / `OUT_OF_STOCK`)

### Customer-Facing Storefront
- Browse/search products by name, price range, category
- Sort by price (asc/desc) or newest
- Guest checkout — no account required; customer details captured at checkout only
- Server-side price calculation (never trusts client-submitted prices)
- Order tracking via order number + email (no login required)
- Order cancellation (customer or admin) with automatic stock restoration

### Order Management (Admin)
- View all orders / look up by ID or order number
- Manually update order status (`PENDING → PROCESSING → SHIPPED → DELIVERED`)
- **Automatic status progression** — a scheduled job advances orders through the fulfillment pipeline over time (configurable interval), simulating a COD fulfillment flow
- Finalized orders (`DELIVERED`, `CANCELLED`) cannot be modified further

### Authentication & Authorization
- JWT-based stateless authentication
- Passwords hashed with BCrypt
- Role-based access control (`@PreAuthorize`) — admin-only actions are enforced at the method level

### Notifications
- Email sent to admin on: new order placed, category/product created/updated/deleted/reactivated, new contact form submission, order cancelled
- Email sent to customer on: order confirmation, order status change, order cancellation
- All emails sent asynchronously (non-blocking)

### Admin Dashboard
- Revenue (this month), orders (this week), low-stock count, total products/categories
- Top-selling products, recent orders — single aggregated endpoint

### Contact Us
- Public submission endpoint; admin can view and mark messages resolved

## Architecture

Layered architecture: `Controller → Service → Repository → Database`

```
src/main/java/com/example/EcomStore/
├── Config/         → Security & password encoder configuration
├── Controller/     → REST endpoints (HTTP layer only)
├── Dto/            → Request/response shapes, decoupled from entities
├── Entities/       → JPA entities
├── Exception/      → Custom exceptions + global exception handler
├── Repository/     → Spring Data JPA repositories
├── Security/       → JWT generation, validation, and request filter
└── Service/        → Business logic
```

DTOs are used wherever the client shouldn't control certain fields directly (e.g. checkout — price/total are server-calculated; registration — role is hardcoded, not client-supplied).

## Setup

### Prerequisites
- Java 21
- MySQL 8+
- A Gmail account with an [App Password](https://myaccount.google.com/apppasswords) generated (requires 2-Step Verification enabled)

### Configuration

Create `src/main/resources/application.properties` (not committed to version control):

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/EcomStore?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=your_db_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=your_jwt_signing_secret_at_least_32_chars
jwt.expiration=86400000

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your_16_char_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

admin.notification.email=your-email@gmail.com
```

### Run

```bash
mvn spring-boot:run
```

API available at `http://localhost:8080`. Swagger UI at `http://localhost:8080/swagger-ui/index.html`.

### First-time setup

1. Register the first admin: `POST /api/admins/register`
2. Log in: `POST /api/auth/login` → returns a JWT
3. Use the JWT as a `Bearer` token for all admin-only endpoints

## API Overview

| Area | Endpoint | Access |
|---|---|---|
| Auth | `POST /api/auth/login` | Public |
| Admin | `POST /api/admins/register` | Public |
| Admin | `GET /api/admins` | Admin |
| Categories | `GET /api/categories`, `GET /api/categories/{id}` | Public |
| Categories | `POST/PUT/DELETE/PATCH /api/categories/...` | Admin |
| Products | `GET /api/products`, `/search`, `/category/{id}` | Public |
| Products | `POST/PUT/DELETE/PATCH /api/products/...` | Admin |
| Orders | `POST /api/orders/checkout` | Public |
| Orders | `GET /api/orders/track`, `PATCH /api/orders/cancel` | Public (order number + email required) |
| Orders | `GET /api/orders`, `PATCH /api/orders/{id}/status`, `PATCH /api/orders/{id}/cancel` | Admin |
| Contact | `POST /api/contact` | Public |
| Contact | `GET /api/contact`, `PATCH /api/contact/{id}/resolve` | Admin |
| Dashboard | `GET /api/dashboard` | Admin |

Full request/response schemas available via Swagger UI.

## Known Limitations / Scope Decisions

- **Cash on Delivery only** — no payment gateway integration, no `payment_status` tracking
- **Guest checkout only** — no customer accounts or login; order history is accessed via order number + email
- **Single admin role** — no tiered permissions (e.g. super-admin vs. staff)
- **Automatic order progression** is a simulated fulfillment timeline (time-based), not driven by real courier/warehouse events — a deliberate simplification given the COD-only, no-integration scope
- **Best-selling products filter** (customer-facing) was scoped out due to time constraints; the dashboard's top-products aggregation demonstrates the same underlying query pattern

## Security Notes

- `application.properties` is excluded via `.gitignore` — never commit real credentials
- JWT secret and Gmail app password must be kept out of version control
- Passwords are never stored or returned in plaintext (BCrypt hash only; `Admin` API responses use a DTO that excludes the password field)