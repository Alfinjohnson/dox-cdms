# Use an official Eclipse Temurin runtime as a parent image
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app


# Expose the SSL port
EXPOSE 8443
# Define the command to run your application with SSL
CMD ["java", "-jar", "/app/cdms.jar", "--spring.profiles.active=docker", "--spring.main.allow-bean-definition-overriding=true"]
