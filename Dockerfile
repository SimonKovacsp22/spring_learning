# Use an official OpenJDK runtime as the base image
FROM adoptopenjdk:latest

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/learning_online-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the application will run on
EXPOSE 8080

# Set the entrypoint command to run the application
CMD ["java", "-jar", "app.jar"]
