# Stage 1: build
# Start with a Maven image that includes JDK 17
FROM maven:3.9.9-amazoncorretto-17 AS build

# Declare working dir in container
WORKDIR /app

# Copy built file JAR to container
COPY target/identity-service-0.0.1-SNAPSHOT.jar app.jar

# Set port for applicaion
EXPOSE 8181

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]