# Contributing to Product Return Management System

Thank you for your interest in contributing to the Product Return Management System.

## Project Guidelines

1. **Technology Stack**:
   - Java 17+
   - Apache Tomcat 10.1 (Jakarta Servlet API 6.0)
   - MySQL 8.0+
   - Apache Ant (No Maven or Gradle)
   - Plain HTML5, modern CSS3, and JavaScript

2. **Coding Standards**:
   - Use `jakarta.servlet.*` imports exclusively (Tomcat 10.1 requirement).
   - Use JDBC `PreparedStatement` for all database interactions to prevent SQL injection.
   - Escape all dynamic JSP outputs with `<c:out value="${...}"/>` to avoid XSS vulnerabilities.
   - Maintain the MVC architecture: Servlets handle HTTP control flow, DAOs manage data persistence, Models encapsulate domain entities, and JSPs render views.
   - Never commit sensitive configuration files like `db.properties` or database credentials.

3. **Return Business Rules Enforcement**:
   - The 7-day return window from delivery date must be verified server-side.
   - Only delivered orders are eligible for return.
   - Only one active (non-rejected) return request is allowed per order item.
   - Return quantity must never exceed purchased quantity minus already-returned quantities.
   - Status transitions must follow the authorized RMA lifecycle.

4. **Running Automated Tests**:
   Before committing, always ensure that all automated unit and business logic tests pass:
   ```bash
   ant test
   ```

5. **Building the WAR File**:
   ```bash
   ant war
   ```
