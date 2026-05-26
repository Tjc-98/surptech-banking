# Frontend Architecture Documentation

## Overview

The SurpTech Banking Frontend follows a layered architecture that mirrors the backend microservices structure while incorporating React and Next.js best practices.

## Architectural Layers

### 1. Presentation Layer (Components)

**Location**: `components/`

**Purpose**: Pure UI components responsible for rendering and user interaction.

**Components**:
- `SearchForm.tsx`: Input form with validation
- `CustomerInfoCard.tsx`: Data display component
- `LoadingSpinner.tsx`: Loading state indicator
- `ErrorMessage.tsx`: Error display component

**Principles**:
- Single Responsibility: Each component has one clear purpose
- Composition: Components are composed together in pages
- Reusability: Components are generic and reusable
- Props-driven: All data comes from props

### 2. Page Layer (App Router)

**Location**: `app/`

**Purpose**: Next.js routing and page composition.

**Files**:
- `layout.tsx`: Root layout with header/footer
- `page.tsx`: Home page orchestrating components
- `globals.css`: Global styles

**Principles**:
- Container pattern: Pages contain business logic
- Component composition: Pages assemble components
- State management: Pages manage application state

### 3. Hook Layer

**Location**: `hooks/`

**Purpose**: Reusable stateful logic and side effects.

**Hooks**:
- `useCustomerCreditInfo.ts`: Manages customer data fetching state

**Principles**:
- Encapsulation: Hides complex state management
- Reusability: Can be used across multiple components
- Separation of concerns: Isolates data fetching logic

### 4. Service Layer

**Location**: `services/`

**Purpose**: Business logic orchestration and API coordination.

**Services**:
- `CustomerService.ts`: Customer operations and data transformation

**Principles**:
- Business logic: Contains domain-specific operations
- Abstraction: Hides implementation details from components
- Testability: Easy to unit test in isolation

### 5. Client Layer

**Location**: `client/`

**Purpose**: HTTP communication with backend services.

**Clients**:
- `DataAggregatorClient.ts`: REST API client for data-aggregator

**Principles**:
- Single responsibility: Only handles HTTP communication
- Error handling: Converts HTTP errors to domain errors
- Configuration: Uses centralized API configuration

### 6. Mapper Layer

**Location**: `mapper/`

**Purpose**: Data transformation between API DTOs and domain entities.

**Mappers**:
- `CustomerCreditInfoMapper.ts`: Converts snake_case ↔ camelCase

**Principles**:
- Separation of concerns: API contracts vs. domain models
- Bidirectional: Supports both directions of transformation
- Type safety: Ensures correct data structure

### 7. Domain Layer

**Location**: `domain/`

**Purpose**: Type definitions and domain models.

**Structure**:
```
domain/
├── entity/          # Domain entities (camelCase)
├── request/         # Request DTOs (snake_case)
├── response/        # Response DTOs (snake_case)
└── error/           # Error types
```

**Principles**:
- Type safety: Strong TypeScript typing
- Immutability: Interfaces define read-only contracts
- Documentation: Comprehensive JSDoc comments

### 8. Utility Layer

**Location**: `utils/`

**Purpose**: Reusable helper functions.

**Utilities**:
- `validation.utils.ts`: Input validation functions
- `format.utils.ts`: Data formatting functions

**Principles**:
- Pure functions: No side effects
- Reusability: Used across the application
- Testability: Easy to unit test

### 9. Configuration Layer

**Location**: `config/`

**Purpose**: Application configuration and constants.

**Files**:
- `api.config.ts`: API endpoints and settings

**Principles**:
- Centralization: Single source of truth
- Environment-aware: Supports different environments
- Type safety: Strongly typed configuration

## Data Flow

### Request Flow (User Action → API Call)

```
User Input
    ↓
SearchForm (Component)
    ↓
page.tsx (Container)
    ↓
useCustomerCreditInfo (Hook)
    ↓
CustomerService (Service)
    ↓
DataAggregatorClient (Client)
    ↓
HTTP Request → Backend API
```

### Response Flow (API Response → UI Update)

```
HTTP Response ← Backend API
    ↓
DataAggregatorClient (Client)
    ↓ (CustomerCreditInfoResponse - snake_case)
CustomerCreditInfoMapper (Mapper)
    ↓ (CustomerCreditInfo - camelCase)
CustomerService (Service)
    ↓
useCustomerCreditInfo (Hook)
    ↓
page.tsx (Container)
    ↓
CustomerInfoCard (Component)
    ↓
UI Update
```

