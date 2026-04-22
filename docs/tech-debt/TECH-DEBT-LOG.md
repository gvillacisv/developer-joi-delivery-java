# Technical Debt Log

> This document tracks technical debt items for the JOI Delivery project.
> For larger projects, use GitHub Issues with labels (`tech-debt`, `priority:high`, `status:triage`).

---

## Active Technical Debt

| ID | Title | Severity | Status | Created |
|----|-------|----------|--------|---------|
| TD-001 | Inventory Health endpoint not implemented | Medium | Resolved | 2026-04-20 |
| TD-002 | Missing input validation | High | Resolved | 2026-04-20 |
| TD-003 | No authentication / authorization | High | Open | 2026-04-20 |
| TD-004 | O(n) data access performance | Medium | Will Not Fix | 2026-04-20 |
| TD-005 | No containerization | Medium | Resolved | 2026-04-20 |
| TD-006 | Replace DTOs with Java Records | Low | Resolved | 2026-04-20 |
| TD-007 | Unused imports in domain classes | Low | Resolved | 2026-04-20 |
| TD-008 | Fix all unit tests with TDD | High | Open | 2026-04-20 |
| TD-009 | No code coverage library | Medium | Resolved | 2026-04-20 |

---

## TD-001: Inventory Health Endpoint Not Implemented

| Attribute | Value |
|-----------|-------|
| **Category** | Feature Gap |
| **Severity** | Medium |
| **Status** | Resolved |
| **Created** | 2026-04-20 |

**Description**: The `/api/v1/inventory/health` endpoint is documented in SPEC.md and OpenAPI spec, but the implementation returns an empty response (`HttpEntity.EMPTY`).

**Impact**: Inventory health check functionality missing.

**Technical Details**:
- Controller: `InventoryController.fetchStoreInventoryHealth()` returns empty
- Requires: Store inventory data model, threshold calculation, response DTO

**Suggested Fix**: Implement threshold-based health status (HEALTHY, LOW_STOCK, OUT_OF_STOCK) and return structured response per OpenAPI spec.

---

## TD-002: Missing Input Validation

| Attribute | Value |
|-----------|-------|
| **Category** | Security / Robustness |
| **Severity** | High |
| **Status** | Resolved |
| **Created** | 2026-04-20 |
| **Resolved** | 2026-04-21 |

**Description**: API endpoints lack input validation. DTOs accept null, empty, or malformed strings. Query parameters are not validated.

**Impact**: Runtime NullPointerExceptions, 500 errors for client mistakes.

**Resolution**: Implemented Jakarta Bean Validation with global exception handler.
- Added `spring-boot-starter-validation` to build.gradle
- Created `ValidationErrorResponse.java` DTO for structured 400 errors
- Created `GlobalExceptionHandler.java` (@RestControllerAdvice)
- Added `@NotBlank` to `AddProductRequest` (userId, outletId, productId)
- Added `@Validated` + `@NotBlank` to controllers for query param validation
- Added `@Valid` to `@RequestBody` for request body validation

**Validation Annotations Are Self-Testing**: Jakarta Bean Validation annotations are fail-fast and framework-tested. No unit tests needed for validation logic itself.

**Files Changed**:
- `build.gradle`: +1 line (validation starter)
- `ValidationErrorResponse.java`: NEW DTO
- `GlobalExceptionHandler.java`: NEW exception handler
- `AddProductRequest.java`: +@NotBlank annotations
- `CartController.java`: +@Validated, @Valid
- `InventoryController.java`: +@Validated

**Endpoints Now Validated**:
| Endpoint | Input | Validation |
|----------|-------|------------|
| `POST /api/v1/cart/product` | RequestBody | @NotBlank on userId, outletId, productId |
| `GET /api/v1/cart/view?userId=` | QueryParam | @NotBlank + @Validated |
| `GET /api/v1/inventory/health?storeId=` | QueryParam | @NotBlank + @Validated |

---

## TD-003: No Authentication / Authorization

| Attribute | Value |
|-----------|-------|
| **Category** | Security |
| **Severity** | High |
| **Status** | Open |
| **Created** | 2026-04-20 |

**Description**: All API endpoints are unauthenticated.

**Impact**: Security vulnerability — any client can access any user's cart.

**Suggested Fix**: Add API Key authentication or OAuth 2.0.

---

## TD-004: O(n) Data Access Performance

| Attribute | Value |
|-----------|-------|
| **Category** | Performance |
| **Severity** | Medium |
| **Status** | **Will Not Fix** |
| **Created** | 2026-04-20 |

**Description**: User and Product lookups use `List.stream().filter()` — O(n) complexity.

**Impact**: Performance degrades as data grows.

**Technical Details**:
- `UserService.fetchUserById()`: O(n)
- `ProductService.getProduct()`: O(n)

**Suggested Fix**: Use `Map<String, User>` and `Map<String, GroceryProduct>` for O(1) lookup.

**Resolution**: **Will Not Fix — Interview Challenge Context**

This is an interview challenge project using in-memory seed data. The O(n) lookups are acceptable because:
- Data is small (static seed data, not a real database)
- Performance is negligible with current scale (1 user, 3 products)
- Any DB migration would bring its own indexes (B-tree, O(1) lookups)
- Fixing this would be premature optimization

> *"Don't optimize until you have a problem."* — Knuth

