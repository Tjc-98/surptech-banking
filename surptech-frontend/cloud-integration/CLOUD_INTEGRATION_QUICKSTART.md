# Cloud Integration Quick Start Guide

## 🚀 Quick Setup (5 Minutes)

### 1. Install Dependencies

```bash
# Firebase
npm install firebase

# Google Cloud Platform
npm install @google-cloud/storage @google-cloud/firestore

# MongoDB
npm install mongodb

# Optional: Caching and State Management
npm install @tanstack/react-query redis
```

### 2. Create Environment File

Create `.env.local` in the project root:

```bash
# Firebase
NEXT_PUBLIC_FIREBASE_API_KEY=your_api_key_here
NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN=your_project.firebaseapp.com
NEXT_PUBLIC_FIREBASE_PROJECT_ID=your_project_id
NEXT_PUBLIC_FIREBASE_STORAGE_BUCKET=your_project.appspot.com
NEXT_PUBLIC_FIREBASE_MESSAGING_SENDER_ID=your_sender_id
NEXT_PUBLIC_FIREBASE_APP_ID=your_app_id

# GCP (Server-side only)
GCP_PROJECT_ID=your_gcp_project
GCP_CREDENTIALS_PATH=./config/credentials/gcp-service-account.json
GCP_STORAGE_BUCKET=your_bucket_name

# MongoDB (Server-side only)
MONGODB_URI=mongodb://localhost:27017
MONGODB_DATABASE=surptech_banking

# Feature Flags
NEXT_PUBLIC_USE_FIREBASE_AUTH=false
NEXT_PUBLIC_USE_MONGODB_CACHE=false
NEXT_PUBLIC_USE_GCP_STORAGE=false
```

### 3. Update .gitignore

```bash
# Add to .gitignore
.env.local
config/credentials/
```

### 4. Create Directory Structure

```bash
mkdir -p lib/firebase lib/gcp lib/mongodb
mkdir -p config/credentials
```


## 🔥 Firebase Setup

### 1. Create Firebase Project
1. Go to https://console.firebase.google.com/
2. Click "Add project"
3. Follow the setup wizard
4. Enable Authentication, Firestore, and Storage

### 2. Get Configuration
1. Project Settings → General
2. Scroll to "Your apps"
3. Click Web icon (</>) to add web app
4. Copy configuration values to `.env.local`

### 3. Test Connection

```typescript
// test-firebase.ts
import { initializeFirebase } from '@/lib/firebase/firebase';

try {
  const app = initializeFirebase();
  console.log('✅ Firebase connected:', app.name);
} catch (error) {
  console.error('❌ Firebase error:', error);
}
```

## ☁️ GCP Setup

### 1. Create GCP Project
1. Go to https://console.cloud.google.com/
2. Create new project
3. Enable Cloud Storage API
4. Create service account
5. Download JSON key file

### 2. Configure Credentials
1. Save JSON key to `config/credentials/gcp-service-account.json`
2. Update `GCP_CREDENTIALS_PATH` in `.env.local`
3. Create storage bucket in GCP Console

### 3. Test Connection

```bash
# Install gcloud CLI
# https://cloud.google.com/sdk/docs/install

# Authenticate
gcloud auth application-default login

# Test
gcloud storage ls
```

## 🍃 MongoDB Setup

### 1. Install MongoDB

**Windows:**
```bash
# Download from https://www.mongodb.com/try/download/community
# Or use Docker:
docker run -d -p 27017:27017 --name mongodb mongo:7
```

**Mac:**
```bash
brew tap mongodb/brew
brew install mongodb-community@7
brew services start mongodb-community@7
```

**Linux:**
```bash
sudo apt-get install mongodb
sudo systemctl start mongodb
```

### 2. Create Database

```bash
mongosh
use surptech_banking
db.customers.insertOne({ test: "data" })
```

### 3. Test Connection

```typescript
// test-mongodb.ts
import { connectToDatabase } from '@/lib/mongodb/client';

async function test() {
  try {
    const db = await connectToDatabase();
    console.log('✅ MongoDB connected:', db.databaseName);
  } catch (error) {
    console.error('❌ MongoDB error:', error);
  }
}

test();
```


## 🧪 Testing Your Setup

### Run All Tests

```bash
# Create test script
npm run test:cloud
```

Add to `package.json`:

```json
{
  "scripts": {
    "test:cloud": "node scripts/test-cloud-connections.js"
  }
}
```

### Test Script

Create `scripts/test-cloud-connections.js`:

```javascript
const { initializeFirebase } = require('./lib/firebase/firebase');
const { connectToDatabase } = require('./lib/mongodb/client');

async function testConnections() {
  console.log('🧪 Testing Cloud Connections...\n');
  
  // Test Firebase
  try {
    const app = initializeFirebase();
    console.log('✅ Firebase: Connected');
  } catch (error) {
    console.error('❌ Firebase: Failed -', error.message);
  }
  
  // Test MongoDB
  try {
    const db = await connectToDatabase();
    console.log('✅ MongoDB: Connected');
  } catch (error) {
    console.error('❌ MongoDB: Failed -', error.message);
  }
  
  console.log('\n✨ Test complete!');
}

testConnections();
```

## 📚 Common Commands

### Development

```bash
# Start dev server
npm run dev

# Type check
npm run type-check

# Lint
npm run lint

# Build
npm run build
```

### Database

```bash
# MongoDB shell
mongosh

# Show databases
show dbs

# Use database
use surptech_banking

# Show collections
show collections

# Query
db.customers.find()
```

### Firebase

```bash
# Install Firebase CLI
npm install -g firebase-tools

# Login
firebase login

# Deploy
firebase deploy
```

### GCP

```bash
# List buckets
gcloud storage ls

# Upload file
gcloud storage cp file.txt gs://your-bucket/

# List files
gcloud storage ls gs://your-bucket/
```

## 🐛 Quick Troubleshooting

### Firebase Not Connecting
```bash
# Check environment variables
echo $NEXT_PUBLIC_FIREBASE_API_KEY

# Verify Firebase project
firebase projects:list
```

### MongoDB Connection Refused
```bash
# Check if MongoDB is running
mongosh --eval "db.version()"

# Check port
netstat -an | grep 27017
```

### GCP Authentication Failed
```bash
# Check credentials file
cat config/credentials/gcp-service-account.json

# Test authentication
gcloud auth application-default print-access-token
```

## 📖 Next Steps

1. ✅ Complete setup above
2. 📖 Read [INTEGRATION_BASELINE.md](./INTEGRATION_BASELINE.md) for detailed implementation
3. 🏗️ Follow Phase 1 of the Implementation Roadmap
4. 🧪 Write tests for your integrations
5. 🚀 Deploy to staging environment

## 💡 Pro Tips

- Start with feature flags disabled
- Test each integration independently
- Use MongoDB for caching, not primary storage
- Keep GCP operations server-side only
- Monitor costs from day one
- Set up alerts for errors and usage

## 🆘 Need Help?

- 📖 [Full Integration Guide](./INTEGRATION_BASELINE.md)
- 🔧 [Troubleshooting Guide](./INTEGRATION_BASELINE.md#troubleshooting-guide)
- 📚 [Official Docs](./INTEGRATION_BASELINE.md#additional-resources)
