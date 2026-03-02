# Deployment Guide

## Backend Deployment (Render)

1. Push repository to GitHub
2. Create Web Service on Render
3. Connect repository

### Build Command
./mvnw clean package

### Start Command
java -jar target/*.jar

---

## Environment Variables

groq.api.key=YOUR_API_KEY
groq.api.url=https://api.groq.com/openai/v1/chat/completions

---

## Health Endpoint

/health

```md
## ☁ Deployment Architecture

```mermaid
flowchart LR

    Dev[Developer Push] --> GitHub
    GitHub --> Render[Render Backend Deployment]
    Render --> API[Live Spring Boot API]

    API --> Mongo[(MongoDB Atlas)]
    API --> Groq[Groq AI API]

    User --> API

    style Render fill:#e8ffe8
    style Groq fill:#fff0e6
```