# 🚀 Cloud Integration Quick Reference Card

**Print this page and keep it handy during implementation!**

---

## 📚 Documentation Map

| Need | Document | Time |
|------|----------|------|
| **Quick Setup** | [CLOUD_INTEGRATION_QUICKSTART.md](./CLOUD_INTEGRATION_QUICKSTART.md) | 5 min |
| **Overview** | [CLOUD_INTEGRATION_SUMMARY.md](./CLOUD_INTEGRATION_SUMMARY.md) | 15 min |
| **Code Examples** | [INTEGRATION_BASELINE.md](./INTEGRATION_BASELINE.md) | Reference |
| **Architecture** | [CLOUD_ARCHITECTURE_DIAGRAM.md](./CLOUD_ARCHITECTURE_DIAGRAM.md) | 20 min |
| **Task List** | [IMPLEMENTATION_CHECKLIST.md](./IMPLEMENTATION_CHECKLIST.md) | Ongoing |
| **Navigation** | [CLOUD_INTEGRATION_INDEX.md](./CLOUD_INTEGRATION_INDEX.md) | 10 min |
| **Next Steps** | [CLOUD_INTEGRATION_NEXT_STEPS.md](./CLOUD_INTEGRATION_NEXT_STEPS.md) | 15 min |

---

## ⚡ Quick Commands

### Installation
```bash
npm install firebase @google-cloud/storage mongodb
```

### Environment Setup
```bash
# Create .env.local file with:
NEXT_PUBLIC_FIREBASE_API_KEY=your_key
NEXT_PUBLIC_FIREBASE_PROJECT_ID=your_project
GCP_PROJECT_ID=your_project
GCP_CREDENTIALS_PATH=./credentials.json
MONGODB_URI=mongodb+srv://...
```

### Development
```bash
npm run dev          # Start dev server
npm run build        # Build for production
npm run test         # Run tests
npm run lint         # Lint code
```

---

## 🔥 Firebase Quick Reference

### Configuration
```typescript
// config/firebase.config.ts
export const firebaseConfig = {
  apiKey: process.env.NEXT_PUBLIC_FIREBASE_API_KEY,
  projectId: process.env.NEXT_PUBLIC_FIREBASE_PROJECT_ID,
  // ... more config
};
```

### Authentication
```typescript
// Sign up
await createUserWithEmailAndPassword(auth, email, password);

// Sign in
await signInWithEmailAndPassword(auth, email, password);

// Sign out
await signOut(auth);
```

### Firestore
```typescript
// Add document
await addDoc(collection(db, 'users'), data);

// Get document
const doc = await getDoc(doc(db, 'users', id));

// Update document
await updateDoc(doc(db, 'users', id), data);

// Delete document
await deleteDoc(doc(db, 'users', id));
```

### Storage
```typescript
// Upload file
const storageRef = ref(storage, `files/${file.name}`);
await uploadBytes(storageRef, file);

// Get download URL
const url = await getDownloadURL(storageRef);
```

---

## ☁️ GCP Quick Reference

### Configuration
```typescript
// config/gcp.config.ts
export const gcpConfig = {
  projectId: process.env.GCP_PROJECT_ID!,
  keyFilename: process.env.GCP_CREDENTIALS_PATH!,
  bucketName: process.env.GCP_BUCKET_NAME!,
};
```

### Cloud Storage
```typescript
// Upload file
await bucket.upload(filePath, {
  destination: destinationPath,
});

// Download file
await bucket.file(fileName).download({
  destination: localPath,
});

// Generate signed URL
const [url] = await bucket.file(fileName).getSignedUrl({
  action: 'read',
  expires: Date.now() + 15 * 60 * 1000, // 15 minutes
});
```

---

## 🍃 MongoDB Quick Reference

### Configuration
```typescript
// config/mongodb.config.ts
export const mongoConfig = {
  uri: process.env.MONGODB_URI!,
  dbName: process.env.MONGODB_DB_NAME || 'surptech',
};
```

### Connection
```typescript
// Get client
const client = await MongoDBClient.getInstance();
const db = client.db(mongoConfig.dbName);
```

### CRUD Operations
```typescript
// Create
await collection.insertOne(document);

// Read
const doc = await collection.findOne({ _id: id });

// Update
await collection.updateOne({ _id: id }, { $set: data });

// Delete
await collection.deleteOne({ _id: id });
```

---

## 📅 10-Week Timeline

