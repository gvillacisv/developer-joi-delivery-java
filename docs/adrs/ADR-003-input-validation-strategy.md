# ADR-003: Input Validation Strategy

## Status
Accepted

## Date
2026-04-20

## Context
Currently, there is NO input validation on any API endpoints. DTOs and query parameters have no constraints - any string (including null or empty) is accepted. This leads to:
- NullPointerExceptions at runtime
- 500 Internal Server Errors for client mistakes
- No field-level error messages

## Decision
We will implement **multi-layer validation** with Bean Validation (JSR-380):

### Layer 1: Bean Validation on DTOs
All request body DTOs should have Bean Validation annotations on fields.

### Layer 2: Validation on Parameters
All query parameters (`@RequestParam`) and path variables (`@PathVariable`) should have validation annotations.

### Layer 3: Service-Level Validation
- Validate that referenced entities exist
- Return 404 Not Found if entity doesn't exist

### Layer 4: Global Exception Handler
- Catch `MethodArgumentNotValidException` → 400 Bad Request
- Catch constraint violations → 400 Bad Request
- Catch custom "Not Found" exceptions → 404 Not Found
- Catch unexpected exceptions → 500 Internal Server Error

## Validation Rules by Field

### Request Bodies (DTOs)
All request body DTOs should have Bean Validation annotations on fields.

### Query Parameters
All `@RequestParam` annotated parameters should have validation annotations.

### Path Variables
All `@PathVariable` annotated parameters should have validation annotations.

**Note**: Specific rules per field will be defined per endpoint based on business requirements.

## Consequences

### Positive
- Fails fast with 400 instead of 500
- Clear, specific error messages
- Prevents invalid data entering the system
- Enforces contract at API boundary

### Negative
- Need to add spring-boot-starter-validation dependency
- Must implement global exception handler
- Slight performance overhead (negligible)

## Dependencies Required
```groovy
implementation 'org.springframework.boot:spring-boot-starter-validation'
```