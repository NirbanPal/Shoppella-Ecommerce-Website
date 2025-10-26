# Use Maven to build the app
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src

# Build the JAR (skip tests for faster build)
RUN mvn clean package -DskipTests

# Use lightweight JDK for running
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy the built jar
COPY --from=build /app/target/*.jar app.jar

# Expose the app port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
