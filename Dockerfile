# Use an official Eclipse Temurin runtime as a parent image
FROM eclipse-temurin:17-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file into the container
COPY target/cdms-0.0.1.jar /app/cdms.jar

# Copy your SSL certificate and private key into the container
COPY data/ssl/keystore.p12 /app/data/ssl/keystore.p12

# Set the active profile
ENV SPRING_PROFILES_ACTIVE=docker

# Expose the SSL port
EXPOSE 8443

# Define the command to run your application with SSL
CMD ["java", "-jar", "/app/cdms.jar", "--server.port=8443", "--server.ssl.key-store=/app/data/ssl/keystore.p12", "--server.ssl.key-store-password=rootcdms", "--server.ssl.key-store-type=PKCS12", "--server.ssl.key-alias=cdms"]
