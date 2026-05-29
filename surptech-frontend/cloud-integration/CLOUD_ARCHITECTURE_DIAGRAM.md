# Cloud Integration Architecture Diagram

## System Overview

```
┌─────────────────────────────────────────────────────────────────────┐
│                         USER INTERFACE                               │
│                    (React Components + Next.js)                      │
└─────────────────────────────────────────────────────────────────────┘
                                  │
                                  ▼
┌─────────────────────────────────────────────────────────────────────┐
│                         FRONTEND LAYER                               │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐             │
│  │   Custom     │  │   Services   │  │   Mappers    │             │
│  │   Hooks      │  │   Layer      │  │              │             │
│  └──────────────┘  └──────────────┘  └──────────────┘             │
└─────────────────────────────────────────────────────────────────────┘
                                  │
                    ┌─────────────┼─────────────┐
                    ▼             ▼             ▼
┌──────────────────────┐ ┌──────────────────────┐ ┌──────────────────┐
│   EXISTING BACKEND   │ │   FIREBASE SERVICES  │ │  MONGODB CACHE   │
│                      │ │                      │ │                  │
│  ┌────────────────┐ │ │  ┌────────────────┐ │ │  ┌────────────┐ │
│  │ Data Aggregator│ │ │  │ Authentication │ │ │  │ Customer   │ │
│  │ Service        │ │ │  │                │ │ │  │ Repository │ │
│  └────────────────┘ │ │  └────────────────┘ │ │  └────────────┘ │
│                      │ │                      │ │                  │
│  ┌────────────────┐ │ │  ┌────────────────┐ │ │  ┌────────────┐ │
│  │ Customer       │ │ │  │ Firestore DB   │ │ │  │ Transaction│ │
│  │ Profile        │ │ │  │                │ │ │  │ Repository │ │
│  └────────────────┘ │ │  └────────────────┘ │ │  └────────────┘ │
│                      │ │                      │ │                  │
│  ┌────────────────┐ │ │  ┌────────────────┐ │ │                  │
│  │ Credit         │ │ │  │ Cloud Storage  │ │ │                  │
│  │ Profile        │ │ │  │                │ │ │                  │
│  └────────────────┘ │ │  └────────────────┘ │ │                  │
└──────────────────────┘ └──────────────────────┘ └──────────────────┘
                                  │
                                  ▼
                    ┌─────────────────────────┐
                    │   GCP CLOUD STORAGE     │
                    │   (File Management)     │
                    └─────────────────────────┘
```


## Data Flow Diagrams

### 1. User Authentication Flow (Firebase)

```
┌─────────┐
│  User   │
└────┬────┘
     │ 1. Enter credentials
     ▼
┌─────────────────┐
│  Login Form     │
│  (Component)    │
└────┬────────────┘
     │ 2. Call signIn()
     ▼
┌─────────────────────┐
│ useFirebaseAuth     │
│ (Custom Hook)       │
└────┬────────────────┘
     │ 3. Authenticate
     ▼
┌─────────────────────┐
│ FirebaseAuthService │
│ (Service Layer)     │
└────┬────────────────┘
     │ 4. API Call
     ▼
┌─────────────────────┐
│ Firebase Auth       │
│ (Cloud Service)     │
└────┬────────────────┘
     │ 5. Return token
     ▼
┌─────────────────────┐
│ Store user state    │
│ Redirect to app     │
└─────────────────────┘
```

### 2. Customer Data Retrieval (Hybrid Approach)

```
┌─────────┐
│  User   │
└────┬────┘
     │ 1. Search by SSN
     ▼
┌──────────────────────┐
│  SearchForm          │
│  (Component)         │
└────┬─────────────────┘
     │ 2. Fetch data
     ▼
┌──────────────────────┐
│ HybridCustomerService│
│ (Service Layer)      │
└────┬─────────────────┘
     │ 3. Check cache
     ▼
┌──────────────────────┐     ┌─────────────────────┐
│ MongoDB Repository   │────▶│ Cache Hit?          │
│ (findBySSN)          │     └──────┬──────────────┘
└──────────────────────┘            │
                                    │ Yes: Return cached
                                    │
                                    │ No: Fetch from API
                                    ▼
                         ┌──────────────────────┐
                         │ DataAggregatorClient │
                         │ (Backend API)        │
                         └──────┬───────────────┘
                                │ 4. Get fresh data
                                ▼
                         ┌──────────────────────┐
                         │ Store in MongoDB     │
                         │ (for future requests)│
                         └──────┬───────────────┘
                                │ 5. Return to user
                                ▼
                         ┌──────────────────────┐
                         │ Display in UI        │
                         └──────────────────────┘
```

