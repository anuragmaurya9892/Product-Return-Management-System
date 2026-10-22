# ============================================================
# Product Return Management System - Dockerfile for Render.com
# Multi-stage build: Ant Builder -> Tomcat 10.1 Runtime
# ============================================================

# Stage 1: Build the WAR with Ant & Java 17
FROM eclipse-temurin:17-jdk AS builder

# Install Apache Ant
RUN apt-get update && apt-get install -y --no-install-recommends ant && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy repository source code
COPY . .

# Compile and package WAR file
RUN ant war

# Stage 2: Runtime image with Apache Tomcat 10.1
FROM tomcat:10.1-jdk17-temurin

# Clean out default Tomcat sample webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Deploy productreturn.war as ROOT.war so it serves directly at https://<your-app>.onrender.com/
COPY --from=builder /app/build/dist/productreturn.war /usr/local/tomcat/webapps/ROOT.war

# Create uploads directory for customer returns evidence
RUN mkdir -p /usr/local/tomcat/uploads && chmod 777 /usr/local/tomcat/uploads

EXPOSE 8080

CMD ["catalina.sh", "run"]