| Weeks | Phase | Focus |
|-------|-------|-------|
| 1-2 | Foundation | Setup & Configuration |
| 3-4 | Firebase | Auth, Firestore, Storage |
| 5-6 | GCP | Cloud Storage, APIs |
| 7-8 | MongoDB | Repositories, Caching |
| 9-10 | Integration | Testing & Deployment |

---

## 💰 Cost Estimates

| Service | Development | Production |
|---------|-------------|------------|
| Firebase | $0 (free tier) | $0-50/month |
| GCP | $0 ($300 credit) | $10-100/month |
| MongoDB | $0 (free tier) | $0-57/month |
| **Total** | **$0** | **$10-207/month** |

---

## 🔒 Security Checklist

### Before Production
- [ ] All credentials in environment variables
- [ ] No secrets in code repository
- [ ] `.env.local` in `.gitignore`
- [ ] Firebase security rules configured
- [ ] GCP IAM policies set
- [ ] MongoDB authentication enabled
- [ ] HTTPS enforced
- [ ] Rate limiting enabled
- [ ] Input validation implemented
- [ ] CORS properly configured

---

## 🧪 Testing Commands

```bash
# Unit tests
npm run test

# Integration tests
npm run test:integration

# E2E tests
npm run test:e2e

# Coverage
npm run test:coverage

# Watch mode
npm run test:watch
```

---

## 🚀 Deployment Commands

```bash
# Build
npm run build

# Docker build
docker build -t surptech-frontend .

# Docker run
docker run -p 3000:3000 surptech-frontend

# Docker compose
docker-compose up -d
```

---

## 📊 Success Metrics

### Technical
- [ ] 95%+ uptime
- [ ] API response < 500ms
- [ ] 80%+ test coverage
- [ ] Zero critical vulnerabilities

### Business
- [ ] Authentication working
- [ ] File uploads functional
- [ ] Caching reduces load 30%+
- [ ] Costs within budget

---

## 🆘 Troubleshooting

### Firebase Connection Issues
```bash
# Check credentials
echo $NEXT_PUBLIC_FIREBASE_API_KEY

# Test connection
npm run test:firebase
```

### GCP Connection Issues
```bash
# Check service account
gcloud auth list

# Test credentials
gcloud auth application-default print-access-token
```

### MongoDB Connection Issues
```bash
# Test connection
mongosh "$MONGODB_URI"

# Check network access
# Go to MongoDB Atlas → Network Access
```

---

## 📞 Support Links

