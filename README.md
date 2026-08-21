# Help Desk

An AI-powered IT help desk backend built with **Spring Boot** and **Spring AI**. Users chat with an AI assistant named **Door**, which can search, create, update, and delete support tickets, and notify the support team by email — all through natural conversation instead of manual forms.

## What it does

- Exposes a chat API where users describe an IT problem in plain language.
- The AI assistant (Door) follows a scripted workflow: understand the issue, check for an existing ticket, create a new one if needed (auto-assigning priority, category, and status), and trigger a support notification.
- Ticket data is persisted to a relational database via JPA/Hibernate.
- Conversation history is kept per user (by email) using Spring AI's chat memory, backed by a JDBC-persisted store.
- Supports both a single-shot response endpoint and a streaming (SSE-style) endpoint for real-time chat.

## Tech Stack

- **Java / Spring Boot**
- **Spring AI** — `ChatClient`, tool calling, chat memory
- **Groq** (OpenAI-compatible API) — LLM provider (`llama-3.3-70b-versatile`), configured via `spring.ai.openai.*`
- **PostgreSQL** — primary datastore (tickets + chat memory)
- **Spring Data JPA / Hibernate**
- **Lombok**
- **Reactor (`Flux`)** — for streaming responses

## Architecture

```
Client
  │
  ▼
AiController  ──►  AiService / AiServiceImpl  ──►  ChatClient (Spring AI)
                                                        │
                                                        ├── EmailTool           (notifies support team)
                                                        └── TicketDatabaseTool  ──► TicketService/Impl ──► TicketRepo ──► Ticket (JPA entity)
```

- **AiController** — REST endpoints for chat (`/api/v1/ai`) and streaming chat (`/api/v1/ai/stream`). The caller's email is passed in a header and used as the conversation ID.
- **AiConfig** — builds the `ChatClient` bean, wires up chat memory (`MessageWindowChatMemory`, last 10 messages, backed by `JdbcChatMemoryRepository`) and a logging advisor.
- **AiService / AiServiceImpl** — loads the system prompt (`helpdesk_system.st`), injects the user's email as a prompt variable, registers the tools, and calls the LLM (blocking or streaming).
- **Tools** (functions the LLM can call):
  - `TicketDatabaseTool` — create, fetch by email, update, delete tickets; get current server time.
  - `EmailTool` — records/sends a notification to the support team about a new ticket.
- **Ticket domain**:
  - `Ticket` — JPA entity (summary, description, category, priority, status, email, timestamps).
  - `Priority` — `LOW`, `MEDIUM`, `HIGH`, `URGENT`.
  - `Status` — `OPEN`, `RESOLVED`, `CLOSED`.
  - `TicketRepo` — Spring Data repository (find/delete by email).
  - `TicketService / TicketServiceImpl` — business logic for CRUD operations on tickets.
- **Exception handling** — `GlobalExceptionHandler` (`@RestControllerAdvice`) maps `ResourceNotFoundException` and `TicketNotFoundException` to appropriate HTTP responses.

## The AI Assistant ("Door")

The assistant's behavior is fully driven by the system prompt template `helpdesk_system.st`, which defines:

- **Identity** — friendly, concise, professional IT help desk assistant.
- **Workflow** — understand the issue → check for an existing ticket (`getMyTicket`) → create a ticket only if none exists → send an email notification → confirm to the user.
- **Priority rules** — e.g. login/VPN/Outlook issues → `HIGH`; server outage/security/data loss → `URGENT`; single app or printer issues → `MEDIUM`; general questions → `LOW`.
- **Category rules** — Network, Email, Hardware, Software, Account, Security, Other.
- **Guardrails** — never invents ticket IDs, statuses, or confirmations; never asks for passwords/OTPs/PINs; never asks the user for their email (it's injected automatically); only reports success after a tool call actually succeeds.

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/v1/ai` | Send a chat message (header: `email`), get a full text response |
| `POST` | `/api/v1/ai/stream` | Same as above, but streamed as a `Flux<String>` |

> Ticket CRUD is currently exposed to the AI only through tools (`TicketDatabaseTool`); there is no separate public REST controller for tickets in the provided sources.

## Configuration

Key properties (`application.properties` / `application-local.properties`):

- `spring.datasource.*` — PostgreSQL connection (via `DATASOURCE_URL`, `USERNAME`, `PASSWORD` env vars)
- `spring.jpa.hibernate.ddl-auto=update`
- `spring.ai.openai.api-key` / `base-url` / `chat.options.model` — configured to point at **Groq's** OpenAI-compatible endpoint
- `spring.ai.chat.memory.repository.jdbc.initialize-schema=ALWAYS` — auto-creates the chat memory schema
- Local profile enables SQL logging and Spring AI advisor debug logs

**Required environment variables:**
```
DATASOURCE_URL=jdbc:postgresql://<host>:5432/<db>
USERNAME=<db_username>
PASSWORD=<db_password>
GROQ_API_KEY=<your_groq_api_key>
```

## Reference: Company Leave Policy

The repository also includes `LeavePolicy.pdf` (KB0044163 – HR India Leave and Holiday Policy), a reference/knowledge-base document covering:

- Leave types: Privilege Leave (22 days/yr), Casual/Sick Leave (12 days/yr), Maternity/Paternity/Adoption Leave, Loss of Pay Leave, Bereavement Leave (5 days), Relocation Transfer Leave (3 days)
- 11 declared public holidays per year
- Leave application, carry-forward (max 90 days), encashment, and auto-approval rules

This document isn't wired into the code yet, but it's a natural candidate for a future **HR knowledge-base tool** (e.g., RAG over policy PDFs) that Door could use to answer employee leave questions alongside IT tickets.

## Getting Started

1. Provision a PostgreSQL database.
2. Set the required environment variables above.
3. Run the app:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Call the chat endpoint:
   ```bash
   curl -X POST http://localhost:8080/api/v1/ai \
     -H "email: user@example.com" \
     -H "Content-Type: text/plain" \
     -d "My VPN isn't connecting"
   ```

## Notes / Possible Improvements

- `AiConfig`'s default system prompt ("You are a teacher of every every subject...") is overridden per-request in `AiServiceImpl`, but is worth cleaning up since it's unused/misleading as a fallback.
- `Ticket.email` has a commented-out `@Column(unique = true)` — worth revisiting since `findByEmail`/`deleteByEmail` currently assume one ticket per user.
- No dedicated ticket REST controller yet — could be added for admin/dashboard use outside the chat flow.
