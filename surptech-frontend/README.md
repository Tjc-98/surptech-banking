# SurpTech Banking Frontend

A modern, responsive web application for viewing customer banking information in the SurpTech Banking System. Built with React.js, Next.js, and TypeScript, following best practices and the architectural patterns established in the backend services.

## 🏗️ Architecture

The frontend follows a layered architecture similar to the backend microservices:

```
surptech-frontend/
├── app/                          # Next.js App Router (pages and routing)
│   ├── layout.tsx               # Root layout with header/footer
│   ├── page.tsx                 # Home page (main entry point)
│   └── globals.css              # Global styles
├── components/                   # React UI components
│   ├── CustomerInfoCard.tsx     # Displays customer information
│   ├── SearchForm.tsx           # SSN search form
│   ├── LoadingSpinner.tsx       # Loading indicator
│   └── ErrorMessage.tsx         # Error display component
├── domain/                       # Domain models and types
│   ├── entity/                  # Domain entities (camelCase)
│   │   └── CustomerCreditInfo.ts
│   ├── request/                 # Request DTOs (snake_case)
│   │   └── CustomerInfoRequest.ts
│   ├── response/                # Response DTOs (snake_case)
│   │   └── CustomerCreditInfoResponse.ts
│   └── error/                   # Error types
│       └── ApiError.ts
├── client/                       # HTTP API clients
│   └── DataAggregatorClient.ts  # Client for data-aggregator service
├── services/                     # Business logic layer
│   └── CustomerService.ts       # Customer operations service
├── mapper/                       # Data transformation layer
│   └── CustomerCreditInfoMapper.ts
├── hooks/                        # Custom React hooks
│   └── useCustomerCreditInfo.ts # Hook for fetching customer data
├── utils/                        # Utility functions
│   ├── validation.utils.ts      # Input validation
│   └── format.utils.ts          # Data formatting
├── config/                       # Configuration
│   └── api.config.ts            # API endpoints and settings
└── package.json                  # Dependencies and scripts
```

## 🎯 Features

- **Customer Information Lookup**: Search for customers by Social Security Number
- **Aggregated Data Display**: View combined customer profile and credit information
- **Real-time Validation**: Client-side SSN format validation
- **Error Handling**: User-friendly error messages with contextual information
- **Responsive Design**: Works seamlessly on desktop, tablet, and mobile devices
- **Loading States**: Visual feedback during data fetching
- **Modern UI**: Clean, professional interface with Tailwind CSS

## 🚀 Getting Started

### Prerequisites

- **Node.js**: >= 18.0.0
- **npm**: >= 9.0.0
- **Backend Services**: Data Aggregator service running on port 5555

### Installation

1. Navigate to the frontend directory:
```bash
cd surptech-frontend
```

2. Install dependencies:
```bash
npm install
```

3. Configure environment variables (optional):
```bash
cp .env.example .env
```

Edit `.env` to customize the data aggregator connection:
```env
DATA_AGGREGATOR_HOST=localhost
DATA_AGGREGATOR_PORT=5555
```

### Running the Application

#### Development Mode
```bash
npm run dev
```

The application will start on `http://localhost:3000`

#### Production Build
```bash
npm run build
npm start
```

#### Type Checking
```bash
npm run type-check
```

#### Linting
```bash
npm run lint
```

## 🔌 API Integration

The frontend connects to the **Data Aggregator Service** (port 5555) to retrieve customer information.

### CORS Handling

To avoid CORS (Cross-Origin Resource Sharing) issues, the frontend uses **Next.js API routes as a proxy**:

```
Browser → Next.js API (/api/*) → Backend Service (port 5555)
```

This means browser requests go to `http://localhost:3000/api/customer/info`, which Next.js forwards to the backend. See [CORS_SETUP.md](CORS_SETUP.md) for details and alternative configurations.

### Endpoint Used

**Frontend calls**: `GET /api/customer/info?socialSecurityNumber={ssn}`  
**Backend endpoint**: `GET /data-aggregator/customer/info?socialSecurityNumber={ssn}`

**Response:**
```json
{
  "social_security_number": "123-45-6789",
  "first_name": "John",
  "last_name": "Doe",
  "address": "123 Main St, City, ST 12345",
  "full_credit_balance": 5000.00,
  "spend_balance": 3000.00,
  "interest_rate": 0.15
}
```

## 🎨 Design Patterns

### Component Composition
- **Presentational Components**: Pure UI components (CustomerInfoCard, LoadingSpinner)
- **Container Components**: Components with business logic (page.tsx)
- **Custom Hooks**: Reusable stateful logic (useCustomerCreditInfo)

### Data Flow
1. User enters SSN in `SearchForm`
2. `page.tsx` calls `useCustomerCreditInfo` hook
3. Hook invokes `CustomerService.getCustomerCreditInfo()`
4. Service calls `DataAggregatorClient.getCustomerCreditInfo()`
5. Client makes HTTP request to backend
6. Response is mapped from snake_case DTO to camelCase entity
7. Entity is returned to component for display

### Error Handling
- **ApiError**: Custom error class with status codes
- **User-friendly messages**: Contextual error messages based on error type
- **Graceful degradation**: Clear feedback when services are unavailable

### Validation
- **Client-side validation**: SSN format validation before API calls
- **Format enforcement**: Automatic SSN formatting (XXX-XX-XXXX)
- **Real-time feedback**: Immediate validation error display

