FROM gradle:8.5.0-jdk17-jammy AS builder
#docker pull
WORKDIR /app

# Copy the source code to the builder image
COPY . .

# Build the Spring Boot application
RUN gradle clean assemble

# Stage 2: Runner Stage
FROM openjdk:22-ea-17-slim-bookworm

WORKDIR /app

# Copy the built artifacts from the builder image to the runner image
COPY --from=builder /app/build/libs/webapp-0.0.1-SNAPSHOT.jar ./app.jar
COPY ./users.csv users.csv
# Expose the port your Spring Boot app runs on (adjust as needed)
EXPOSE 8080

# Command to run the Spring Boot application
CMD ["java", "-jar", "app.jar"]
