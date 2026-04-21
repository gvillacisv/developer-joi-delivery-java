# JOI Delivery API Specification

> **Version**: 1.0.0  
> **Status**: Contract-First Specification  
> **Last Updated**: 2026-04-20

---

## 1. Overview

This document defines the formal API specification for JOI Delivery backend services. All implementations MUST adhere to this contract.

### 1.1 Base URL

```
http://localhost:8080/api/v1
```

### 1.2 Content Type

All requests and responses use `application/json`.

---

## 2. Endpoints

### 2.1 Cart Operations

#### 2.1.1 Add Product to Cart

```
POST /api/v1/cart/product
```

**Request Body**:

| Field | Type | Required | Constraints |
|-------|------|----------|-------------|
| `userId` | String | ✅ Yes | Non-empty, alphanumeric |
| `productId` | String | ✅ Yes | Non-empty, alphanumeric |
| `outletId` | String | ✅ Yes | Non-empty, alphanumeric |
| `quantity` | Integer | No (default: 1) | Min: 1, Max: 99 |

```json
{
  "userId": "user101",
  "productId": "product101",
  "outletId": "store101",
  "quantity": 2
}
```

**Success Response** (`200 OK`):

| Field | Type | Description |
|-------|------|-------------|
| `cart.cartId` | String | Unique cart identifier |
| `cart.outlet.name` | String | Store name |
| `cart.outlet.outletId` | String | Store identifier |
| `cart.products[]` | Array | List of cart items |
| `cart.user.userId` | String | User identifier |
| `cart.user.firstName` | String | User first name |
| `cart.user.lastName` | String | User last name |
| `cart.user.email` | String | User email |
| `product.productId` | String | Added product ID |
| `product.productName` | String | Product name |
| `product.mrp` | Number | Maximum retail price |
| `sellingPrice` | Number | Final selling price |

**Error Responses**:

| Code | Condition |
|------|-----------|
| `400 Bad Request` | Missing or empty required fields |
| `404 Not Found` | User, product, or outlet not found |

---

#### 2.1.2 View Cart

```
GET /api/v1/cart/view?userId={userId}
```

**Query Parameters**:

| Parameter | Type | Required | Constraints |
|-----------|------|----------|-------------|
| `userId` | String | ✅ Yes | Non-empty |

**Success Response** (`200 OK`):

| Field | Type | Description |
|-------|------|-------------|
| `cartId` | String | Unique cart identifier |
| `outlet` | Object | Associated store |
| `products[]` | Array | Cart items |
| `user` | Object | User details |

**Error Responses**:

| Code | Condition |
|------|-----------|
| `400 Bad Request` | Missing userId parameter |
| `404 Not Found` | User not found or has no cart |

---

### 2.2 Inventory Operations

#### 2.2.1 Inventory Health Check (TD-001)

```
GET /api/v1/inventory/health?storeId={storeId}
```

**Query Parameters**:

| Parameter | Type | Required | Constraints |
|-----------|------|----------|-------------|
| `storeId` | String | ✅ Yes | Non-empty |

**Success Response** (`200 OK`):

| Field | Type | Description |
|-------|------|-------------|
| `storeId` | String | Store identifier |
| `status` | String | Health status: `HEALTHY`, `LOW_STOCK`, `OUT_OF_STOCK` |

**Error Responses**:

| Code | Condition |
|------|-----------|
| `400 Bad Request` | Missing storeId parameter |
| `404 Not Found` | Store not found |

---

## 3. Data Models

### 3.1 User

| Field | Type | Nullable |
|-------|------|----------|
| `userId` | String | No |
| `username` | String | Yes |
| `firstName` | String | No |
| `lastName` | String | No |
| `email` | String | No |
| `phoneNumber` | String | No |
| `cart` | Cart | Yes |

### 3.2 Product

| Field | Type | Nullable | Notes |
|-------|------|----------|-------|
| `productId` | String | No | |
| `productName` | String | No | |
| `mrp` | BigDecimal | No | |
| `sellingPrice` | BigDecimal | Yes | |
| `weight` | BigDecimal | Yes | |
| `expiryDate` | String | Yes | ISO 8601 format (e.g., "2026-05-20") |
| `threshold` | Integer | Yes | Low stock threshold |
| `availableStock` | Integer | No | |
| `discount` | BigDecimal | Yes | |
| `store` | GroceryStore | Yes | |

### 3.3 Outlet

| Field | Type | Nullable |
|-------|------|----------|
| `outletId` | String | No |
| `name` | String | No |
| `description` | String | Yes |
| `inventory` | Array | Yes |

### 3.4 Cart

| Field | Type | Nullable |
|-------|------|----------|
| `cartId` | String | No |
| `outlet` | Outlet | Yes |
| `products` | List<Product> | No (empty list) |
| `user` | User | Yes |

---

## 4. Security Requirements

| Requirement | Status | Priority |
|-------------|--------|----------|
| API Key Authentication | Not Implemented | HIGH |
| OAuth 2.0 | Not Implemented | HIGH |
| Input Sanitization | Not Implemented | HIGH |
| Rate Limiting | Not Implemented | MEDIUM |

---

## 5. Validation Rules

All input fields MUST follow these rules:

| Field | Rule |
|-------|------|
| `userId` | `^[a-zA-Z0-9_-]{3,50}$` |
| `productId` | `^[a-zA-Z0-9_-]{3,50}$` |
| `outletId` | `^[a-zA-Z0-9_-]{3,50}$` |

---

## 6. Error Response Format

All error responses MUST follow RFC 7807 (Problem Details for HTTP APIs) format:

```json
{
  "type": "https://api.joidelivery.com/problems/validation-error",
  "title": "Bad Request",
  "status": 400,
  "detail": "userId must not be blank",
  "instance": "/api/v1/cart/product"
}
```

---

## 7. Versioning

- URL-based versioning: `/api/v1/`
- This is version 1.0.0

---

## 8. Future Enhancements (v1.1)

The following features are documented for future implementation:

- **Authentication**: API Key / OAuth 2.0 security schemes
- **Rate Limiting**: Protect API from abuse

---

## 9. Technical Debt

Known technical debt items are tracked in: `docs/tech-debt/TECH-DEBT-LOG.md`

Key items to address before production:
- TD-001: Inventory Health endpoint implementation
- TD-002: Input validation (HIGH priority)
- TD-003: Authentication (HIGH priority)

---

## 10. OpenAPI Schema Reference

For full OpenAPI 3.0 schema, see: `docs/openapi.yaml`

> **Note**: This document is the human-readable rendering of the OpenAPI spec. In case of conflicts, `docs/openapi.yaml` is the authoritative source.