### 3. File Upload Flow (GCP Storage)

```
┌─────────┐
│  User   │
└────┬────┘
     │ 1. Select file
     ▼
┌──────────────────────┐
│  FileUpload          │
│  (Component)         │
└────┬─────────────────┘
     │ 2. Upload file
     ▼
┌──────────────────────┐
│ Next.js API Route    │
│ /api/gcp/upload      │
└────┬─────────────────┘
     │ 3. Process file
     ▼
┌──────────────────────┐
│ GCPStorageService    │
│ (Service Layer)      │
└────┬─────────────────┘
     │ 4. Upload to cloud
     ▼
┌──────────────────────┐
│ GCP Cloud Storage    │
│ (Cloud Service)      │
└────┬─────────────────┘
     │ 5. Generate signed URL
     ▼
┌──────────────────────┐
│ Return URL to client │
│ Display success      │
└──────────────────────┘
```


## Component Integration Map

### Frontend Components with Cloud Services

```
┌─────────────────────────────────────────────────────────────────┐
│                        COMPONENTS                                │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌──────────────┐         ┌──────────────┐                     │
│  │ LoginPage    │────────▶│ Firebase     │                     │
│  │              │         │ Auth         │                     │
│  └──────────────┘         └──────────────┘                     │
│                                                                  │
│  ┌──────────────┐         ┌──────────────┐                     │
│  │ CustomerPage │────────▶│ MongoDB      │                     │
│  │              │         │ Cache        │                     │
│  └──────────────┘         └──────────────┘                     │
│                                                                  │
│  ┌──────────────┐         ┌──────────────┐                     │
│  │ FileUpload   │────────▶│ GCP          │                     │
│  │              │         │ Storage      │                     │
│  └──────────────┘         └──────────────┘                     │
│                                                                  │
│  ┌──────────────┐         ┌──────────────┐                     │
│  │ Dashboard    │────────▶│ Firestore    │                     │
│  │              │         │ Real-time    │                     │
│  └──────────────┘         └──────────────┘                     │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

## Service Layer Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                      SERVICE LAYER                               │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌────────────────────────────────────────────────────────┐    │
│  │  CustomerService (Existing)                            │    │
│  │  • getCustomerCreditInfo()                             │    │
│  │  • isServiceAvailable()                                │    │
│  └────────────────────────────────────────────────────────┘    │
│                                                                  │
│  ┌────────────────────────────────────────────────────────┐    │
│  │  FirebaseAuthService (New)                             │    │
│  │  • signIn()                                            │    │
│  │  • signUp()                                            │    │
│  │  • signOut()                                           │    │
│  │  • onAuthStateChange()                                 │    │
│  └────────────────────────────────────────────────────────┘    │
│                                                                  │
│  ┌────────────────────────────────────────────────────────┐    │
│  │  FirestoreService (New)                                │    │
│  │  • getDocument()                                       │    │
│  │  • getCollection()                                     │    │
│  │  • setDocument()                                       │    │
│  │  • updateDocument()                                    │    │
│  │  • deleteDocument()                                    │    │
│  └────────────────────────────────────────────────────────┘    │
│                                                                  │
│  ┌────────────────────────────────────────────────────────┐    │
│  │  GCPStorageService (New)                               │    │
│  │  • uploadFile()                                        │    │
│  │  • downloadFile()                                      │    │
│  │  • getSignedUrl()                                      │    │
│  │  • deleteFile()                                        │    │
│  │  • listFiles()                                         │    │
│  └────────────────────────────────────────────────────────┘    │
│                                                                  │
│  ┌────────────────────────────────────────────────────────┐    │
│  │  HybridCustomerService (New)                           │    │
│  │  • getCustomerInfo() - with MongoDB caching            │    │
│  │  • invalidateCache()                                   │    │
│  └────────────────────────────────────────────────────────┘    │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```


## Database Schema Examples

### MongoDB Collections

```
┌─────────────────────────────────────────────────────────────────┐
│  Collection: customers                                           │
├─────────────────────────────────────────────────────────────────┤
│  {                                                               │
│    "_id": "customer_1234567890",                                │
│    "socialSecurityNumber": "123-45-6789",                       │
│    "firstName": "John",                                          │
│    "lastName": "Doe",                                            │
│    "email": "john.doe@example.com",                             │
│    "address": "123 Main St, City, ST 12345",                    │
│    "createdAt": "2026-05-26T10:00:00Z",                         │
│    "updatedAt": "2026-05-26T10:00:00Z"                          │
│  }                                                               │
│                                                                  │
│  Indexes:                                                        │
│  • socialSecurityNumber (unique)                                │
│  • email                                                         │
│  • createdAt                                                     │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│  Collection: transactions                                        │
├─────────────────────────────────────────────────────────────────┤
│  {                                                               │
│    "_id": "txn_1234567890",                                     │
│    "customerId": "customer_1234567890",                         │
│    "amount": 150.00,                                            │
│    "type": "debit",                                             │
│    "description": "Purchase at Store",                          │
│    "timestamp": "2026-05-26T10:00:00Z"                          │
│  }                                                               │
│                                                                  │
│  Indexes:                                                        │
│  • customerId                                                    │
│  • timestamp                                                     │
│  • type                                                          │
└─────────────────────────────────────────────────────────────────┘
```

