# ADR-003: Input Validation Strategy

## Status
Accepted

## Date
2026-04-20

## Context
Currently, there is NO input validation on any API endpoints. The `AddProductRequest` DTO has no constraints - any string (including null or empty) is accepted. This leads to:
- NullPointerExceptions at runtime
- 500 Internal Server Errors for client mistakes
- No field-level error messages

## Decision
We will implement **multi-layer validation** with Bean Validation (JSR-380):

### Layer 1: Bean Validation on DTOs
```java
public class AddProductRequest {
    @NotBlank(message = "userId must not be blank")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{3,50}$")
    private String userId;
    
    @NotBlank(message = "productId must not be blank")
    private String productId;
    
    @NotBlank(message = "outletId must not be blank")
    private String outletId;
}
```

### Layer 2: Service-Level Validation
- Validate that referenced entities exist (user, product, outlet)
- Return 404 Not Found if entity doesn't exist

### Layer 3: Global Exception Handler
- Catch `MethodArgumentNotValidException` → 400 Bad Request
- Catch custom "Not Found" exceptions → 404 Not Found
- Catch unexpected exceptions → 500 Internal Server Error

## Validation Rules by Field

| Field | Rules |
|-------|-------|
| `userId` | `@NotBlank`, `@Pattern([a-zA-Z0-9_-]{3,50})` |
| `productId` | `@NotBlank`, `@Pattern([a-zA-Z0-9_-]{3,50})` |
| `outletId` | `@NotBlank`, `@Pattern([a-zA-Z0-9_-]{3,50})` |

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