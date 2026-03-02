# Use Java 17
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# ✅ Give permission to mvnw
RUN chmod +x mvnw

# Build application
RUN ./mvnw clean package -DskipTests

# Expose Render port
EXPOSE 8080

# Run Spring Boot app
CMD ["sh", "-c", "java -jar target/*.jar"]