# Product Return Management System

A full-stack Java web application for managing product returns in an ecommerce business. Customers can request returns, select refunds or replacements, and track their request status. Administrators can manage and process return requests through a defined workflow.

## Features

### Customer Features
- Secure registration and login with password hashing (BCrypt)
- Dashboard with order and return summary
- Browse product catalogue with search and category filtering
- View order history and order details
- Submit product return requests with reason, resolution type, and optional evidence image
- Unique return reference number for each request
- Track return status through the workflow
- Profile management

### Admin Features
- Role-based admin access
- Dashboard with key metrics (orders, pending/approved/rejected returns, processed refunds)
- View, search, and filter all return requests
- Approve or reject requests with admin notes
- Full return workflow management:
  - **Pending** → **Approved** → **Item Received** → **Refund Processed** / **Replacement Shipped**
  - **Pending** → **Rejected** (terminal)
- Product management
- View customers and orders
- Basic reports and statistics

## Technology Stack

| Technology | Purpose |
|---|---|
| Java JDK 17+ | Backend language |
| Apache Tomcat 10.1 | Application server |
| Jakarta Servlet / JSP | Web framework |
| JDBC | Database connectivity |
| MySQL 8.0 | Relational database |
| BCrypt (jBCrypt) | Password hashing |
| JSTL | JSP tag library |
| HTML5 / CSS3 / JavaScript | Frontend |
| Apache Ant | Build tool |

## Business Rules

1. **Return Eligibility**: Returns can only be requested for **delivered** orders within **7 days** of the delivery date.
2. **No Duplicates**: Only one active (non-rejected) return request is allowed per order item.
3. **Quantity Validation**: Return quantity must not exceed the purchased quantity minus any already-returned quantity.
4. **Status Workflow**:
   - Refund path: Pending → Approved → Item Received → Refund Processed
   - Replacement path: Pending → Approved → Item Received → Replacement Shipped
   - Rejection: Pending → Rejected (terminal state)
5. **Refund Recording**: When a refund return is approved, a refund record is created with "PENDING" status. When the return reaches "Refund Processed", the refund record is updated to "PROCESSED". The system records the refund — it does **not** transfer actual funds.
6. **Data Isolation**: Customers can only view their own orders, returns, and profile.
7. **Role Security**: Registration always creates a CUSTOMER account. Admin accounts are created only through the secure setup utility.
8. **Monetary Values**: All prices and amounts use `DECIMAL(10,2)` in the database and `BigDecimal` in Java.

## Prerequisites

- **Java JDK 17** or higher ([Download](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html))
- **Apache Tomcat 10.1** ([Download](https://tomcat.apache.org/download-10.cgi))
- **MySQL 8.0** or higher ([Download](https://dev.mysql.com/downloads/mysql/))
- **Apache Ant 1.10+** ([Download](https://ant.apache.org/bindownload.cgi))
- **Git** ([Download](https://git-scm.com/downloads))

## Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/Product-Return-Management-System.git
cd Product-Return-Management-System
```

### 2. Set Environment Variables

Set the following system environment variables:

| Variable | Value (example) |
|---|---|
| `JAVA_HOME` | `C:\Program Files\Java\jdk-17` |
| `CATALINA_HOME` | `C:\apache-tomcat-10.1.x` |
| `ANT_HOME` | `C:\apache-ant-1.10.x` |

Add `%ANT_HOME%\bin` to your system `PATH`.

### 3. Create the Database

```bash
mysql -u root -p < database/schema.sql
mysql -u root -p < database/seed.sql
```

### 4. Create the Admin Account

```bash
# After building (step 6), run:
java -cp "web/WEB-INF/lib/*;build/classes" com.productreturn.util.AdminSetup
```

Or manually via SQL — see [database/README.md](database/README.md) for details.

### 5. Configure Database Connection

```bash
copy db.properties.example db.properties
```

Edit `db.properties` with your MySQL credentials:

```properties
db.url=jdbc:mysql://localhost:3306/product_return_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.username=root
db.password=YOUR_PASSWORD
```

### 6. Build the Project

```bash
ant clean build
```

This compiles the Java code and creates `build/dist/productreturn.war`.

### 7. Deploy to Tomcat

**Option A: Automatic**
```bash
ant deploy
```

**Option B: Manual**
Copy `build/dist/productreturn.war` to `%CATALINA_HOME%/webapps/`.

### 8. Start Tomcat

```bash
%CATALINA_HOME%\bin\startup.bat
```

### 9. Access the Application

Open your browser and navigate to:

```
http://localhost:8080/productreturn/
```

## Demo Accounts (Local Development Only)

| Role | Username | Password |
|---|---|---|
| Customer | john_doe | customer123 |
| Customer | jane_smith | customer123 |
| Customer | raj_patel | customer123 |
| Admin | admin | *(set during Step 4)* |

> **Warning:** These demo accounts are for local development and testing only. Never use default credentials in production.

## Project Structure

```
Product-Return-Management-System/
├── src/com/productreturn/     # Java source code
│   ├── model/                 # Entity classes
│   ├── dao/                   # Data access objects
│   ├── servlet/               # Servlet controllers
│   │   ├── customer/          # Customer servlets
│   │   └── admin/             # Admin servlets
│   ├── filter/                # Auth, Admin, CSRF filters
│   └── util/                  # Utilities (DB, Security, Validation)
├── web/                       # Web content
│   ├── WEB-INF/               # web.xml, lib/
│   ├── css/                   # Stylesheets
│   ├── js/                    # JavaScript
│   ├── customer/              # Customer JSP pages
│   ├── admin/                 # Admin JSP pages
│   └── includes/              # Shared JSP fragments
├── database/                  # SQL scripts
├── test/                      # Test classes
├── lib/                       # Compile-time dependencies
├── build.xml                  # Ant build file
└── db.properties.example      # Database config template
```

## Security Features

- **Password Hashing**: BCrypt with cost factor 12
- **SQL Injection Prevention**: PreparedStatements for all queries
- **XSS Prevention**: JSTL `<c:out>` for all dynamic output
- **CSRF Protection**: Token-per-session validated on all POST requests
- **Authentication**: Session-based with filter enforcement
- **Authorization**: Role-based access control (Customer vs Admin)
- **File Upload Safety**: Type whitelist (JPG/PNG only), 5MB size limit
- **Session Security**: HttpOnly cookies, invalidation on logout
- **Input Validation**: Server-side validation on all inputs

## Troubleshooting

| Problem | Solution |
|---|---|
| `ant` not recognized | Ensure `ANT_HOME` is set and `%ANT_HOME%\bin` is in PATH. Open a **new** terminal after setting env vars. |
| Compilation errors | Check that `lib/` contains `servlet-api.jar`, `jsp-api.jar`, `el-api.jar` from Tomcat. |
| MySQL connection refused | Verify MySQL is running: `net start MySQL80`. Check credentials in `db.properties`. |
| 404 after deploy | Ensure the WAR is in `%CATALINA_HOME%/webapps/` and Tomcat is running. Check `catalina.out` logs. |
| JSTL errors | Verify `jakarta.servlet.jsp.jstl-3.0.1.jar` and `jakarta.servlet.jsp.jstl-api-3.0.0.jar` are in `web/WEB-INF/lib/`. |
| File upload fails | Ensure `uploads/` directory exists and is writable. Check Tomcat's temp directory permissions. |
| ClassNotFoundException | Run `ant clean build` to recompile. Verify all JARs are in the correct locations. |

## License

This project is a college project and is provided as-is for educational purposes.
