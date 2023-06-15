﻿# Use an official OpenJDK runtime as the base image
FROM adoptopenjdk:11-jdk-hotspot

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/learning_online.jar app.jar

# Expose the port the application will run on
EXPOSE 8080

# Set the entrypoint command to run the application
CMD ["java", "-jar", "app.jar"]