| Service | Documentation | Console |
|---------|--------------|---------|
| **Firebase** | [docs](https://firebase.google.com/docs) | [console](https://console.firebase.google.com) |
| **GCP** | [docs](https://cloud.google.com/docs) | [console](https://console.cloud.google.com) |
| **MongoDB** | [docs](https://docs.mongodb.com) | [atlas](https://cloud.mongodb.com) |

---

## 🎯 Common Tasks

### Add New User
```typescript
import { createUser } from '@/lib/firebase/auth';
await createUser(email, password);
```

### Upload File
```typescript
import { uploadFile } from '@/lib/firebase/storage';
const url = await uploadFile(file, 'uploads/');
```

### Cache Data
```typescript
import { CustomerRepository } from '@/lib/mongodb/repositories';
const repo = new CustomerRepository();
await repo.create(customerData);
```

### Get Customer
```typescript
import { HybridCustomerService } from '@/services';
const service = new HybridCustomerService();
const customer = await service.getCustomer(id);
```

---

## 🔄 Git Workflow

```bash
# Create feature branch
git checkout -b feature/firebase-auth

# Commit changes
git add .
git commit -m "feat: add Firebase authentication"

# Push to remote
git push origin feature/firebase-auth

# Create pull request
# Review and merge
```

---

## 📝 Code Review Checklist

- [ ] TypeScript types defined
- [ ] Error handling implemented
- [ ] Tests written and passing
- [ ] Security best practices followed
- [ ] Performance optimized
- [ ] Documentation updated
- [ ] No console.logs in production
- [ ] Environment variables used
- [ ] Code follows project conventions

---

## 🎓 Learning Resources

### Firebase
- [Firebase Docs](https://firebase.google.com/docs)
- [Firebase YouTube](https://www.youtube.com/firebase)
- [Firebase Codelab](https://firebase.google.com/codelabs)

### GCP
- [GCP Docs](https://cloud.google.com/docs)
- [GCP Training](https://cloud.google.com/training)
- [GCP YouTube](https://www.youtube.com/googlecloudplatform)

### MongoDB
- [MongoDB Docs](https://docs.mongodb.com)
- [MongoDB University](https://university.mongodb.com) (Free!)
- [MongoDB YouTube](https://www.youtube.com/mongodb)

---

## ⚙️ Environment Variables Template

```bash
# Firebase
NEXT_PUBLIC_FIREBASE_API_KEY=
NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN=
NEXT_PUBLIC_FIREBASE_PROJECT_ID=
NEXT_PUBLIC_FIREBASE_STORAGE_BUCKET=
NEXT_PUBLIC_FIREBASE_MESSAGING_SENDER_ID=
NEXT_PUBLIC_FIREBASE_APP_ID=

# GCP
GCP_PROJECT_ID=
GCP_CREDENTIALS_PATH=
GCP_BUCKET_NAME=

# MongoDB
MONGODB_URI=
MONGODB_DB_NAME=

# Backend APIs (existing)
NEXT_PUBLIC_DATA_AGGREGATOR_URL=
NEXT_PUBLIC_CUSTOMER_PROFILE_URL=
NEXT_PUBLIC_CREDIT_PROFILE_URL=
```

---

## 🏗️ Project Structure

```
surptech-frontend/
├── app/
│   ├── api/                    # API routes
│   │   ├── auth/              # Auth endpoints
│   │   ├── gcp/               # GCP endpoints
│   │   └── mongodb/           # MongoDB endpoints
│   └── (routes)/              # Page routes
├── components/                 # React components
├── config/                     # Configuration files
│   ├── firebase.config.ts
│   ├── gcp.config.ts
│   └── mongodb.config.ts
├── hooks/                      # Custom React hooks
│   ├── useFirebaseAuth.ts
│   └── useFirestore.ts
├── lib/                        # Library code
│   ├── firebase/              # Firebase services
│   ├── gcp/                   # GCP services
│   └── mongodb/               # MongoDB services
├── services/                   # Business logic
│   └── HybridCustomerService.ts
└── types/                      # TypeScript types
```

---

## 🎯 Phase 1 Quick Start (Week 1-2)

### Day 1-2: Firebase Setup
- [ ] Create Firebase project
- [ ] Add Firebase config to `.env.local`
- [ ] Install Firebase SDK
- [ ] Test connection

### Day 3-4: GCP Setup
- [ ] Create GCP project
- [ ] Create service account
- [ ] Download credentials
- [ ] Test connection

### Day 5-6: MongoDB Setup
- [ ] Create MongoDB Atlas cluster
- [ ] Get connection string
- [ ] Add to `.env.local`
- [ ] Test connection

### Day 7-10: Configuration
- [ ] Create config files
- [ ] Set up initialization
- [ ] Write connection tests
- [ ] Document setup

---

## 💡 Pro Tips

### Performance
- Use connection pooling for MongoDB
- Implement caching strategies
- Optimize Firestore queries with indexes
- Use CDN for static files

### Security
- Never commit `.env.local`
- Use server-side API routes for sensitive operations
- Implement rate limiting
- Validate all user inputs

### Development
- Use TypeScript strict mode
- Write tests as you code
- Use feature flags for gradual rollout
- Keep documentation updated

### Cost Optimization
- Use free tiers during development
- Set up billing alerts
- Monitor usage dashboards
- Optimize queries and caching

---

## 📱 Quick Contact

| Role | Responsibility |
|------|----------------|
| **Technical Lead** | Architecture decisions |
| **DevOps** | Infrastructure setup |
| **Security** | Security reviews |
| **QA** | Testing strategies |

---

## ✅ Daily Checklist

### Morning
- [ ] Pull latest code
- [ ] Check CI/CD status
- [ ] Review open PRs
- [ ] Check monitoring dashboards

### During Development
- [ ] Write tests
- [ ] Follow coding standards
- [ ] Document changes
- [ ] Commit regularly

### Before Pushing
- [ ] Run tests locally
- [ ] Run linter
- [ ] Review your changes
- [ ] Update documentation

### End of Day
- [ ] Push your code
- [ ] Update task status
- [ ] Document blockers
- [ ] Plan tomorrow

---

**Version**: 1.0  
**Last Updated**: May 26, 2026  
**Print Date**: ___________

**Keep this handy during implementation! 📌**
