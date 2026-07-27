# AI Help Desk Assistant

An AI-powered Help Desk Assistant built using **Spring Boot** and **Spring AI** to automate IT support by understanding user queries, managing support tickets, and sending email notifications.

## Features

- AI-powered conversational support using Google Gemini/Ollama
- Create, search, update, and delete help desk tickets
- Automatic ticket categorization and priority assignment
- Chat memory for context-aware conversations
- Email notification after successful ticket creation
- PostgreSQL database integration
- RESTful APIs for AI interactions

## Tech Stack

- Java 21
- Spring Boot
- Spring AI
- Google Gemini / Ollama
- PostgreSQL
- Spring Data JPA
- Maven

## Project Workflow

1. User reports an issue.
2. AI understands the request and asks follow-up questions if needed.
3. Checks for existing tickets.
4. Creates a new ticket if none exists.
5. Stores ticket details in PostgreSQL.
6. Sends an email notification to the support team.
7. Returns the ticket status to the user.

## API Endpoint

```
POST /api/v1/ai
```

### Sample Request

```text
My Outlook crashes every time I open it after the latest Windows update.
```

### Sample Response

```text
Your support ticket has been created successfully.
Ticket ID: 15
Priority: HIGH
Status: OPEN
```

## Project Structure

```
src
├── controller
├── exceptions
├── service
├── repository
├── entity
├── tools
├── config
└── resources
```
