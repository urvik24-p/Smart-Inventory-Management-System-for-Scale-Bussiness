# Stage 1: Build the application with Maven and Java 21
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Package the application (skip tests for faster cloud build)
RUN mvn clean package -DskipTests

# Stage 2: Run the application with a lightweight Java 21 JRE
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Render assigns a dynamic port via the PORT environment variable
ENV PORT=8081
EXPOSE 8081

# Launch Spring Boot
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]
