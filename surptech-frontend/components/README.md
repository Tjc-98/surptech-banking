# Components Directory

## Structure

```
components/
├── ui/                      # Reusable UI primitives
│   ├── Icon.tsx            # Icon system
│   └── Card.tsx            # Card components
├── SearchForm.tsx          # Customer search form
├── CustomerInfoCard.tsx    # Customer data display
├── LoadingSpinner.tsx      # Loading indicator
└── ErrorMessage.tsx        # Error display
```

## UI Primitives (`ui/`)

### Icon Component

Centralized icon system for consistent icon usage.

**Usage:**
```tsx
import { Icon } from '@/components/ui/Icon';

<Icon name="user" className="w-5 h-5 text-primary-600" />
```

**Available Icons:**
- `user` - User profile icon
- `card` - Credit card icon
- `error` - Error/warning icon
- `close` - Close/dismiss icon
- `check` - Checkmark icon
- `spinner` - Loading spinner

**Adding New Icons:**
1. Open `components/ui/Icon.tsx`
2. Add icon name to `IconName` type
3. Add SVG path to `icons` object

### Card Components

Reusable card layout components.

**Usage:**
```tsx
import { Card, CardHeader, CardContent } from '@/components/ui/Card';

<Card>
  <CardHeader title="Title" subtitle="Optional subtitle" />
  <CardContent>
    {/* Your content */}
  </CardContent>
</Card>
```

**Components:**
- `Card` - Main container with shadow and rounded corners
- `CardHeader` - Gradient header with title and subtitle
- `CardContent` - Padded content area

## Feature Components

### SearchForm

Form for searching customers by SSN with validation.

**Props:**
```tsx
interface SearchFormProps {
  onSearch: (ssn: string) => void;  // Called when valid SSN submitted
  disabled?: boolean;                // Disable during loading
}
```

**Features:**
- Real-time validation
- SSN format enforcement (XXX-XX-XXXX)
- Error display
- Loading state

**Usage:**
```tsx
<SearchForm 
  onSearch={(ssn) => console.log(ssn)} 
  disabled={loading} 
/>
```

### CustomerInfoCard

Displays customer and credit information in organized sections.

**Props:**
```tsx
interface CustomerInfoCardProps {
  customerInfo: CustomerCreditInfo;  // Customer data entity
}
```

**Features:**
- Personal information section
- Credit information section
- Formatted currency and percentages
- Masked SSN display
- Responsive grid layout

**Usage:**
```tsx
<CustomerInfoCard customerInfo={data} />
```

### LoadingSpinner

Animated loading indicator with optional message.

**Props:**
```tsx
interface LoadingSpinnerProps {
  message?: string;  // Default: "Loading customer information..."
}
```

**Features:**
- Spinning ring animation
- Pulsing center
- Bouncing dots
- Customizable message

**Usage:**
```tsx
<LoadingSpinner message="Fetching data..." />
```

### ErrorMessage

Displays error messages with contextual styling and icons.

**Props:**
```tsx
interface ErrorMessageProps {
  error: ApiError | Error | string;  // Error to display
  onDismiss?: () => void;            // Optional dismiss callback
}
```

**Features:**
- Contextual error messages
- Appropriate icons
- Status code display
- Dismissible
- ApiError integration

**Usage:**
```tsx
<ErrorMessage 
  error={error} 
  onDismiss={() => setError(null)} 
/>
```

## Component Patterns

### Composition

Components are designed to compose together:

```tsx
<Card>
  <CardHeader title="Customer Info" />
  <CardContent>
    <Section title="Personal" icon="user">
      <InfoField label="Name" value="John Doe" />
    </Section>
  </CardContent>
</Card>
```

### Variants

Components support variants through props:

```tsx
<MetricCard variant="blue" />   // Blue background
<MetricCard variant="green" />  // Green background
<MetricCard variant="purple" /> // Purple background
```

### Conditional Rendering

Components handle different states:

```tsx
{loading && <LoadingSpinner />}
{error && <ErrorMessage error={error} />}
{data && <CustomerInfoCard customerInfo={data} />}
{!data && !loading && !error && <EmptyState />}
```

## Styling Conventions

### Tailwind Classes

- Use utility classes for styling
- Extract repeated patterns to components
- Use `className` prop for customization