### Firestore Collections

```
┌─────────────────────────────────────────────────────────────────┐
│  Collection: user_preferences                                    │
├─────────────────────────────────────────────────────────────────┤
│  Document ID: user_uid                                           │
│  {                                                               │
│    "theme": "dark",                                             │
│    "language": "en",                                            │
│    "notifications": {                                           │
│      "email": true,                                             │
│      "push": false                                              │
│    },                                                           │
│    "lastLogin": Timestamp,                                      │
│    "updatedAt": Timestamp                                       │
│  }                                                               │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│  Collection: audit_logs                                          │
├─────────────────────────────────────────────────────────────────┤
│  Document ID: auto-generated                                     │
│  {                                                               │
│    "userId": "user_uid",                                        │
│    "action": "customer_search",                                 │
│    "details": {                                                 │
│      "ssn": "***-**-1234",                                      │
│      "timestamp": Timestamp                                     │
│    },                                                           │
│    "ipAddress": "192.168.1.1",                                  │
│    "userAgent": "Mozilla/5.0..."                                │
│  }                                                               │
└─────────────────────────────────────────────────────────────────┘
```

## Security Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                      SECURITY LAYERS                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  Layer 1: Network Security                                      │
│  ┌────────────────────────────────────────────────────────┐    │
│  │ • HTTPS/TLS encryption                                 │    │
│  │ • Firewall rules                                       │    │
│  │ • VPC configuration (GCP)                              │    │
│  │ • IP whitelisting                                      │    │
│  └────────────────────────────────────────────────────────┘    │
│                                                                  │
│  Layer 2: Authentication                                        │
│  ┌────────────────────────────────────────────────────────┐    │
│  │ • Firebase Authentication                              │    │
│  │ • JWT tokens                                           │    │
│  │ • Session management                                   │    │
│  │ • Multi-factor authentication (optional)               │    │
│  └────────────────────────────────────────────────────────┘    │
│                                                                  │
│  Layer 3: Authorization                                         │
│  ┌────────────────────────────────────────────────────────┐    │
│  │ • Role-based access control (RBAC)                     │    │
│  │ • Firebase Security Rules                              │    │
│  │ • GCP IAM policies                                     │    │
│  │ • MongoDB user roles                                   │    │
│  └────────────────────────────────────────────────────────┘    │
│                                                                  │
│  Layer 4: Data Protection                                       │
│  ┌────────────────────────────────────────────────────────┐    │
│  │ • Encryption at rest                                   │    │
│  │ • Encryption in transit                                │    │
│  │ • Data masking (SSN, etc.)                             │    │
│  │ • Secure credential storage                            │    │
│  └────────────────────────────────────────────────────────┘    │
│                                                                  │
│  Layer 5: Application Security                                  │
│  ┌────────────────────────────────────────────────────────┐    │
│  │ • Input validation                                     │    │
│  │ • SQL injection prevention                             │    │
│  │ • XSS protection                                       │    │
│  │ • CSRF tokens                                          │    │
│  │ • Rate limiting                                        │    │
│  └────────────────────────────────────────────────────────┘    │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```


## Deployment Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                    PRODUCTION ENVIRONMENT                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌────────────────────────────────────────────────────────┐    │
│  │  Load Balancer / CDN                                   │    │
│  │  (Firebase Hosting / Vercel / Cloudflare)             │    │
│  └──────────────────┬─────────────────────────────────────┘    │
│                     │                                           │
│                     ▼                                           │
│  ┌────────────────────────────────────────────────────────┐    │
│  │  Next.js Application (Docker Container)               │    │
│  │  • Server-side rendering                               │    │
│  │  • API routes                                          │    │
│  │  • Static assets                                       │    │
│  └──────────────────┬─────────────────────────────────────┘    │
│                     │                                           │
│         ┌───────────┼───────────┬───────────┐                  │
│         ▼           ▼           ▼           ▼                  │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐         │
│  │ Firebase │ │   GCP    │ │ MongoDB  │ │ Backend  │         │
│  │          │ │          │ │          │ │   APIs   │         │
│  │ • Auth   │ │ • Storage│ │ • Cache  │ │          │         │
│  │ • Store  │ │ • BigQ   │ │ • Data   │ │ • REST   │         │
│  │ • Analyt │ │ • Logs   │ │          │ │ • Health │         │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘         │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

## Monitoring & Observability

```
┌─────────────────────────────────────────────────────────────────┐
│                    MONITORING STACK                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  Application Metrics                                            │
│  ┌────────────────────────────────────────────────────────┐    │
│  │ • Request rate                                         │    │
│  │ • Response time                                        │    │
│  │ • Error rate                                           │    │
│  │ • Success rate                                         │    │
│  └────────────────────────────────────────────────────────┘    │
│                     │                                           │
│                     ▼                                           │
│  ┌────────────────────────────────────────────────────────┐    │
│  │  Firebase Analytics                                    │    │
│  │  • User behavior                                       │    │
│  │  • Page views                                          │    │
│  │  • Custom events                                       │    │
│  └────────────────────────────────────────────────────────┘    │
│                                                                  │
│  Infrastructure Metrics                                         │
│  ┌────────────────────────────────────────────────────────┐    │
│  │ • CPU usage                                            │    │
│  │ • Memory usage                                         │    │
│  │ • Network I/O                                          │    │
│  │ • Disk usage                                           │    │
│  └────────────────────────────────────────────────────────┘    │
│                     │                                           │
│                     ▼                                           │
│  ┌────────────────────────────────────────────────────────┐    │
│  │  Google Cloud Monitoring                               │    │
│  │  • Resource metrics                                    │    │
│  │  • Custom metrics                                      │    │
│  │  • Alerts                                              │    │
│  └────────────────────────────────────────────────────────┘    │
│                                                                  │
│  Logs & Traces                                                  │
│  ┌────────────────────────────────────────────────────────┐    │
│  │ • Application logs                                     │    │
│  │ • Error logs                                           │    │
│  │ • Audit logs                                           │    │
│  │ • Distributed traces                                   │    │
│  └────────────────────────────────────────────────────────┘    │
│                     │                                           │
│                     ▼                                           │
│  ┌────────────────────────────────────────────────────────┐    │
│  │  Cloud Logging / Firestore                             │    │
│  │  • Centralized logging                                 │    │
│  │  • Log analysis                                        │    │
│  │  • Log retention                                       │    │
│  └────────────────────────────────────────────────────────┘    │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

