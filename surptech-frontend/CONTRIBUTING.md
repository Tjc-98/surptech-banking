# Contributing to SurpTech Banking Frontend

Thank you for your interest in contributing to the SurpTech Banking Frontend!

## Development Setup

1. **Clone the repository**
```bash
git clone <repository-url>
cd surptech-banking/surptech-frontend
```

2. **Install dependencies**
```bash
npm install
```

3. **Set up environment variables**
```bash
cp .env.example .env.local
```

4. **Start development server**
```bash
npm run dev
```

## Code Style Guidelines

### TypeScript

- Use **explicit return types** for functions
- Use **interfaces** for object types
- Use **const** for immutable values
- Use **descriptive variable names**

```typescript
// Good
export const formatCurrency = (amount: number): string => {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
  }).format(amount);
};

// Avoid
export const format = (a: any) => {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
  }).format(a);
};
```

### React Components

- Use **functional components** with hooks
- Use **'use client'** directive for client components
- Use **PascalCase** for component names
- Use **props destructuring**

```typescript
// Good
'use client';

interface ButtonProps {
  label: string;
  onClick: () => void;
}

export const Button: React.FC<ButtonProps> = ({ label, onClick }) => {
  return <button onClick={onClick}>{label}</button>;
};
```

### File Naming

- **Components**: PascalCase (e.g., `CustomerInfoCard.tsx`)
- **Hooks**: camelCase with 'use' prefix (e.g., `useCustomerCreditInfo.ts`)
- **Utils**: camelCase with suffix (e.g., `validation.utils.ts`)
- **Services**: PascalCase with suffix (e.g., `CustomerService.ts`)

### Folder Structure

Follow the established architecture:

```
surptech-frontend/
├── app/              # Pages and routing
├── components/       # UI components
├── hooks/            # Custom hooks
├── services/         # Business logic
├── client/           # API clients
├── mapper/           # Data transformation
├── domain/           # Types and interfaces
├── utils/            # Utility functions
└── config/           # Configuration
```

## Adding New Features

### 1. Adding a New Component

```typescript
// components/NewComponent.tsx
'use client';

interface NewComponentProps {
  // Define props
}

/**
 * Description of what this component does.
 */
export const NewComponent: React.FC<NewComponentProps> = ({ /* props */ }) => {
  return (
    <div>
      {/* Component JSX */}
    </div>
  );
};
```

### 2. Adding a New API Endpoint

1. **Update API Config**
```typescript
// config/api.config.ts
export const ApiConfig = {
  endpoints: {
    // ... existing endpoints
    newEndpoint: '/new/endpoint',
  },
};
```

2. **Add Client Method**
```typescript
// client/DataAggregatorClient.ts
public async newMethod(): Promise<ResponseType> {
  const url = buildApiUrl(ApiConfig.endpoints.newEndpoint);
  // Implementation
}
```

3. **Add Service Method**
```typescript
// services/CustomerService.ts
public async newServiceMethod(): Promise<EntityType> {
  const response = await dataAggregatorClient.newMethod();
  return mapToEntity(response);
}
```

4. **Create Custom Hook**
```typescript
// hooks/useNewFeature.ts
export const useNewFeature = () => {
  // Hook implementation
};
```

### 3. Adding a New Page

```typescript
// app/new-page/page.tsx
'use client';

export default function NewPage() {
  return (
    <div>
      {/* Page content */}
    </div>
  );
}
```

## Testing

### Running Tests (Future)

```bash
npm test                 # Run all tests
npm test -- --watch     # Run tests in watch mode
npm test -- --coverage  # Run tests with coverage
```

### Writing Tests

```typescript
// __tests__/utils/validation.utils.test.ts
import { isValidSocialSecurityNumber } from '@/utils/validation.utils';

describe('isValidSocialSecurityNumber', () => {
  it('should validate correct SSN format', () => {
    expect(isValidSocialSecurityNumber('123-45-6789')).toBe(true);
  });

  it('should reject invalid SSN format', () => {
    expect(isValidSocialSecurityNumber('123456789')).toBe(false);
  });
});
```

## Code Review Checklist

Before submitting a pull request, ensure:

- [ ] Code follows the style guidelines
- [ ] All TypeScript types are properly defined
- [ ] Components have proper JSDoc comments
- [ ] No console.log statements in production code
- [ ] Error handling is implemented
- [ ] Loading states are handled
- [ ] Responsive design is maintained
- [ ] Accessibility is considered
- [ ] No hardcoded values (use config)
- [ ] Code is DRY (Don't Repeat Yourself)

## Commit Message Guidelines

Use conventional commit format:

```
type(scope): subject

body (optional)

footer (optional)
```

**Types**:
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting)
- `refactor`: Code refactoring
- `test`: Adding tests
- `chore`: Maintenance tasks

**Examples**:
```
feat(search): add SSN validation to search form

fix(api): handle timeout errors in data aggregator client

docs(readme): update installation instructions

refactor(mapper): simplify customer info mapping logic
```

## Pull Request Process

1. **Create a feature branch**
```bash
git checkout -b feature/your-feature-name
```

2. **Make your changes**
- Follow code style guidelines
- Add tests if applicable
- Update documentation

3. **Commit your changes**
```bash
git add .
git commit -m "feat(scope): description"
```

4. **Push to your branch**
```bash
git push origin feature/your-feature-name
```

5. **Create a Pull Request**
- Provide a clear description
- Reference any related issues
- Request review from maintainers

## Questions or Issues?

If you have questions or encounter issues:

1. Check the [README.md](README.md) for basic information
2. Review the [ARCHITECTURE.md](ARCHITECTURE.md) for design details
3. Open an issue on GitHub
4. Contact the maintainers

## License

By contributing, you agree that your contributions will be licensed under the same license as the project.
