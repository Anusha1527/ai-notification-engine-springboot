# Architecture Decisions
```md
## 🏗 System Architecture

```mermaid
flowchart TB

    subgraph Client Layer
        A[Frontend / Postman]
    end

    subgraph Application Layer
        B[Controller Layer]
        C[Service Layer]
        D[AI Integration Layer]
    end

    subgraph External Services
        E[Groq AI API]
        F[(MongoDB)]
    end

    A --> B
    B --> C
    C --> D
    D --> E
    C --> F

    style D fill:#fff3cd
    style E fill:#ffe6e6
    style F fill:#e6f7ff
```
> This screams **enterprise architecture**

## Spring Boot
Chosen for scalable enterprise backend architecture.

## MongoDB
Flexible schema suitable for dynamic notification data.

## WebClient
Used for non-blocking AI API communication.

## AI Integration
Groq LLM used for real-time notification classification.

## Fail-Safe Strategy
AI systems may fail or return invalid responses.
Fallback logic guarantees uninterrupted service.