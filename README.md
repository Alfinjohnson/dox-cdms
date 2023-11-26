# Dox CDMS Documentation

## Table of Contents
- [Introduction](#introduction)
    - [What Are We Trying to Solve?](#what-are-we-trying-to-solve)
    - [Problem Statement](#problem-statement)
    - [Tech Stack](#tech-stack)
    - [Key Features](#key-features)
- [Getting Started](#getting-started)
    - [Installation](#installation)
        - [Docker Compose](#docker-compose)
        - [Docker Image](#docker-image)
    - [Configuration](#configuration)
- [Usage](#usage)
    - [REST API Usage](#rest-api-usage)
    - [Example: Serving Different Data Types](#example-serving-different-data-types)
    - [Example: Test API Endpoint](#example-test-api-endpoint)
- [Contributing](#contributing)
- [License](#license)
- [Acknowledgments](#acknowledgments)

## Introduction
In the dynamic landscape of Software as a Service (SaaS), managing configurations across servers can be complex. Dox CDMS is a Configuration Management System designed to simplify this process, acting as a bridge between servers and ensuring each receives the precise configuration it needs.

### What Are We Trying to Solve?
In the realm of SaaS, a common challenge arises when multiple servers demand different configurations. This diversity can lead to a chaotic scenario where developers and system administrators grapple with the complexities of ensuring each server has the right settings. Dox CDMS aims to solve this problem by providing a centralized solution for configuring applications. It acts as a translating layer, harmonizing configurations across servers and eliminating the headaches associated with managing diverse settings.

### Problem Statement
Dox CDMS addresses the challenge of diverse configurations across servers. By providing a centralized system for configuring applications, it acts as a translating layer, preventing configuration-related headaches for developers and system administrators.

### Tech Stack
Dox CDMS leverages a robust tech stack:
- Java Spring Boot
- Postgres
- REST API

### Key Features
- Docker Support: Seamless integration with Docker for streamlined deployment.
- REST API: Intuitive API for effective communication and configuration.
- Health Monitoring: Proactive issue resolution through application health monitoring.
- Kafka Support: Efficient and real-time configuration updates via Kafka integration.
- Datadog Support: System performance monitoring and analysis with Datadog.
- SSL Support.

## Getting Started
### Installation
#### Docker Compose
Clone the Dox CDMS repository:
```bash
git clone https://github.com/Alfinjohnson/dox-cdms.git
```

#### Docker Image
Pull the Dox CDMS Docker image from the official repository:
```bash
docker pull ajcode/dox-cdms:latest
```

### Configuration
Access the Dox CDMS API at http://localhost:8080 or your specified port.

## Usage
### REST API Usage
...

### Example: Serving Different Data Types
Consider a scenario where Dox CDMS serves different data types for various servers. For instance:
- Server 1 requires JSON data.
- Server 2 needs integer and long values.
- Server 3 prefers float and double values.
- Server 4 uses string data.
  Dox CDMS handles these diverse data types, ensuring each server receives the appropriate configurations seamlessly.

### Example: Test API Endpoint
To test the Dox CDMS API endpoint using `curl`, you can use the following command:
```bash
curl --location 'https://localhost:8443/api/v1/configuration/test'
```

In this example:
- The URL is `https://localhost:8443/api/v1/configuration/test`.
- The request type is `GET`.

This command is designed to test the API endpoint and receive a response. Adjust the URL as needed for your specific testing requirements.

## Contributing
We welcome contributions! If you'd like to contribute to Dox CDMS, please follow our contribution guidelines.

## License
Dox CDMS is licensed under the Apache License 2.0. See the LICENSE file for the full license text.

## Acknowledgments
A big thank you to our contributors and the community for their continuous support.