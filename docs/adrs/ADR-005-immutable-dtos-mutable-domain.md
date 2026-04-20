# ADR-005: Immutable DTOs vs Mutable Domain Objects

## Status
Accepted

## Date
2026-04-20

## Context
We are using Java 25 which supports Records. Discussion arose about:
- Whether DTOs should be immutable
- Whether domain objects should use Records

## Decision

### DTOs → Immutable (Use Records)

**DTOs (Data Transfer Objects)** should be immutable using Java Records:

```java
// Instead of mutable + Lombok
public class AddProductRequest {
    private String userId;
    private String outletId;
    private String productId;
}

// Use immutable Records
public record AddProductRequest(
    String userId,
    String outletId,
    String productId
) {}
```

**Rationale**:
- DTOs are data containers with no behavior
- Immutability prevents accidental modification during transfer
- No setters needed — creates trust in data integrity
- Java 25 Records require no Lombok

### Domain Objects → Mutable (Keep Setters)

**Domain objects** should remain mutable:

**Rationale**:
- **ORM Compatibility**: JPA/Hibernate requires setters for entity loading and lazy initialization
- **Spring Data**: Repository implementations may need to hydrate entities
- **Builder Pattern**: Lombok's `@Builder` can still be used for construction
- **Validation**: May need to modify state during business operations

```java
// Domain stays mutable for ORM
@Entity
public class User {
    private String userId;
    private String firstName;
    
    // Setters needed for JPA/Spring Data
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
```

## Consequences

### Positive (DTOs as Records)
- No null safety issues from unexpected mutations
- Thread-safe by design
- Less code (no Lombok needed)
- Clear contract — data cannot change after creation
- Automatically gets `equals()`, `hashCode()`, `toString()`, and constructor
- JSON serialization/deserialization works out of the box with Jackson

### Positive (Domain as Mutable)
- ORM frameworks work out of the box (standard approach)
- Lazy loading works correctly
- Standard JPA patterns supported
- Easier partial updates (PUT/PATCH scenarios)

### Negative (DTOs as Records)
- Cannot extend another class (Records implicitly extend `java.lang.Record`)
- Only one canonical constructor — workarounds require static factory methods
- Less flexibility for complex validation scenarios

### Negative (Domain as Mutable)
- Potential for accidental modification in concurrent scenarios
- Requires discipline to not mutate in inappropriate places
- Setters can expose internal state unintentionally

---

## Implementation Notes

When implementing TD-002 (Input Validation):
- Use Bean Validation on Records via constructor parameters — **this works!**
- Example: `public record AddProductRequest(@NotBlank String userId, ...)`
- Add `@Valid` in controller: `@Valid @RequestBody AddProductRequest request`

## Related

- TD-002: Input Validation (will use Records)
- TD-004: O(n) data access (separate from this decision)