# ADR-006: Consider Clean Architecture for Future

## Status
Proposed

## Date
2026-04-20

## Context
Current architecture is a simple layered approach:
```
Controller → Service → Domain
```

For a challenge/interview, this is appropriate. However, for production-ready code, consider migrating to Clean Architecture or Hexagonal Architecture.

## Decision

**For this challenge**: Keep layered architecture — it meets the requirements.

**For future production**: Consider migrating to Clean Architecture.

> **Important**: This ADR is a **suggestion for production only**. It should **NOT** be implemented in any AI SDD workflow for this challenge. The current layered architecture is sufficient and appropriate for the scope.

### Clean Architecture Layers

```
┌─────────────────────────────────────┐
│  Frameworks & Drivers               │
│  (Controllers, Persistence)         │
└─────────────────┬───────────────────┘
                  ↓
┌─────────────────────────────────────┐
│         Interface Adapters          │
│   (DTOs, Mappers, Presenters)       │
└─────────────────┬───────────────────┘
                  ↓
┌─────────────────────────────────────┐
│            Use Cases                │
│     (Application Services)          │
└─────────────────┬───────────────────┘
                  ↓
┌─────────────────────────────────────┐
│              Entities               │
│        (Domain Models)              │
└─────────────────────────────────────┘
```

### Hexagonal Architecture

```
        ┌──────────────┐
        │   Adapters   │
        │  (REST, DB)  │
        └──────┬───────┘
               ↓
┌─────────────────────────────────────┐
│        Ports (Interfaces)           │
└─────────────────┬───────────────────┘
               ↓
┌─────────────────────────────────────┐
│              Domain                 │
│      (Core Business Logic)          │
└─────────────────────────────────────┘
```

## Consequences

### Positive (for production)
- **Testability**: Domain isolated, easy to unit test
- **Maintainability**: Clear boundaries
- **Flexibility**: Swap implementations (DB, API) without changing domain
- **Scalability**: Teams can work on different layers independently

### Negative (for this challenge)
- **Overhead**: More code, more files, more complexity
- **Learning curve**: Harder to understand for interviewers
- **Time cost**: Refactoring takes time away from core features
- **Over-engineering**: Not needed for a challenge scope

## Recommendation

| Project Type | Architecture |
|--------------|--------------|
| Interview Challenge | Layered (current) ✅ |
| Production | Clean/Hexagonal ✅ |
| MVP (small team) | Layered or Modular ✅ |
| Enterprise | Clean/Hexagonal ✅ |

## When to Consider Migration

- When team grows beyond 5 developers
- When domain logic becomes complex
- When swapping data sources (DB, cache, external API)
- When multiple teams need to work on same codebase

## Note

This is a **decision record only**. It documents architectural awareness for future consideration. No implementation is required for this challenge.

## Related

- This ADR is informational — no TD item needed