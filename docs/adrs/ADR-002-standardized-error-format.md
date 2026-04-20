# ADR-002: Standardized Error Response Format

## Status
Accepted

## Date
2026-04-20

## Context
Currently, the application does not have a standardized error response format. Different scenarios may return different error structures (or none at all - just NPEs). We need a consistent error format across all endpoints.

## Decision
All API error responses will follow RFC 7807 (Problem Details for HTTP APIs) format:

```json
{
  "type": "https://api.joidelivery.com/problems/bad-request",
  "title": "Bad Request",
  "status": 400,
  "detail": "Human-readable error description",
  "instance": "/api/v1/cart/product"
}
```

### Error Code Mapping:

| HTTP Code | Error Key | Description |
|-----------|-----------|-------------|
| 400 | Bad Request | Invalid input / validation failure |
| 401 | Unauthorized | Missing or invalid authentication |
| 403 | Forbidden | Authenticated but not authorized |
| 404 | Not Found | Resource not found |
| 500 | Internal Server Error | Unexpected server error |

## Consequences

### Positive
- Consistent error handling experience for clients
- Easier debugging with timestamp and path
- Follows industry standard (RFC 7807)
- Can easily extend for field-level errors

### Negative
- Need to implement global exception handler
- Must update existing error handling

## Implementation Notes
- Use `@ControllerAdvice` for global exception handling
- Map specific exceptions to HTTP status codes
- Include request path for debugging