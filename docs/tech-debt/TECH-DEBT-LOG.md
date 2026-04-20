# Technical Debt Log

> This document tracks technical debt items for the JOI Delivery project.
> For larger projects, use GitHub Issues with labels (`tech-debt`, `priority:high`, `status:triage`).

---

## Active Technical Debt

| ID | Title | Severity | Status | Created |
|----|-------|----------|--------|---------|
| TD-001 | Inventory Health endpoint not implemented | Medium | Open | 2026-04-20 |
| TD-002 | Missing input validation | High | Open | 2026-04-20 |
| TD-003 | No authentication / authorization | High | Open | 2026-04-20 |
| TD-004 | O(n) data access performance | Medium | Open | 2026-04-20 |
| TD-005 | No containerization | Medium | Open | 2026-04-20 |

---

## TD-001: Inventory Health Endpoint Not Implemented

| Attribute | Value |
|-----------|-------|
| **Category** | Feature Gap |
| **Severity** | Medium |
| **Status** | Open |
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
| **Status** | Open |
| **Created** | 2026-04-20 |

**Description**: API endpoints lack input validation. DTOs accept null, empty, or malformed strings.

**Impact**: Runtime NullPointerExceptions, 500 errors for client mistakes.

**Technical Details**:
- `AddProductRequest` has no Bean Validation annotations
- Services return `null` for not-found entities

**Suggested Fix**: Add Spring Validation + Bean Validation annotations + global exception handler (see `docs/adrs/ADR-003-input-validation-strategy.md`).

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
| **Status** | Open |
| **Created** | 2026-04-20 |

**Description**: User and Product lookups use `List.stream().filter()` — O(n) complexity.

**Impact**: Performance degrades as data grows.

**Technical Details**:
- `UserService.fetchUserById()`: O(n)
- `ProductService.getProduct()`: O(n)

**Suggested Fix**: Use `Map<String, User>` and `Map<String, GroceryProduct>` for O(1) lookup.

---

## TD-005: No Containerization

| Attribute | Value |
|-----------|-------|
| **Category** | Infrastructure |
| **Severity** | Medium |
| **Status** | Open |
| **Created** | 2026-04-20 |

**Description**: No Dockerfile or docker-compose configuration.

**Impact**: Difficult to deploy consistently.

**Suggested Fix**: Add `Dockerfile` and `docker-compose.yml`.

---

## Priority Order

| Priority | ID | Item |
|----------|----|------|
| 1 (High) | TD-002 | Input Validation |
| 2 (High) | TD-003 | Authentication |
| 3 (Medium) | TD-001 | Inventory Health |
| 4 (Medium) | TD-004 | O(n) Performance |
| 5 (Medium) | TD-005 | Containerization |

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