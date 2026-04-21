# ADR-006: Inventory Health Implementation

## Status
Accepted

## Date
2026-04-21

## Context
TD-001 requires implementing `/api/v1/inventory/health?storeId={storeId}` which returns a health status for a store's inventory.

## Assumptions

1. **storeId = outletId**: The `storeId` parameter in the API maps directly to `GroceryStore.outletId` in the domain. This is the same value with different naming.

## Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        CONTROLLER                                 │
│  - Receives storeId from request                                 │
│  - Calls service.getStoreHealth(storeId)                        │
│  - Returns InventoryHealth (DTO) with storeId + status (String)│
└────────────────────────────┬──────────────────────────────────────┘
                           │ InventoryHealth (storeId, String)
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                         SERVICE                                  │
│  - Receives storeId                                            │
│  - Filters SeedData.groceryProducts by storeId (outletId)      │
│  - Uses HEALTH_CHECKS map with InventoryHealthStatus enum      │
│  - Returns InventoryHealth (DTO) with status.name() cast       │
│                                                                  │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │ InventoryHealthStatus (internal enum)                   │    │
│  │   HEALTHY, LOW_STOCK, OUT_OF_STOCK                     │    │
│  └─────────────────────────────────────────────────────────┘    │
│                                                                  │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │ HEALTH_CHECKS Map<Status, Function<Products, Boolean>> │    │
│  │   Maps each status to its business logic (lambda)      │    │
│  └─────────────────────────────────────────────────────────┘    │
└────────────────────────────┬──────────────────────────────────────┘
                           │ List<GroceryProduct>
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                        SEED DATA                                │
│  - GroceryProduct: availableStock, threshold, store (outletId) │
└─────────────────────────────────────────────────────────────────┘
```

## Implementation Details

### 1. InventoryHealthStatus Enum (Internal to Service)

This enum is internal business logic, not exposed to the controller:

```java
public enum InventoryHealthStatus {
    HEALTHY,
    LOW_STOCK,
    OUT_OF_STOCK
}
```

### 2. Health Check Map Pattern

The service uses a `Map` with lambdas to define health check logic:

```java
private static final Map<InventoryHealthStatus, Function<List<GroceryProduct>, Boolean>> HEALTH_CHECKS = Map.of(
    InventoryHealthStatus.OUT_OF_STOCK, products -> products.stream()
            .anyMatch(p -> p.getAvailableStock() == 0),
    InventoryHealthStatus.LOW_STOCK, products -> products.stream()
            .anyMatch(p -> p.getAvailableStock() > 0 && p.getAvailableStock() <= p.getThreshold()),
    InventoryHealthStatus.HEALTHY, products -> true
);
```

Status is determined by finding the first matching condition:

```java
InventoryHealthStatus status = HEALTH_CHECKS.keySet().stream()
    .filter(healthStatus -> HEALTH_CHECKS.get(healthStatus).apply(storeProducts))
    .findFirst()
    .orElse(InventoryHealthStatus.HEALTHY);
```

### 3. Service → Controller Contract

The service returns the enum name cast to String:

```java
return new InventoryHealth(storeId, status.name());
```

### 4. InventoryHealth DTO (Controller Response)

The response DTO contains only simple types:

```java
public record InventoryHealth(
    String storeId,
    String status  // cast from InventoryHealthStatus.name()
) {}
```

## Business Logic

| Status | Condition |
|--------|-----------|
| OUT_OF_STOCK | Any product has `availableStock == 0` |
| LOW_STOCK | No OUT_OF_STOCK, but any product has `0 < availableStock <= threshold` |
| HEALTHY | No critical conditions met |

## Consequences

### Positive
- **Separation of concerns**: Business logic (InventoryHealthStatus) stays internal to service
- **Declarative**: Health checks defined as data, not scattered if-else
- **Extensible**: Add new statuses easily by adding entries to the map
- **Testable**: Each health check is a pure function
- **Clean API**: Controller only deals with simple types (String)

### Negative
- Slightly more complex than simple if-else
- Depends on Map iteration order (insertion order matters)

### Alternative Considered
```java
// Traditional if-else (rejected)
if (hasOutOfStock) return OUT_OF_STOCK;
if (hasLowStock) return LOW_STOCK;
return HEALTHY;
```

## Related
- TD-001: Inventory Health endpoint
- ADR-005: DTOs as Records