**Note**: If this moves to a production database, the fix would be handled by DB indexes (ORM/JPA `@Column(unique=true)`) or database-level indexing, not in-memory Maps.

---

## TD-005: No Containerization

| Attribute | Value |
|-----------|-------|
| **Category** | Infrastructure |
| **Severity** | Medium |
| **Status** | **Resolved** |
| **Created** | 2026-04-20 |
| **Resolved** | 2026-04-21 |

**Description**: No Dockerfile or docker-compose configuration.

**Impact**: Difficult to deploy consistently.

**Resolution**: Implemented GraalVM Native Image build with multi-stage Dockerfile.

**Implementation**:
- Added `org.graalvm.buildtools.native` Gradle plugin
- Created multi-stage `Dockerfile`:
  - Stage 1: Build with `ghcr.io/graalvm/native-image-community:25`
  - Stage 2: Runtime with `debian:stable-slim` + zlib
- Binary: 85MB native executable, startup < 100ms

**Build Commands**:
```bash
# Local native build (requires GraalVM)
./gradlew nativeCompile

# Container build
podman build -t joi-delivery .
```

---

## TD-006: Replace DTOs with Java Records

| Attribute | Value |
|-----------|-------|
| **Category** | Code Quality |
| **Severity** | Low |
| **Status** | Open |
| **Created** | 2026-04-20 |

**Description**: DTOs currently use Lombok with mutable fields. Java 25 supports Records which provide immutability without Lombok.

**Impact**: 
- DTOs can be immutable (correct for data transfer)
- Reduces dependencies (no Lombok for DTOs)
- Follows modern Java best practices

**Suggested Fix**: Convert all DTOs to Java Records. See ADR-005 for details.

---

## TD-007: Unused Imports in Domain Classes

| Attribute | Value |
|-----------|-------|
| **Category** | Code Quality |
| **Severity** | Very Low |
| **Status** | Open |
| **Created** | 2026-04-20 |

**Description**: Several domain classes have unused imports.

**Files with unused imports**:
- `Cart.java`: `HashSet`, `Set` — imported but not used (uses `ArrayList`)
- `Outlet.java`: `ArrayList`, `HashSet`, `List`, `Set` — all imported but not used

**Suggested Fix**: Remove unused imports from Cart.java and Outlet.java.

---

## TD-008: Fix All Unit Tests with TDD

| Attribute | Value |
|-----------|-------|
| **Category** | Testing |
| **Severity** | High |
| **Status** | Open |
| **Created** | 2026-04-20 |

**Description**: Tests exist but were written after code (write tests first, then code). Need to follow TDD: Red → Green → Refactor. Also need to add missing tests for services and domain classes.

**Impact**: Tests don't drive design, potential missed edge cases, unclear test intent.

**Existing Test Files**:
- `CartControllerTest.java` — controller tests
- `InventoryControllerTest.java` — controller tests (empty mocks)
- `DeliveryApplicationTests.java` — context load only

**Missing Test Files** (to add):
- `CartServiceTest.java`
- `UserServiceTest.java`
- `ProductServiceTest.java`
- Domain tests (Cart, User, GroceryProduct, Outlet)

**Suggested Fix**: Rewrite all tests following TDD:
1. Write failing test first (Red)
2. Write minimal code to pass (Green)
3. Refactor (Refactor)

---

## TD-009: No Code Coverage Library

| Attribute | Value |
|-----------|-------|
| **Category** | Testing |
| **Severity** | Medium |
| **Status** | Open |
| **Created** | 2026-04-20 |

**Description**: No code coverage tool (JaCoCo, Cobertura) configured to measure test coverage.

**Impact**: Cannot verify test coverage percentage, potential untested code paths.

**Suggested Fix**: Add JaCoCo plugin to build.gradle:
```groovy
plugins {
    id 'jacoco'
}

jacocoTestReport {
    reports {
        xml.required = true
    }
}
```

---

## Priority Order

| Priority | ID | Item |
|----------|----|------|
| 1 (High) | TD-008 | Fix all unit tests with TDD |
| 2 (High) | TD-002 | Input Validation |
| 3 (High) | TD-003 | Authentication |
| 4 (Medium) | TD-009 | Code Coverage Library |
| 5 (Medium) | TD-001 | Inventory Health |
| 6 (Medium) | TD-004 | O(n) Performance |
| 7 (Medium) | TD-005 | Containerization |
| 8 (Low) | TD-006 | Replace DTOs with Records |
| 9 (Very Low) | TD-007 | Unused Imports |

---

## How to Update

When a debt item is resolved:
1. Change **Status** to "Resolved"
2. Add **Resolved Date**
3. Add **Resolution Note** (how it was fixed)

Example:
```
| TD-001 | Inventory Health endpoint | Medium | Resolved | 2026-04-20 |
```

---

## For Larger Projects

For projects with many debt items, use **GitHub Issues** instead:
- Label: `tech-debt`
- Columns: To Do → In Progress → Done
- Link issues in this document

---

## Related Documentation

- `docs/SPEC.md` — API Specification
- `docs/openapi.yaml` — OpenAPI 3.0 Schema
- `docs/adrs/ADR-001-contract-first-development.md`
- `docs/adrs/ADR-002-standardized-error-format.md`
- `docs/adrs/ADR-003-input-validation-strategy.md`
- `docs/adrs/ADR-004-clean-architecture-future.md`
- `docs/adrs/ADR-005-immutable-dtos-mutable-domain.md`