## 🛠️ Technologies

- **Next.js 14**: React framework with App Router
- **React 18**: UI library with hooks and modern patterns
- **TypeScript**: Type-safe development
- **Tailwind CSS**: Utility-first CSS framework
- **ESLint**: Code quality and consistency

## 📝 Key Differences from Backend Architecture

While the frontend follows similar architectural patterns to the backend, there are important differences:

### Frontend-Specific Layers
- **components/**: UI rendering layer (no backend equivalent)
- **hooks/**: React state management and side effects
- **app/**: Next.js routing (replaces Spring controllers)

### Shared Patterns
- **client/**: HTTP clients (similar to backend RestClient)
- **domain/**: Type definitions (similar to backend DTOs)
- **services/**: Business logic orchestration
- **mapper/**: Data transformation (snake_case ↔ camelCase)
- **utils/**: Validation and formatting utilities

### Data Conventions
- **Backend**: Uses snake_case for JSON fields
- **Frontend**: 
  - DTOs (request/response): snake_case (matches API)
  - Entities: camelCase (TypeScript convention)
  - Mapper layer handles conversion

## 🔒 Security Considerations

- **No sensitive data storage**: SSNs are not persisted in browser
- **Display masking**: SSNs are masked in UI (***-**-1234)
- **HTTPS ready**: Configure for production with HTTPS
- **No authentication**: Currently not implemented (as per requirements)

## 🚦 Running with Backend Services

Ensure all backend services are running:

```bash
# Terminal 1: Customer Profile Service
cd customer-profile
mvn spring-boot:run

# Terminal 2: Credit Profile Service
cd credit-profile
mvn spring-boot:run

# Terminal 3: Data Aggregator Service
cd data-aggregator
mvn spring-boot:run

# Terminal 4: Frontend
cd surptech-frontend
npm run dev
```

Access the application at `http://localhost:3000`

## 📊 Example Usage

1. Open `http://localhost:3000` in your browser
2. Enter a Social Security Number (e.g., `123-45-6789`)
3. Click "Search Customer"
4. View the aggregated customer and credit information

## 🐛 Troubleshooting

### "Unable to connect to the server"
- Ensure the data-aggregator service is running on port 5555
- Verify the backend is accessible: `curl http://localhost:5555/data-aggregator/management/health`
- Check Next.js terminal for proxy errors
- Restart the Next.js dev server: `npm run dev`
- See [CORS_SETUP.md](CORS_SETUP.md) for CORS configuration options

### "Customer not found"
- Verify the SSN exists in the database
- Check the backend service logs
- Ensure the SSN format is correct (XXX-XX-XXXX)

### Type errors during development
- Run `npm run type-check` to identify TypeScript issues
- Ensure all dependencies are installed
- Check that `tsconfig.json` paths are correct

## 📖 Additional Documentation

**Project Documentation:**
- [CORS Setup Guide](CORS_SETUP.md) - CORS configuration and troubleshooting
- [Architecture Details](ARCHITECTURE.md) - Detailed architecture documentation
- [Quick Start Guide](QUICKSTART.md) - Get started in 5 minutes
- [Contributing Guide](CONTRIBUTING.md) - Development guidelines

**Cloud Integration Documentation** (all docs live in `cloud-integration/`):
- 🎯 [START HERE](cloud-integration/START_HERE.md) - **Your entry point to cloud integration** (start here!)
- ⚡ [Quick Start Guide](cloud-integration/CLOUD_INTEGRATION_QUICKSTART.md) - Set up cloud services in 5 minutes
- 💻 [Integration Baseline](cloud-integration/INTEGRATION_BASELINE.md) - Complete technical guide with all code examples
- 📊 [Integration Summary](cloud-integration/CLOUD_INTEGRATION_SUMMARY.md) - Executive overview, timeline, and costs
- 🏛️ [Architecture Diagrams](cloud-integration/CLOUD_ARCHITECTURE_DIAGRAM.md) - Visual system architecture
- ✅ [Implementation Checklist](cloud-integration/IMPLEMENTATION_CHECKLIST.md) - Phase-by-phase task tracking
- 🗺️ [Implementation Roadmap](cloud-integration/CLOUD_INTEGRATION_ROADMAP.md) - Visual 10-week timeline
- 📖 [Documentation Index](cloud-integration/CLOUD_INTEGRATION_INDEX.md) - Master navigation guide
- 📄 [Delivery Summary](cloud-integration/CLOUD_INTEGRATION_DELIVERY_SUMMARY.md) - What's been delivered
- 📋 [Quick Reference Card](cloud-integration/CLOUD_INTEGRATION_QUICK_REFERENCE.md) - Print and keep handy
- 🚀 [Next Steps Guide](cloud-integration/CLOUD_INTEGRATION_NEXT_STEPS.md) - Action plan and decisions

**External Documentation:**
- [Next.js Documentation](https://nextjs.org/docs)
- [React Documentation](https://react.dev)
- [Tailwind CSS Documentation](https://tailwindcss.com/docs)
- [TypeScript Documentation](https://www.typescriptlang.org/docs)
- [Firebase Documentation](https://firebase.google.com/docs)
- [Google Cloud Platform Documentation](https://cloud.google.com/docs)
- [MongoDB Documentation](https://www.mongodb.com/docs)

## 📝 License

Copyright © 2024 SurpTech
