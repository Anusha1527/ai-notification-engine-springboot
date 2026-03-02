# Use Java 17
FROM eclipse-temurin:17-jdk

# Working directory
WORKDIR /app

# Copy files
COPY . .

# Give permission to Maven wrapper
RUN chmod +x mvnw

# Build project
RUN ./mvnw clean package -DskipTests

# Render provides PORT automatically
ENV PORT=10000

# Expose dynamic port
EXPOSE 10000

# Run application
CMD ["sh", "-c", "java -jar target/*.jar --server.port=$PORT"]