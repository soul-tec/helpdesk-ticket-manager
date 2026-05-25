# IT Help Desk Ticket Manager

A command-line IT support ticketing system built in Java.

## What It Does

- Create tickets with requester name, category, priority, and issue description
- Track status: **Open → In Progress → Resolved → Closed**
- Filter tickets by status or priority
- Dashboard showing open/in-progress/resolved counts and critical alerts
- Data persists to disk between sessions

## Why I Built This

Every real IT support environment uses a ticketing system (ServiceNow, Jira, Zendesk). This project demonstrates I understand how those systems work — triage, escalation, status tracking, and resolution notes — not just how to use them.

## Java Concepts Demonstrated

| Concept | Where |
|---|---|
| Object-Oriented Programming | `Ticket.java` — encapsulation, constructors, methods |
| Enums | `Ticket.Status`, `Ticket.Priority`, `Ticket.Category` |
| Collections | `ArrayList<Ticket>` in `TicketManager` |
| Java Streams | `.stream().filter().sorted().collect()` |
| File I/O | `BufferedReader`/`PrintWriter` for persistence |
| Exception Handling | `try/catch` on file ops and user input |
| `Optional<T>` | `findById()` — modern null-safe lookups |

## How to Run

```bash
# Compile
javac -d out src/helpdesk/*.java

# Run
java -cp out helpdesk.Main
```

## Project Structure

```
src/helpdesk/
├── Main.java          # CLI menu loop
├── Ticket.java        # Ticket model (OOP, enums, serialization)
└── TicketManager.java # Business logic (CRUD, filter, persistence)
```

## Author

Mark Egbe-Osibe | github.com/soul-tec | andrewosibe@gmail.com
