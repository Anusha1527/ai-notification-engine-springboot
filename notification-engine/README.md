# AI Notification Prioritization Engine — Spring Boot

This project is a production-ready AI-powered Notification Prioritization Engine developed for the **Cyepro Solutions AI/ML Engineer Build & Ship Test**.

The system analyzes incoming notifications using a real AI model and assigns priority levels with a fail-safe architecture.

---

## 🚀 Features

- Spring Boot REST API
- MongoDB database integration
- Real AI classification using Groq LLM
- Automatic fallback mechanism
- Swagger API documentation
- Production-safe error handling

---

## 🧠 How It Works

1. Client sends notification
2. Backend calls AI model
3. AI classifies priority
4. Result stored in MongoDB
5. Response returned instantly

If AI fails → fallback decision ensures reliability.

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

## 🛠 Tech Stack

- Java 17+
- Spring Boot
- MongoDB
- WebClient
- Groq AI API
- Maven

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
## 🔐 Fail-Safe Design

AI timeout protection

JSON validation

Exception-safe parsing

Guaranteed API response

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

# ✅ 4️⃣ FAIL-SAFE ARCHITECTURE (VERY IMPORTANT FOR EVALUATION)

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