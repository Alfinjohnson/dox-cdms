
[![Pull Requests](https://img.shields.io/github/issues-pr/Alfinjohnson/dox-cdms.svg)](https://github.com/Alfinjohnson/dox-cdms/pulls) [![Stars](https://img.shields.io/github/stars/Alfinjohnson/dox-cdms.svg)](https://github.com/Alfinjohnson/dox-cdms/stargazers)
# Dox CDMS Documentation

## Table of Contents
- [Introduction](#introduction)
  - [What Are We Trying to Solve?](#what-are-we-trying-to-solve)
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
  - [Example: Get Configuration API](#example-get-configuration-api)
- [Contributing](#contributing)
  - [Contribution Guidelines](#contribution-guidelines)
- [Change Log](#change-log)
- [Known Issues](#known-issues)
- [New Contributors](#new-contributors)
- [License](#license)
- [Acknowledgments](#acknowledgments)

## Introduction
In the dynamic landscape of Software as a Service (SaaS), managing configurations across multiple servers often leads to a tangled web of complexities. Imagine scenarios where each server demands a different configuration - one server prefers English language code as "eng," while another insists on "en-us." This diversity can cause headaches for developers and system administrators. Dox CDMS is a Configuration Management System designed to simplify this process, acting as a bridge between servers and ensuring each receives the precise configuration it needs.

![cmd-arch](https://github.com/Alfinjohnson/dox-cdms/assets/56215309/19088437-1432-44eb-ad3c-24a45db04bd3)

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

### Example: Get Configuration API
To retrieve a configuration using the Dox CDMS API, you can use the following `curl` command:
```bash
curl --location --request GET 'https://localhost:8443/api/v1/configuration' \
--header 'Content-Type: application/json' \
--data '{
    "name": "englishlangcode",
    "subscriber": "thridparty-server1"
}'
```

Example Response:
```json
{
    "statusCode": 200,
    "message": "Success",
    "timestamp": "2023-11-26 13:18:27",
    "data": {
        "name": "englishlangcode",
        "subscriber": "thridparty-server1",
        "dataType": "string",
        "value": "eng"
    }
}
```

Refer to the latest release documents for the full Postman API collection.

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
We welcome contributions! If you'd like to contribute to Dox CDMS, please follow our [contribution guidelines](CONTRIBUTING.md).

### Change Log
Explore the project's history and releases in the [Change Log](https://github.com/Alfinjohnson/dox-cdms/commits/pre).

### Known Issues
Highlight any known issues or limitations present in this release.

### New Contributors
* @Alfinjohnson made their first contribution in [Pull Request #1](https://github.com/Alfinjohnson/dox-cdms/pull/1).

## License
Dox CDMS is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for the full license text.

## Acknowledgments
A big thank you to our contributors and the community for their continuous support.
