# surptech-frontend

Next.js web application for the SurpTech Banking System. Provides a customer information lookup portal where users can search by Social Security Number and view aggregated personal and credit data.

## Purpose

Serves as the user-facing layer of the banking system. It connects to the `data-aggregator` backend service and presents the combined customer and credit profile in a clean, responsive UI. A server-side proxy route handles all backend communication to avoid CORS issues.

## Prerequisites

- Node.js 18+
- npm 9+
- `data-aggregator` service running on port `5555`

## Getting Started

```bash
cd surptech-frontend
npm install
npm run dev
```

Open `http://localhost:3000` in your browser.

## Available Scripts

| Script | Command | Description |
|---|---|---|
| Development | `npm run dev` | Starts Next.js dev server on port 3000 with hot reload |
| Build | `npm run build` | Compiles the production build |
| Start | `npm start` | Runs the compiled production build on port 3000 |
| Lint | `npm run lint` | Runs ESLint via `next lint` |
| Type check | `npm run type-check` | Runs `tsc --noEmit` without emitting files |

## Environment Variables

Copy `.env.example` to `.env.local` and adjust as needed.

```env
DATA_AGGREGATOR_HOST=localhost
DATA_AGGREGATOR_PORT=5555
```

These are read server-side by the Next.js API proxy route and by `next.config.js`. No `NEXT_PUBLIC_` prefix is needed since they are never sent to the browser.

## How It Works

### CORS Proxy

Browser requests cannot call the backend directly due to CORS. Instead, all API calls go through a Next.js API route that proxies them server-side:

```
Browser → GET /api/customer/info?socialSecurityNumber=XXX
            ↓ (server-side)
          Next.js API route (app/api/customer/info/route.ts)
            ↓ HTTP
          data-aggregator → GET http://localhost:5555/data-aggregator/customer/info?socialSecurityNumber=XXX
```

### Data Flow

1. User enters an SSN in `SearchForm`
2. `page.tsx` calls `useCustomerCreditInfo().fetchCustomerInfo(ssn)`
3. The hook calls `CustomerService.getCustomerCreditInfo(ssn)`
4. The service calls `DataAggregatorClient.getCustomerCreditInfo(ssn)`
5. The client sends `GET /api/customer/info?socialSecurityNumber=...`
6. The Next.js proxy forwards it to the backend and returns the response
7. `CustomerCreditInfoMapper` converts snake_case JSON fields to camelCase TypeScript entities
8. `CustomerInfoCard` renders the result

## Project Structure

```
surptech-frontend/
├── app/
│   ├── api/
│   │   └── customer/info/route.ts   # Server-side CORS proxy to data-aggregator
│   ├── layout.tsx                   # Root layout: header, footer, Inter font
│   ├── page.tsx                     # Home page: search form + results
│   └── globals.css                  # Global Tailwind CSS styles
├── components/
│   ├── SearchForm.tsx               # SSN input with client-side validation
│   ├── CustomerInfoCard.tsx         # Displays aggregated customer + credit data
│   ├── LoadingSpinner.tsx           # Loading indicator
│   ├── ErrorMessage.tsx             # Error display with dismiss button
│   └── ui/
│       ├── Card.tsx                 # Reusable card container
│       └── Icon.tsx                 # Icon component
├── client/
│   └── DataAggregatorClient.ts      # HTTP client (30s timeout, error handling)
├── services/
│   └── CustomerService.ts           # Orchestrates fetch + mapping
├── hooks/
│   └── useCustomerCreditInfo.ts     # React hook: data, loading, error, fetchCustomerInfo, reset
├── mapper/
│   └── CustomerCreditInfoMapper.ts  # snake_case response → camelCase entity
├── domain/
│   ├── entity/CustomerCreditInfo.ts         # camelCase TypeScript entity
│   ├── response/CustomerCreditInfoResponse.ts # snake_case API response DTO
│   ├── request/CustomerInfoRequest.ts        # Request type
│   └── error/ApiError.ts                     # Custom error class with status code
├── utils/
│   ├── validation.utils.ts          # SSN format validation (regex: ^\d{3}-\d{2}-\d{4}$)
│   └── format.utils.ts              # Currency, percentage, name, SSN masking formatters
├── config/
│   └── api.config.ts                # API base URL and endpoint definitions
├── cloud-integration/               # Future cloud integration skeleton (not active)
├── next.config.js
├── tailwind.config.ts
├── tsconfig.json
└── package.json
```

## Key Implementation Details

**SSN validation** mirrors the backend: regex `^\d{3}-\d{2}-\d{4}$`. The `formatSocialSecurityNumber` utility also auto-formats a 9-digit string into `XXX-XX-XXXX` format.

**SSN masking** — SSNs are displayed as `***-**-XXXX` in the UI. They are never stored in the browser.

**Data conventions** — The backend returns snake_case JSON (`social_security_number`, `full_credit_balance`). The mapper converts these to camelCase TypeScript properties (`socialSecurityNumber`, `fullCreditBalance`) for use in components.

**Interest rate display** — The `formatPercentage` utility multiplies the raw rate by 100 before displaying (e.g., `3.5` → `350.00%`). Verify the backend's unit convention if this looks wrong.

**Partial responses** — If the backend returns a customer with no credit data (or vice versa), the card renders the available fields and omits the missing ones.

**Request timeout** — The proxy route and the `DataAggregatorClient` both enforce a 30-second timeout. A timeout returns HTTP 408.

## Technology Stack

| Technology | Version |
|---|---|
| Next.js | 14 |
| React | 18 |
| TypeScript | 5 |
| Tailwind CSS | 3 |
| Node.js | 18+ |
| npm | 9+ |

## Troubleshooting

**"Unable to connect to the server"**
- Confirm `data-aggregator` is running: `curl http://localhost:5555/data-aggregator/management/health`
- Check that `DATA_AGGREGATOR_HOST` and `DATA_AGGREGATOR_PORT` in `.env.local` match where the service is running
- Restart the dev server after changing environment variables

**"Customer not found" (404)**
- Verify the SSN exists in the database. Seed data: `123-45-6789` (James Smith), `987-65-4321` (John Travolta)
- Confirm the SSN format is `XXX-XX-XXXX`

**Type errors**
- Run `npm run type-check` to see all TypeScript issues
- Ensure `npm install` has been run and `node_modules` is present

## Additional Documentation

- [ARCHITECTURE.md](ARCHITECTURE.md) — Detailed layered architecture description
- [CORS_SETUP.md](CORS_SETUP.md) — CORS configuration options and alternatives
- [QUICKSTART.md](QUICKSTART.md) — Get running in 5 minutes
- [CONTRIBUTING.md](CONTRIBUTING.md) — Development guidelines
- [TROUBLESHOOTING.md](TROUBLESHOOTING.md) — Extended troubleshooting guide
- [cloud-integration/](cloud-integration/) — Future Firebase / GCP / MongoDB integration skeleton (not yet wired into the app)

## License

Copyright © 2026 SurpTech
