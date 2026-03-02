```md
# System Workflow

```md
## 🔄 Notification Processing Workflow

```mermaid
sequenceDiagram
    participant User
    participant Controller
    participant Service
    participant AI
    participant DB

    User->>Controller: POST /api/notifications/process
    Controller->>Service: processNotification()
    Service->>AI: analyze(message)
    AI->>Service: decision + reason
    Service->>DB: save(notification)
    DB-->>Service: saved entity
    Service-->>Controller: response
    Controller-->>User: JSON result
```
## Request Flow

1. User sends notification request
2. NotificationController receives API call
3. NotificationService processes request
4. AiDecisionService calls Groq AI API
5. AI returns classification
6. Result stored in MongoDB
7. Response returned to client

---

## Fail-Safe Workflow

If AI fails:
- fallback decision applied
- system continues execution
- API never crashes

```mermaid
flowchart TD
    A[AI Request] --> B{AI Available?}
    B -->|Yes| C[Parse AI Response]
    B -->|No| D[Fallback Decision]

    C --> E[Save Notification]
    D --> E
    E --> F[Return Response]
```
> 🔥 This diagram directly proves **fail-safe architecture requirement**.
