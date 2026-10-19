# Stage 1: Build the application with Maven and Java 21
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Run the test suite before packaging the production artifact.
RUN mvn clean verify

# Stage 2: Run the application with a lightweight Java 21 JRE
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Render assigns the PORT environment variable dynamically at runtime.
# application.properties reads it via: server.port=${PORT:8080}
# Do NOT hardcode PORT here — it would override Render's assignment.
EXPOSE 8080

# application.properties resolves the deployment-provided PORT.
ENTRYPOINT ["java", "-jar", "app.jar"]
