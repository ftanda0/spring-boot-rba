# CardApp

Welcome, travelers! 🎉

**CardApp** is a full-stack application designed for managing card requests. This application leverages Java Spring Boot for backend (BE), where most of the business logic resides, and React for frontend (FE), which handles data representation and user interactions.

## Table of Contents

- [Project Structure](#project-structure)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Running the Application](#running-the-application)
  - [Backend](#backend)
  - [Frontend](#frontend)
- [Deployment](#deployment)
- [Security](#security)
- [Kafka Topics](#kafka-topics)
  - [Simulator](#simulator)
- [Configuration](#configuration)
- [License](#license)

## Project Structure

The project is organized into several packages to maintain a clean and manageable codebase:

```plaintext
rba.it.CardApp
│
├── configuration
├── controller
├── dto
├── exception
├── model
├── repository
├── service
└── simulator
```

Additionally, the project includes separate directories for unit tests to ensure code reliability and maintainability.

## Technologies Used

- **Backend:** Java Spring Boot
- **Frontend:** React
- **Database:** PostgreSQL (hosted on AWS RDS)
- **Messaging:** Apache Kafka (hosted on AWS MSK)
- **Containerization:** Docker (optional, for local development)
- **Security:** OAuth 2.0
- **Deployment:** AWS EC2 (Linux and Windows instances)



## Prerequisites

Before setting up the project locally or deploying it, ensure you have the following installed:

- Java 17 or higher
- Maven
- Node.js and npm
- Docker (optional, for local development)

## Installation

### Backend

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/ftanda0/spring-boot-rba.git
   cd CardApp
   ```

2. **Build the Application:**

   Using Maven:

   ```bash
   mvn clean package
   ```

### Frontend

1. **Navigate to Frontend Directory:**

   ```bash
   cd path/to/your/react-app
   ```

2. **Install Dependencies:**

   ```bash
   npm install
   ```

3. **Build the Application:**

   ```bash
   npm run build
   ```

## Running the Application

1. Open your browser and navigate to [http://localhost:3000](http://localhost:3000).

## Deployment

- **Backend:** Deployed on an AWS EC2 Linux instance.
- **Frontend:** Deployed on an AWS EC2 Windows instance.
- **Kafka:** Deployed on an AWS MSK cluster.

## Security

For simplicity, this project uses OAuth 2.0 for securing backend services. In a production environment, I would recommended to use more robust security mechanisms, such as AWS Vault or similar secrets management services.

### Frontend Security

The frontend application includes a mock login system. In a real-world scenario, proper authentication flows should be implemented, potentially leveraging services like Auth0. Currently, the login is simulated within the frontend, and the token is used to access protected resources.

### Backend Security

The backend is secured using OAuth 2.0. All API endpoints require a valid JWT token, except for the `/api/v1/get-token` endpoint, which is publicly accessible for obtaining tokens. In a real-world environment, a dedicated security microservice should handle authentication and authorization.

## Kafka Topics

Two Kafka topics are created for managing card statuses:

- **card_status**
  - **Purpose:** Receives messages for card creation requests.
  - **Usage:** Each card creation request is sent as a message to this topic.
  - **Consumer:** In a real environment, a consumer would listen to this topic to process card creation.

- **card_status_updates**
  - **Purpose:** Receives updates on the status of card requests.
  - **Usage:** After processing a card request (approval or rejection), a message is sent to this topic.
  - **Consumer:** The backend application subscribes to this topic to update the card status in the database accordingly.

### Simulator

A simulator is included to mimic the behavior of the card processing system. You can send status updates by accessing the following endpoint:

```
{{url}}/simulate/status-update?oib=54545182222&status=REJECTED
```

This will send a message to the `card_status_updates` topic, allowing you to test the application's response to status changes.

## Configuration

> Note: In real-world applications, sensitive information such as database credentials and OAuth secrets would be managed using secure methods like AWS Secrets Manager or Vault.
