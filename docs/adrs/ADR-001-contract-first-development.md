# ADR-001: Contract-First API Development with OpenAPI

## Status
Accepted

## Date
2026-04-20

## Context
The JOI Delivery project currently lacks a formal API specification. The only documentation is an informal README.md with example JSON responses. This approach has led to:
- No machine-readable contract
- Inconsistent implementation vs documentation
- No automated validation
- Missing security schemes
- No input validation enforcement

## Decision
We will adopt **Contract-First Development** using OpenAPI 3.0 as the single source of truth.

### Specific Decisions:

1. **Specification Location**: `docs/openapi.yaml`
2. **Reference Documentation**: `docs/SPEC.md` (Markdown rendering of OpenAPI)
3. **Validation**: Use Spring Validation + Bean Validation annotations derived from spec
4. **Code Generation**: Not required initially (manual contract adherence)
5. **Testing**: TDD approach where tests validate against spec requirements

## Consequences

### Positive
- Clear contract between frontend/backend teams
- Can generate API documentation automatically
- Enables contract testing
- Clear validation rules
- Version control for API changes

### Negative
- Initial overhead to write specs before code
- Need to maintain spec alongside code
- Learning curve for team members

## Implementation Plan

1. Define OpenAPI spec for existing endpoints
2. Add Bean Validation to DTOs
3. Implement global exception handler
4. Write TDD tests for validation
5. Add security schemes to spec (future)

## Related ADRs
- ADR-002: Error Response Standardization
- ADR-003: Input Validation Strategy