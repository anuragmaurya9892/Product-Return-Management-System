# Database Setup Guide

## Prerequisites
- MySQL 8.0+ installed and running
- MySQL root access (or a user with CREATE DATABASE privileges)

## Step 1: Create the Database and Tables

Open a terminal/command prompt and run:

```bash
mysql -u root -p < database/schema.sql
```

This creates the `product_return_db` database and all required tables.

## Step 2: Load Demo Data

```bash
mysql -u root -p < database/seed.sql
```

This populates the database with:
- 3 demo customers
- 13 products across 4 categories
- 7 orders with order items
- 3 sample return requests at different stages
- Status history records

## Step 3: Create the Admin Account

The admin account is **not** created with a hardcoded password in the seed script. Instead, use the application's built-in setup utility.

### Option A: Use the Setup Utility (Recommended)

After deploying the application, run:

```bash
java -cp "web/WEB-INF/lib/*;build/classes" com.productreturn.util.AdminSetup
```

Follow the prompts to set the admin username and password.

### Option B: Manual SQL (Local Development Only)

1. Generate a BCrypt hash of your desired password using an online tool (e.g., https://bcrypt-generator.com/) with cost factor 12.

2. Run:
```sql
USE product_return_db;
CALL create_admin_user('admin', 'admin@company.com', '<paste_bcrypt_hash_here>', 'System Administrator');
```

> **WARNING:** Never commit a real password hash to version control. This method is for local development only.

## Step 4: Configure the Application

1. Copy `db.properties.example` to `db.properties` in the project root:
   ```
   copy db.properties.example db.properties
   ```

2. Edit `db.properties` with your MySQL credentials:
   ```properties
   db.url=jdbc:mysql://localhost:3306/product_return_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   db.username=root
   db.password=YOUR_MYSQL_PASSWORD
   ```

3. The `db.properties` file is gitignored and will not be committed.

## Demo Credentials (Local Development Only)

| Role | Username | Password |
|------|----------|----------|
| Customer | john_doe | customer123 |
| Customer | jane_smith | customer123 |
| Customer | raj_patel | customer123 |
| Admin | (create via Step 3) | (your chosen password) |

## Schema Overview

| Table | Description |
|-------|-------------|
| `users` | Customer and admin accounts |
| `products` | Product catalogue |
| `orders` | Customer orders |
| `order_items` | Items within each order |
| `return_requests` | Product return requests |
| `return_status_history` | Audit trail for status changes |
| `refunds` | Refund records for return requests |

## Resetting the Database

To drop and recreate everything:
```sql
DROP DATABASE IF EXISTS product_return_db;
```
Then re-run Steps 1-3.