## Cost Breakdown Visualization

```
┌─────────────────────────────────────────────────────────────────┐
│                    MONTHLY COST ESTIMATE                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  Firebase                                                        │
│  ┌────────────────────────────────────────────────────────┐    │
│  │ Authentication:        $0 - $10                        │    │
│  │ Firestore:            $0 - $20                         │    │
│  │ Storage:              $0 - $10                         │    │
│  │ Hosting:              $0 - $10                         │    │
│  │ ─────────────────────────────────────────────────      │    │
│  │ Subtotal:             $0 - $50                         │    │
│  └────────────────────────────────────────────────────────┘    │
│                                                                  │
│  Google Cloud Platform                                          │
│  ┌────────────────────────────────────────────────────────┐    │
│  │ Cloud Storage:        $10 - $50                        │    │
│  │ Cloud Functions:      $0 - $20                         │    │
│  │ Cloud Logging:        $0 - $10                         │    │
│  │ BigQuery:             $0 - $20                         │    │
│  │ ─────────────────────────────────────────────────      │    │
│  │ Subtotal:             $10 - $100                       │    │
│  └────────────────────────────────────────────────────────┘    │
│                                                                  │
│  MongoDB                                                         │
│  ┌────────────────────────────────────────────────────────┐    │
│  │ Atlas M10 (Shared):   $0 - $57                         │    │
│  │ Self-hosted:          $0 (infrastructure only)         │    │
│  │ ─────────────────────────────────────────────────      │    │
│  │ Subtotal:             $0 - $57                         │    │
│  └────────────────────────────────────────────────────────┘    │
│                                                                  │
│  ┌────────────────────────────────────────────────────────┐    │
│  │ TOTAL ESTIMATED COST: $10 - $207 / month              │    │
│  └────────────────────────────────────────────────────────┘    │
│                                                                  │
│  Note: Costs scale with usage. Free tiers available for        │
│  development and low-traffic applications.                      │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

**Document Purpose**: Visual reference for cloud integration architecture  
**Last Updated**: 2026-05-26  
**Related Documents**: 
- [Integration Baseline](./INTEGRATION_BASELINE.md)
- [Quick Start Guide](./CLOUD_INTEGRATION_QUICKSTART.md)
- [Integration Summary](./CLOUD_INTEGRATION_SUMMARY.md)
