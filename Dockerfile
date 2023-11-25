# Use an official Eclipse Temurin runtime as a parent image
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Create a temporary directory for building
RUN mkdir /tmpbuild
WORKDIR /tmpbuild

# Install Maven
RUN apk add --no-cache maven

# Copy only the necessary files for building the JAR
COPY pom.xml .
COPY src ./src

# Build the Java application using Maven in the temporary directory
RUN mvn clean install -Dmaven.test.skip=true

# Move the JAR file to the app directory
RUN mv target/*.jar /app/cdms.jar

# Clean up: Delete the temporary build directory and its content
WORKDIR /
RUN rm -rf /tmpbuild

# Expose the SSL port
EXPOSE 8443

# Define the command to run your application with SSL
CMD ["java", "-jar", "/app/cdms.jar", "--spring.profiles.active=docker", "--spring.main.allow-bean-definition-overriding=true"]