## Design Patterns

### 1. Container/Presentational Pattern

**Container Components** (page.tsx):
- Manage state and business logic
- Handle data fetching
- Pass data to presentational components

**Presentational Components** (components/):
- Receive data via props
- Focus on UI rendering
- No business logic

### 2. Custom Hooks Pattern

**Purpose**: Extract and reuse stateful logic

**Example**: `useCustomerCreditInfo`
- Manages loading, error, and data state
- Provides fetch and reset functions
- Can be used in multiple components

### 3. Service Layer Pattern

**Purpose**: Centralize business logic

**Benefits**:
- Components stay focused on UI
- Business logic is testable
- Easy to modify without affecting UI

### 4. Mapper Pattern

**Purpose**: Separate API contracts from domain models

**Benefits**:
- API changes don't affect domain layer
- Supports different naming conventions
- Type-safe transformations

### 5. Error Handling Pattern

**ApiError Class**:
- Extends native Error
- Includes status code
- Provides helper methods (isNotFound, isServerError)

**Error Flow**:
1. Client throws ApiError
2. Service catches and re-throws
3. Hook catches and sets error state
4. Component displays ErrorMessage

## Comparison with Backend Architecture

### Similarities

| Frontend | Backend | Purpose |
|----------|---------|---------|
| client/ | client/ | HTTP communication |
| services/ | procedure/ | Business logic |
| domain/entity/ | domain/entity/ | Domain models |
| domain/request/ | domain/request/ | Request DTOs |
| domain/response/ | domain/response/ | Response DTOs |
| mapper/ | mapper/ | Data transformation |
| utils/ | common/validation/ | Utilities |
| config/ | application.properties | Configuration |

### Differences

| Frontend | Backend Equivalent | Notes |
|----------|-------------------|-------|
| components/ | N/A | UI rendering (frontend-only) |
| hooks/ | N/A | React state management |
| app/ | controller/ | Routing and entry points |
| N/A | repository/ | Data persistence (backend-only) |

## Best Practices Implemented

### TypeScript
- ✅ Strict mode enabled
- ✅ Explicit return types
- ✅ Interface over type for objects
- ✅ Comprehensive JSDoc comments

### React
- ✅ Functional components with hooks
- ✅ Proper dependency arrays
- ✅ useCallback for function memoization
- ✅ Controlled form inputs

### Next.js
- ✅ App Router (modern approach)
- ✅ Client components marked with 'use client'
- ✅ Metadata for SEO
- ✅ Environment variable configuration

### Code Organization
- ✅ Single Responsibility Principle
- ✅ Separation of Concerns
- ✅ DRY (Don't Repeat Yourself)
- ✅ Clear folder structure

### Error Handling
- ✅ Custom error classes
- ✅ User-friendly error messages
- ✅ Graceful degradation
- ✅ Loading states

### Styling
- ✅ Tailwind CSS utility classes
- ✅ Responsive design
- ✅ Consistent color scheme
- ✅ Accessible components

## Testing Strategy (Future Enhancement)

### Unit Tests
- Utils: validation, formatting functions
- Mappers: data transformation
- Services: business logic

### Integration Tests
- Hooks: data fetching logic
- Components: user interactions

### E2E Tests
- Full user flows
- API integration

## Performance Considerations

### Optimization Techniques
- **Code Splitting**: Next.js automatic code splitting
- **Lazy Loading**: Components loaded on demand
- **Memoization**: useCallback for functions
- **Efficient Re-renders**: Proper React key usage

### Future Enhancements
- React.memo for expensive components
- useMemo for expensive calculations
- Virtual scrolling for large lists
- Image optimization with next/image

## Security Considerations

### Current Implementation
- ✅ SSN masking in display
- ✅ No sensitive data in localStorage
- ✅ Input validation
- ✅ Error message sanitization

### Future Enhancements
- Authentication/Authorization
- HTTPS enforcement
- Content Security Policy
- Rate limiting

## Scalability

### Current Architecture Supports
- Adding new pages (Next.js routing)
- Adding new API endpoints (client layer)
- Adding new components (component library)
- Adding new business logic (service layer)

### Future Considerations
- State management library (Redux, Zustand)
- API caching (React Query, SWR)
- Component library (Storybook)
- Micro-frontend architecture