### Color Palette

- `primary-*` - Blue (main brand color)
- `secondary-*` - Gray (text and backgrounds)
- `red-*` - Errors
- `green-*` - Success
- `blue-*` - Info
- `purple-*` - Accent

### Spacing

- `gap-*` - Flexbox/Grid gaps
- `space-y-*` - Vertical spacing
- `p-*` - Padding
- `m-*` - Margin

## Best Practices

### 1. Keep Components Small

Each component should have a single responsibility:

```tsx
// ✅ Good - focused component
const InfoField = ({ label, value }) => (
  <div>
    <p>{label}</p>
    <p>{value}</p>
  </div>
);

// ❌ Bad - too many responsibilities
const MegaComponent = ({ data, onSubmit, onDelete, onEdit }) => {
  // 200 lines of code
};
```

### 2. Use Composition

Build complex UIs from simple components:

```tsx
// ✅ Good - composed from primitives
<Card>
  <CardHeader title="Title" />
  <CardContent>
    <Section title="Section">
      <InfoField label="Label" value="Value" />
    </Section>
  </CardContent>
</Card>

// ❌ Bad - monolithic component
<ComplexCard data={data} />
```

### 3. Extract Constants

Move magic values to constants:

```tsx
// ✅ Good
const VARIANT_STYLES = {
  blue: 'bg-blue-50 border-blue-200',
  green: 'bg-green-50 border-green-200',
} as const;

// ❌ Bad
<div className="bg-blue-50 border-blue-200" />
```

### 4. Use TypeScript

Define clear prop interfaces:

```tsx
// ✅ Good
interface ButtonProps {
  label: string;
  onClick: () => void;
  disabled?: boolean;
}

// ❌ Bad
const Button = (props: any) => { /* ... */ }
```

### 5. Avoid Inline Styles

Use Tailwind classes instead:

```tsx
// ✅ Good
<div className="w-20 h-20 bg-blue-500" />

// ❌ Bad
<div style={{ width: '80px', height: '80px', backgroundColor: 'blue' }} />
```

## Testing

### Unit Tests (Future)

```tsx
import { render, screen } from '@testing-library/react';
import { SearchForm } from './SearchForm';

test('displays error for invalid SSN', () => {
  render(<SearchForm onSearch={jest.fn()} />);
  // Test implementation
});
```

### Component Tests (Future)

```tsx
import { render } from '@testing-library/react';
import { CustomerInfoCard } from './CustomerInfoCard';

test('displays customer information', () => {
  const data = { /* mock data */ };
  render(<CustomerInfoCard customerInfo={data} />);
  // Test implementation
});
```

## Adding New Components

### 1. Create Component File

```tsx
// components/NewComponent.tsx
'use client';

interface NewComponentProps {
  // Define props
}

export const NewComponent: React.FC<NewComponentProps> = (props) => {
  return (
    <div>
      {/* Component JSX */}
    </div>
  );
};
```

### 2. Use UI Primitives

```tsx
import { Card, CardHeader, CardContent } from '@/components/ui/Card';
import { Icon } from '@/components/ui/Icon';

export const NewComponent = () => (
  <Card>
    <CardHeader title="Title" />
    <CardContent>
      <Icon name="user" />
    </CardContent>
  </Card>
);
```

### 3. Export from Index (Optional)

```tsx
// components/index.ts
export { SearchForm } from './SearchForm';
export { CustomerInfoCard } from './CustomerInfoCard';
export { NewComponent } from './NewComponent';
```

## Maintenance

### Updating Styles

To update component styles globally:

1. **Colors**: Update Tailwind config
2. **Icons**: Update Icon component
3. **Cards**: Update Card components
4. **Spacing**: Update Tailwind utilities

### Adding Features

To add new features:

1. Create new component in `components/`
2. Use existing UI primitives
3. Follow naming conventions
4. Add to this README

### Refactoring

When refactoring:

1. Extract repeated patterns
2. Create new UI primitives
3. Update existing components
4. Document changes

## Resources

- [React Documentation](https://react.dev)
- [Next.js Documentation](https://nextjs.org/docs)
- [Tailwind CSS Documentation](https://tailwindcss.com/docs)
- [TypeScript Documentation](https://www.typescriptlang.org/docs)
