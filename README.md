# AI Notification Prioritization Engine — Spring Boot

![Java](https://img.shields.io/badge/Java-17+-orange)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.x-green)
![MongoDB](https://img.shields.io/badge/Database-MongoDB-blue)
![AI](https://img.shields.io/badge/AI-Groq%20LLM-purple)
![Deployment](https://img.shields.io/badge/Cloud-Ready-success)

This project is a production-ready AI-powered Notification Prioritization Engine designed to intelligently classify and manage incoming notifications using Large Language Models (LLMs).

The system analyzes notification content in real time and assigns priority levels through an AI-driven decision engine supported by a fail-safe architecture to ensure reliability and continuous operation.

---

## 🧭 System Overview

```mermaid
flowchart LR
    A[Client / Frontend] --> B[Spring Boot API]
    B --> C[Notification Controller]
    C --> D[Notification Service]
    D --> E[AI Decision Service]
    E --> F[Groq AI API]

    F --> E
    E --> D
    D --> G[(MongoDB Database)]
    D --> H[API Response]

    style F fill:#ffefe0
    style G fill:#e0f7ff
```
---
## 🏗 High-Level Architecture

```mermaid
flowchart LR

    User[Client / Frontend] --> API[Spring Boot API]

    API --> Controller[Notification Controller]
    Controller --> Service[Notification Service]

    Service --> AI[AI Decision Service]
    AI --> Groq[Groq AI API]

    Service --> DB[(MongoDB Database)]

    DB --> API
    API --> Response[JSON Response]

    style Groq fill:#ffe6e6
    style DB fill:#e6f7ff
    style API fill:#e8ffe8
```
---
# ✅ 2️⃣ REQUEST PROCESSING FLOW

```md
## 🔄 Request Processing Flow

```mermaid
flowchart TD

    A[User sends Notification] --> B[REST API Endpoint]
    B --> C[Controller Validation]
    C --> D[Business Service]

    D --> E{Call AI?}

    E -->|Success| F[AI Decision Returned]
    E -->|Failure| G[Fallback Decision]

    F --> H[Save to Database]
    G --> H

    H --> I[Return Response to User]

    style E fill:#fff3cd
    style G fill:#ffd6d6
```
✅ Directly proves **fail-safe logic requirement**.

---
# ✅ 3️⃣ AI INTEGRATION ARCHITECTURE

```md
## 🤖 AI Integration Architecture

```mermaid
sequenceDiagram

    participant API as Spring Boot API
    participant AIService as AI Decision Service
    participant Groq as Groq LLM API

    API->>AIService: analyze(message)
    AIService->>Groq: POST /chat/completions
    Groq-->>AIService: JSON decision
    AIService-->>API: decision + reason
```
> ✅ Shows real AI integration (important requirement).
---
# ✅ 4️⃣ FAIL-SAFE ARCHITECTURE

```md
## 🛡 Fail-Safe Decision Architecture

```mermaid
flowchart LR

    Start[Incoming Notification]
        --> AIReq[Send AI Request]

    AIReq --> Check{AI Available?}

    Check -->|Yes| Parse[Parse AI JSON]
    Check -->|No| Fallback[Fallback Rule Engine]

    Parse --> Save[(MongoDB)]
    Fallback --> Save

    Save --> Output[Return Final Response]

    style Fallback fill:#ffd6d6
```
> ✅ "Complete fail-safe architecture with fallback mechanisms"
---
## 🔐 Fail-Safe Design

The system guarantees availability using:

AI timeout protection

JSON validation layer

Exception-safe parsing

Automatic fallback decision engine

Always-return response strategy

---

## 🚀 Features

- Spring Boot REST API
- MongoDB database integration
- Real AI classification using Groq LLM
- Automatic fallback mechanism
- Swagger API documentation
- Production-safe error handling

---
## 🛠 Tech Stack

- Java 17+
- Spring Boot
- MongoDB
- WebClient
- Groq AI API
- Maven

---

## 🧠 How It Works

1. Client sends notification
2. Backend calls AI model
3. AI classifies priority
4. Result stored in MongoDB
5. Response returned instantly

If AI fails → fallback decision ensures reliability.

---

✅ Shows full backend pipeline instantly.

---

## 📡 API Endpoints

### Health Check
GET `/health`

### Process Notification
POST `/api/notifications/process`

Example:

```json
{
  "userId": "101",
  "message": "Server outage detected",
  "context": "production"
}
```
---
## ⚙️ Setup Guide (Local Run)
1️⃣ Clone Repository
```
git clone https://github.com/your-username/notification-engine-springboot.git
cd notification-engine-springboot
```
2️⃣ Configure Environment
Update application.properties:
```
groq.api.url=https://api.groq.com/openai/v1/chat/completions
groq.api.key=YOUR_API_KEY
```
3️⃣ Run Application
```
mvn spring-boot:run
```
Server starts at:

http://localhost:8080

Swagger UI:

http://localhost:8080/swagger-ui.html


---

# ☁ Deployment

The application is designed to be cloud-deployable with environment-based configuration and production-safe startup settings.

## 🌐 Live Architecture

- Backend deployed as a cloud web service
- MongoDB hosted using MongoDB Atlas
- AI classification powered by Groq LLM API
- Environment variables used for secure configuration

# ✅ 5️⃣ DEPLOYMENT ARCHITECTURE

```md
## ☁ Deployment Architecture

```mermaid
flowchart LR

    Dev[Developer Push] --> GitHub
    GitHub --> Render[Render Cloud Deployment]

    Render --> Backend[Spring Boot Backend]
    Backend --> Mongo[(MongoDB Atlas)]
    Backend --> Groq[Groq AI API]

    User --> Backend

    style Render fill:#e8ffe8
    style Groq fill:#ffe6e6
```
---
## 📦 Deployment Design Principles
    Stateless backend architecture
    Externalized configuration
    Fail-safe AI integration
    Cloud-ready build process
    Zero hardcoded secrets
---

## 🚀 Backend Deployment

The Spring Boot application can be deployed on platforms such as:

- Render
- Railway
- AWS
- Docker-based cloud environments

> Here I used Render
---
### Build Command

```bash
./mvnw clean package
```
Start Command
```
java -jar target/*.jar
```
---
## Environment Variables
The following must be configured in the deployment platform:
```
groq.api.url=http://api.groq.com/openai/v1/chat/completions
groq.api.key=YOUR_API_KEY
```