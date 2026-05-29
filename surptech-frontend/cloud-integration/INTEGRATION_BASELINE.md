# Cloud Integration Baseline for SurpTech Banking Frontend

## Overview

This document provides a comprehensive baseline for integrating Firebase, Google Cloud Platform (GCP), and MongoDB into the SurpTech Banking Frontend. The integration follows the existing layered architecture and maintains consistency with the current design patterns.

## Table of Contents

1. [Firebase Integration](#firebase-integration)
2. [Google Cloud Platform Integration](#google-cloud-platform-integration)
3. [MongoDB Integration](#mongodb-integration)
4. [Architecture Considerations](#architecture-considerations)
5. [Implementation Roadmap](#implementation-roadmap)

---

## Firebase Integration

Firebase provides a comprehensive suite of backend services including authentication, real-time database, cloud storage, and hosting.

### 1. Firebase Setup

#### Installation

```bash
npm install firebase
npm install --save-dev @types/firebase
```

#### Configuration Structure

```
surptech-frontend/
├── config/
│   ├── api.config.ts           # Existing
│   ├── firebase.config.ts      # New - Firebase configuration
│   └── environment.config.ts   # New - Environment management
├── lib/
│   └── firebase/
│       ├── firebase.ts         # Firebase initialization
│       ├── auth.ts             # Authentication utilities
│       ├── firestore.ts        # Firestore utilities
│       └── storage.ts          # Storage utilities
```


### 2. Firebase Configuration Files

#### `config/firebase.config.ts`

```typescript
/**
 * Firebase Configuration
 * 
 * Centralizes Firebase project configuration.
 * Uses environment variables for security.
 */

export interface FirebaseConfig {
  apiKey: string;
  authDomain: string;
  projectId: string;
  storageBucket: string;
  messagingSenderId: string;
  appId: string;
  measurementId?: string;
}

export const firebaseConfig: FirebaseConfig = {
  apiKey: process.env.NEXT_PUBLIC_FIREBASE_API_KEY || '',
  authDomain: process.env.NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN || '',
  projectId: process.env.NEXT_PUBLIC_FIREBASE_PROJECT_ID || '',
  storageBucket: process.env.NEXT_PUBLIC_FIREBASE_STORAGE_BUCKET || '',
  messagingSenderId: process.env.NEXT_PUBLIC_FIREBASE_MESSAGING_SENDER_ID || '',
  appId: process.env.NEXT_PUBLIC_FIREBASE_APP_ID || '',
  measurementId: process.env.NEXT_PUBLIC_FIREBASE_MEASUREMENT_ID,
};

/**
 * Validates that all required Firebase configuration is present.
 */
export const validateFirebaseConfig = (): boolean => {
  const required = [
    'apiKey',
    'authDomain',
    'projectId',
    'storageBucket',
    'messagingSenderId',
    'appId',
  ];
  
  return required.every(key => 
    firebaseConfig[key as keyof FirebaseConfig]
  );
};
```


#### `lib/firebase/firebase.ts`

```typescript
/**
 * Firebase Initialization
 * 
 * Initializes Firebase app and provides access to Firebase services.
 * Follows singleton pattern to ensure single Firebase instance.
 */

import { initializeApp, FirebaseApp, getApps } from 'firebase/app';
import { getAuth, Auth } from 'firebase/auth';
import { getFirestore, Firestore } from 'firebase/firestore';
import { getStorage, FirebaseStorage } from 'firebase/storage';
import { firebaseConfig, validateFirebaseConfig } from '@/config/firebase.config';

let firebaseApp: FirebaseApp | null = null;
let auth: Auth | null = null;
let firestore: Firestore | null = null;
let storage: FirebaseStorage | null = null;

/**
 * Initializes Firebase app if not already initialized.
 * 
 * @returns Firebase app instance
 * @throws Error if Firebase configuration is invalid
 */
export const initializeFirebase = (): FirebaseApp => {
  if (!validateFirebaseConfig()) {
    throw new Error('Firebase configuration is incomplete. Check environment variables.');
  }
  
  // Check if Firebase is already initialized
  const existingApps = getApps();
  if (existingApps.length > 0) {
    firebaseApp = existingApps[0];
  } else {
    firebaseApp = initializeApp(firebaseConfig);
  }
  
  return firebaseApp;
};

/**
 * Gets Firebase Authentication instance.
 */
export const getFirebaseAuth = (): Auth => {
  if (!auth) {
    const app = initializeFirebase();
    auth = getAuth(app);
  }
  return auth;
};

/**
 * Gets Firestore instance.
 */
export const getFirebaseFirestore = (): Firestore => {
  if (!firestore) {
    const app = initializeFirebase();
    firestore = getFirestore(app);
  }
  return firestore;
};

/**
 * Gets Firebase Storage instance.
 */
export const getFirebaseStorage = (): FirebaseStorage => {
  if (!storage) {
    const app = initializeFirebase();
    storage = getStorage(app);
  }
  return storage;
};
```


### 3. Firebase Authentication Integration

#### `lib/firebase/auth.ts`

```typescript
/**
 * Firebase Authentication Utilities
 * 
 * Provides authentication methods following the service pattern.
 */

import {
  signInWithEmailAndPassword,
  createUserWithEmailAndPassword,
  signOut,
  onAuthStateChanged,
  User,
  UserCredential,
} from 'firebase/auth';
import { getFirebaseAuth } from './firebase';

export class FirebaseAuthService {
  private auth = getFirebaseAuth();
  
  /**
   * Signs in a user with email and password.
   */
  async signIn(email: string, password: string): Promise<UserCredential> {
    try {
      return await signInWithEmailAndPassword(this.auth, email, password);
    } catch (error) {
      throw this.handleAuthError(error);
    }
  }
  
  /**
   * Creates a new user account.
   */
  async signUp(email: string, password: string): Promise<UserCredential> {
    try {
      return await createUserWithEmailAndPassword(this.auth, email, password);
    } catch (error) {
      throw this.handleAuthError(error);
    }
  }
  
  /**
   * Signs out the current user.
   */
  async signOut(): Promise<void> {
    try {
      await signOut(this.auth);
    } catch (error) {
      throw this.handleAuthError(error);
    }
  }
  
  /**
   * Subscribes to authentication state changes.
   */
  onAuthStateChange(callback: (user: User | null) => void): () => void {
    return onAuthStateChanged(this.auth, callback);
  }
  
  /**
   * Gets the current user.
   */
  getCurrentUser(): User | null {
    return this.auth.currentUser;
  }
  
  /**
   * Handles Firebase authentication errors.
   */
  private handleAuthError(error: unknown): Error {
    if (error instanceof Error) {
      return new Error(`Authentication error: ${error.message}`);
    }
    return new Error('Unknown authentication error');
  }
}

export const firebaseAuthService = new FirebaseAuthService();
```


### 4. Firebase Firestore Integration

#### `lib/firebase/firestore.ts`

```typescript
/**
 * Firebase Firestore Utilities
 * 
 * Provides Firestore database operations following the repository pattern.
 */

import {
  collection,
  doc,
  getDoc,
  getDocs,
  setDoc,
  updateDoc,
  deleteDoc,
  query,
  where,
  QueryConstraint,
  DocumentData,
} from 'firebase/firestore';
import { getFirebaseFirestore } from './firebase';

export class FirestoreService {
  private db = getFirebaseFirestore();
  
  /**
   * Gets a document by ID.
   */
  async getDocument<T = DocumentData>(
    collectionName: string,
    documentId: string
  ): Promise<T | null> {
    try {
      const docRef = doc(this.db, collectionName, documentId);
      const docSnap = await getDoc(docRef);
      
      if (docSnap.exists()) {
        return { id: docSnap.id, ...docSnap.data() } as T;
      }
      return null;
    } catch (error) {
      throw this.handleFirestoreError(error);
    }
  }
  
  /**
   * Gets all documents in a collection.
   */
  async getCollection<T = DocumentData>(
    collectionName: string,
    ...constraints: QueryConstraint[]
  ): Promise<T[]> {
    try {
      const collectionRef = collection(this.db, collectionName);
      const q = constraints.length > 0 
        ? query(collectionRef, ...constraints) 
        : collectionRef;
      
      const querySnapshot = await getDocs(q);
      return querySnapshot.docs.map(doc => ({
        id: doc.id,
        ...doc.data(),
      })) as T[];
    } catch (error) {
      throw this.handleFirestoreError(error);
    }
  }
  
  /**
   * Creates or updates a document.
   */
  async setDocument<T = DocumentData>(
    collectionName: string,
    documentId: string,
    data: T
  ): Promise<void> {
    try {
      const docRef = doc(this.db, collectionName, documentId);
      await setDoc(docRef, data);
    } catch (error) {
      throw this.handleFirestoreError(error);
    }
  }
  
  /**
   * Updates a document.
   */
  async updateDocument<T = Partial<DocumentData>>(
    collectionName: string,
    documentId: string,
    data: T
  ): Promise<void> {
    try {
      const docRef = doc(this.db, collectionName, documentId);
      await updateDoc(docRef, data);
    } catch (error) {
      throw this.handleFirestoreError(error);
    }
  }
  
  /**
   * Deletes a document.
   */
  async deleteDocument(
    collectionName: string,
    documentId: string
  ): Promise<void> {
    try {
      const docRef = doc(this.db, collectionName, documentId);
      await deleteDoc(docRef);
    } catch (error) {
      throw this.handleFirestoreError(error);
    }
  }
  
  private handleFirestoreError(error: unknown): Error {
    if (error instanceof Error) {
      return new Error(`Firestore error: ${error.message}`);
    }
    return new Error('Unknown Firestore error');
  }
}

export const firestoreService = new FirestoreService();
```


### 5. Firebase Custom Hooks

#### `hooks/useFirebaseAuth.ts`

```typescript
/**
 * Custom hook for Firebase Authentication
 * 
 * Manages authentication state and provides auth methods.
 */

'use client';

import { useState, useEffect } from 'react';
import { User } from 'firebase/auth';
import { firebaseAuthService } from '@/lib/firebase/auth';

export interface UseFirebaseAuthReturn {
  user: User | null;
  loading: boolean;
  error: Error | null;
  signIn: (email: string, password: string) => Promise<void>;
  signUp: (email: string, password: string) => Promise<void>;
  signOut: () => Promise<void>;
}

export const useFirebaseAuth = (): UseFirebaseAuthReturn => {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);
  
  useEffect(() => {
    const unsubscribe = firebaseAuthService.onAuthStateChange((user) => {
      setUser(user);
      setLoading(false);
    });
    
    return () => unsubscribe();
  }, []);
  
  const signIn = async (email: string, password: string) => {
    try {
      setError(null);
      setLoading(true);
      await firebaseAuthService.signIn(email, password);
    } catch (err) {
      setError(err as Error);
    } finally {
      setLoading(false);
    }
  };
  
  const signUp = async (email: string, password: string) => {
    try {
      setError(null);
      setLoading(true);
      await firebaseAuthService.signUp(email, password);
    } catch (err) {
      setError(err as Error);
    } finally {
      setLoading(false);
    }
  };
  
  const signOut = async () => {
    try {
      setError(null);
      await firebaseAuthService.signOut();
    } catch (err) {
      setError(err as Error);
    }
  };
  
  return { user, loading, error, signIn, signUp, signOut };
};
```


---

## Google Cloud Platform Integration

GCP provides enterprise-grade cloud services including Cloud Storage, Cloud Functions, BigQuery, and more.

### 1. GCP Setup

#### Installation

```bash
npm install @google-cloud/storage
npm install @google-cloud/firestore
npm install @google-cloud/bigquery
npm install --save-dev @types/google-cloud__storage
```

#### Configuration Structure

```
surptech-frontend/
├── config/
│   ├── gcp.config.ts           # New - GCP configuration
│   └── credentials/
│       └── gcp-service-account.json  # Service account key (gitignored)
├── lib/
│   └── gcp/
│       ├── storage.ts          # Cloud Storage utilities
│       ├── bigquery.ts         # BigQuery utilities
│       └── functions.ts        # Cloud Functions utilities
```

### 2. GCP Configuration Files

#### `config/gcp.config.ts`

```typescript
/**
 * Google Cloud Platform Configuration
 * 
 * Centralizes GCP project configuration.
 */

export interface GCPConfig {
  projectId: string;
  credentials?: string; // Path to service account JSON
  region: string;
  storageBucket: string;
}

export const gcpConfig: GCPConfig = {
  projectId: process.env.GCP_PROJECT_ID || '',
  credentials: process.env.GCP_CREDENTIALS_PATH,
  region: process.env.GCP_REGION || 'us-central1',
  storageBucket: process.env.GCP_STORAGE_BUCKET || '',
};

/**
 * Validates GCP configuration.
 */
export const validateGCPConfig = (): boolean => {
  return !!(gcpConfig.projectId && gcpConfig.storageBucket);
};
```


### 3. GCP Cloud Storage Integration

#### `lib/gcp/storage.ts`

```typescript
/**
 * Google Cloud Storage Service
 * 
 * Provides file storage operations using GCP Cloud Storage.
 * Note: This should run on the server-side (API routes) for security.
 */

import { Storage } from '@google-cloud/storage';
import { gcpConfig } from '@/config/gcp.config';

export class GCPStorageService {
  private storage: Storage;
  private bucketName: string;
  
  constructor() {
    this.storage = new Storage({
      projectId: gcpConfig.projectId,
      keyFilename: gcpConfig.credentials,
    });
    this.bucketName = gcpConfig.storageBucket;
  }
  
  /**
   * Uploads a file to Cloud Storage.
   */
  async uploadFile(
    filePath: string,
    destination: string,
    metadata?: Record<string, string>
  ): Promise<string> {
    try {
      const bucket = this.storage.bucket(this.bucketName);
      await bucket.upload(filePath, {
        destination,
        metadata: {
          metadata,
        },
      });
      
      return `gs://${this.bucketName}/${destination}`;
    } catch (error) {
      throw this.handleStorageError(error);
    }
  }
  
  /**
   * Downloads a file from Cloud Storage.
   */
  async downloadFile(
    fileName: string,
    destination: string
  ): Promise<void> {
    try {
      const bucket = this.storage.bucket(this.bucketName);
      await bucket.file(fileName).download({ destination });
    } catch (error) {
      throw this.handleStorageError(error);
    }
  }
  
  /**
   * Gets a signed URL for temporary file access.
   */
  async getSignedUrl(
    fileName: string,
    expiresInMinutes: number = 15
  ): Promise<string> {
    try {
      const bucket = this.storage.bucket(this.bucketName);
      const file = bucket.file(fileName);
      
      const [url] = await file.getSignedUrl({
        version: 'v4',
        action: 'read',
        expires: Date.now() + expiresInMinutes * 60 * 1000,
      });
      
      return url;
    } catch (error) {
      throw this.handleStorageError(error);
    }
  }
  
  /**
   * Deletes a file from Cloud Storage.
   */
  async deleteFile(fileName: string): Promise<void> {
    try {
      const bucket = this.storage.bucket(this.bucketName);
      await bucket.file(fileName).delete();
    } catch (error) {
      throw this.handleStorageError(error);
    }
  }
  
  /**
   * Lists files in a directory.
   */
  async listFiles(prefix?: string): Promise<string[]> {
    try {
      const bucket = this.storage.bucket(this.bucketName);
      const [files] = await bucket.getFiles({ prefix });
      return files.map(file => file.name);
    } catch (error) {
      throw this.handleStorageError(error);
    }
  }
  
  private handleStorageError(error: unknown): Error {
    if (error instanceof Error) {
      return new Error(`GCP Storage error: ${error.message}`);
    }
    return new Error('Unknown GCP Storage error');
  }
}

export const gcpStorageService = new GCPStorageService();
```


### 4. GCP API Routes (Server-Side)

#### `app/api/gcp/upload/route.ts`

```typescript
/**
 * API Route for GCP file uploads
 * 
 * Handles file uploads to Google Cloud Storage.
 * This runs server-side to keep credentials secure.
 */

import { NextRequest, NextResponse } from 'next/server';
import { gcpStorageService } from '@/lib/gcp/storage';

export async function POST(request: NextRequest) {
  try {
    const formData = await request.formData();
    const file = formData.get('file') as File;
    
    if (!file) {
      return NextResponse.json(
        { error: 'No file provided' },
        { status: 400 }
      );
    }
    
    // Convert File to buffer and save temporarily
    const bytes = await file.arrayBuffer();
    const buffer = Buffer.from(bytes);
    
    // In production, you'd save to temp location and upload
    // For now, this is a placeholder
    const destination = `uploads/${Date.now()}-${file.name}`;
    
    // Upload to GCP
    const fileUrl = await gcpStorageService.uploadFile(
      buffer.toString(), // This needs proper file handling
      destination
    );
    
    return NextResponse.json({
      success: true,
      fileUrl,
      fileName: file.name,
    });
  } catch (error) {
    console.error('Upload error:', error);
    return NextResponse.json(
      { error: 'Upload failed' },
      { status: 500 }
    );
  }
}
```


---

## MongoDB Integration

MongoDB can be integrated for flexible document storage and real-time data operations.

### 1. MongoDB Setup

#### Installation

```bash
npm install mongodb
npm install --save-dev @types/mongodb
```

#### Configuration Structure

```
surptech-frontend/
├── config/
│   └── mongodb.config.ts       # New - MongoDB configuration
├── lib/
│   └── mongodb/
│       ├── client.ts           # MongoDB client initialization
│       ├── repository.ts       # Base repository pattern
│       └── collections.ts      # Collection definitions
```

### 2. MongoDB Configuration Files

#### `config/mongodb.config.ts`

```typescript
/**
 * MongoDB Configuration
 * 
 * Centralizes MongoDB connection configuration.
 */

export interface MongoDBConfig {
  uri: string;
  database: string;
  options: {
    maxPoolSize: number;
    minPoolSize: number;
    serverSelectionTimeoutMS: number;
  };
}

export const mongodbConfig: MongoDBConfig = {
  uri: process.env.MONGODB_URI || 'mongodb://localhost:27017',
  database: process.env.MONGODB_DATABASE || 'surptech_banking',
  options: {
    maxPoolSize: 10,
    minPoolSize: 2,
    serverSelectionTimeoutMS: 5000,
  },
};

/**
 * Validates MongoDB configuration.
 */
export const validateMongoDBConfig = (): boolean => {
  return !!(mongodbConfig.uri && mongodbConfig.database);
};
```


### 3. MongoDB Client Initialization

#### `lib/mongodb/client.ts`

```typescript
/**
 * MongoDB Client
 * 
 * Manages MongoDB connection using singleton pattern.
 * Should only be used in server-side code (API routes).
 */

import { MongoClient, Db } from 'mongodb';
import { mongodbConfig, validateMongoDBConfig } from '@/config/mongodb.config';

let client: MongoClient | null = null;
let db: Db | null = null;

/**
 * Connects to MongoDB and returns database instance.
 */
export async function connectToDatabase(): Promise<Db> {
  if (!validateMongoDBConfig()) {
    throw new Error('MongoDB configuration is incomplete');
  }
  
  if (db) {
    return db;
  }
  
  try {
    if (!client) {
      client = new MongoClient(mongodbConfig.uri, mongodbConfig.options);
      await client.connect();
      console.log('Connected to MongoDB');
    }
    
    db = client.db(mongodbConfig.database);
    return db;
  } catch (error) {
    console.error('MongoDB connection error:', error);
    throw new Error('Failed to connect to MongoDB');
  }
}

/**
 * Closes MongoDB connection.
 */
export async function closeDatabase(): Promise<void> {
  if (client) {
    await client.close();
    client = null;
    db = null;
    console.log('MongoDB connection closed');
  }
}

/**
 * Gets the current database instance.
 */
export function getDatabase(): Db {
  if (!db) {
    throw new Error('Database not connected. Call connectToDatabase first.');
  }
  return db;
}
```


### 4. MongoDB Repository Pattern

#### `lib/mongodb/repository.ts`

```typescript
/**
 * Base MongoDB Repository
 * 
 * Provides generic CRUD operations for MongoDB collections.
 * Follows the repository pattern from backend architecture.
 */

import { Collection, Document, Filter, OptionalId, UpdateFilter } from 'mongodb';
import { connectToDatabase } from './client';

export abstract class MongoRepository<T extends Document> {
  protected collection: Collection<T> | null = null;
  
  constructor(private collectionName: string) {}
  
  /**
   * Gets the collection instance.
   */
  protected async getCollection(): Promise<Collection<T>> {
    if (!this.collection) {
      const db = await connectToDatabase();
      this.collection = db.collection<T>(this.collectionName);
    }
    return this.collection;
  }
  
  /**
   * Finds a document by ID.
   */
  async findById(id: string): Promise<T | null> {
    const collection = await this.getCollection();
    return await collection.findOne({ _id: id } as Filter<T>);
  }
  
  /**
   * Finds documents matching a filter.
   */
  async find(filter: Filter<T>): Promise<T[]> {
    const collection = await this.getCollection();
    return await collection.find(filter).toArray();
  }
  
  /**
   * Finds all documents.
   */
  async findAll(): Promise<T[]> {
    const collection = await this.getCollection();
    return await collection.find({}).toArray();
  }
  
  /**
   * Inserts a new document.
   */
  async insert(document: OptionalId<T>): Promise<string> {
    const collection = await this.getCollection();
    const result = await collection.insertOne(document);
    return result.insertedId.toString();
  }
  
  /**
   * Updates a document by ID.
   */
  async update(id: string, update: UpdateFilter<T>): Promise<boolean> {
    const collection = await this.getCollection();
    const result = await collection.updateOne(
      { _id: id } as Filter<T>,
      update
    );
    return result.modifiedCount > 0;
  }
  
  /**
   * Deletes a document by ID.
   */
  async delete(id: string): Promise<boolean> {
    const collection = await this.getCollection();
    const result = await collection.deleteOne({ _id: id } as Filter<T>);
    return result.deletedCount > 0;
  }
  
  /**
   * Counts documents matching a filter.
   */
  async count(filter: Filter<T> = {}): Promise<number> {
    const collection = await this.getCollection();
    return await collection.countDocuments(filter);
  }
}
```


### 5. MongoDB Example Repository

#### `lib/mongodb/repositories/CustomerRepository.ts`

```typescript
/**
 * Customer Repository
 * 
 * Handles customer data operations in MongoDB.
 */

import { Document } from 'mongodb';
import { MongoRepository } from '../repository';

export interface CustomerDocument extends Document {
  _id: string;
  socialSecurityNumber: string;
  firstName: string;
  lastName: string;
  email: string;
  address: string;
  createdAt: Date;
  updatedAt: Date;
}

export class CustomerRepository extends MongoRepository<CustomerDocument> {
  constructor() {
    super('customers');
  }
  
  /**
   * Finds a customer by SSN.
   */
  async findBySSN(ssn: string): Promise<CustomerDocument | null> {
    const collection = await this.getCollection();
    return await collection.findOne({ socialSecurityNumber: ssn });
  }
  
  /**
   * Finds customers by email.
   */
  async findByEmail(email: string): Promise<CustomerDocument[]> {
    return await this.find({ email });
  }
  
  /**
   * Creates a new customer.
   */
  async createCustomer(
    customer: Omit<CustomerDocument, '_id' | 'createdAt' | 'updatedAt'>
  ): Promise<string> {
    const now = new Date();
    return await this.insert({
      ...customer,
      createdAt: now,
      updatedAt: now,
    } as CustomerDocument);
  }
  
  /**
   * Updates customer information.
   */
  async updateCustomer(
    id: string,
    updates: Partial<Omit<CustomerDocument, '_id' | 'createdAt'>>
  ): Promise<boolean> {
    return await this.update(id, {
      $set: {
        ...updates,
        updatedAt: new Date(),
      },
    });
  }
}

export const customerRepository = new CustomerRepository();
```


### 6. MongoDB API Routes

#### `app/api/mongodb/customers/route.ts`

```typescript
/**
 * MongoDB Customers API Route
 * 
 * Provides REST API for customer operations using MongoDB.
 */

import { NextRequest, NextResponse } from 'next/server';
import { customerRepository } from '@/lib/mongodb/repositories/CustomerRepository';

/**
 * GET /api/mongodb/customers?ssn={ssn}
 * Retrieves customer by SSN
 */
export async function GET(request: NextRequest) {
  try {
    const searchParams = request.nextUrl.searchParams;
    const ssn = searchParams.get('ssn');
    
    if (!ssn) {
      return NextResponse.json(
        { error: 'SSN parameter is required' },
        { status: 400 }
      );
    }
    
    const customer = await customerRepository.findBySSN(ssn);
    
    if (!customer) {
      return NextResponse.json(
        { error: 'Customer not found' },
        { status: 404 }
      );
    }
    
    return NextResponse.json(customer);
  } catch (error) {
    console.error('MongoDB GET error:', error);
    return NextResponse.json(
      { error: 'Internal server error' },
      { status: 500 }
    );
  }
}

/**
 * POST /api/mongodb/customers
 * Creates a new customer
 */
export async function POST(request: NextRequest) {
  try {
    const body = await request.json();
    
    // Validate required fields
    const required = ['socialSecurityNumber', 'firstName', 'lastName', 'email'];
    for (const field of required) {
      if (!body[field]) {
        return NextResponse.json(
          { error: `${field} is required` },
          { status: 400 }
        );
      }
    }
    
    const customerId = await customerRepository.createCustomer(body);
    
    return NextResponse.json(
      { id: customerId, message: 'Customer created successfully' },
      { status: 201 }
    );
  } catch (error) {
    console.error('MongoDB POST error:', error);
    return NextResponse.json(
      { error: 'Internal server error' },
      { status: 500 }
    );
  }
}
```


---

## Architecture Considerations

### 1. Layered Architecture Integration

The cloud integrations follow the existing layered architecture:

```
┌─────────────────────────────────────────────────────────┐
│                    Presentation Layer                    │
│              (React Components + Hooks)                  │
└─────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────┐
│                      Service Layer                       │
│    (CustomerService, FirebaseAuthService, etc.)         │
└─────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────┐
│                      Client Layer                        │
│  (DataAggregatorClient, FirestoreService, etc.)         │
└─────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────┐
│                   External Services                      │
│    (Backend APIs, Firebase, GCP, MongoDB)               │
└─────────────────────────────────────────────────────────┘
```

### 2. Security Best Practices

#### Environment Variables

Create `.env.local` file (never commit to git):

```bash
# Firebase Configuration
NEXT_PUBLIC_FIREBASE_API_KEY=your_api_key
NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN=your_project.firebaseapp.com
NEXT_PUBLIC_FIREBASE_PROJECT_ID=your_project_id
NEXT_PUBLIC_FIREBASE_STORAGE_BUCKET=your_project.appspot.com
NEXT_PUBLIC_FIREBASE_MESSAGING_SENDER_ID=your_sender_id
NEXT_PUBLIC_FIREBASE_APP_ID=your_app_id

# GCP Configuration (Server-side only)
GCP_PROJECT_ID=your_gcp_project
GCP_CREDENTIALS_PATH=./config/credentials/gcp-service-account.json
GCP_REGION=us-central1
GCP_STORAGE_BUCKET=your_bucket_name

# MongoDB Configuration (Server-side only)
MONGODB_URI=mongodb://localhost:27017
MONGODB_DATABASE=surptech_banking
```

#### Security Rules

1. **Firebase**: Use Firebase Security Rules
2. **GCP**: Use IAM roles and service accounts
3. **MongoDB**: Use authentication and role-based access control
4. **Next.js**: Keep sensitive operations in API routes (server-side)


### 3. Data Flow Patterns

#### Pattern 1: Firebase Authentication + Backend API

```
User Login
    ↓
Firebase Auth (Client-side)
    ↓
Get ID Token
    ↓
Backend API Request (with token in header)
    ↓
Backend validates token
    ↓
Return data
```

#### Pattern 2: Hybrid Storage (MongoDB + Backend)

```
User Request
    ↓
Next.js API Route
    ↓
Check MongoDB Cache
    ↓
If not found → Call Backend API
    ↓
Store in MongoDB
    ↓
Return to client
```

#### Pattern 3: File Upload (GCP Storage)

```
User selects file
    ↓
Upload to Next.js API Route
    ↓
API Route uploads to GCP Storage
    ↓
Return signed URL
    ↓
Display to user
```

### 4. Error Handling Strategy

All cloud integrations should use consistent error handling:

```typescript
// Example error handling wrapper
export class CloudServiceError extends Error {
  constructor(
    message: string,
    public service: 'firebase' | 'gcp' | 'mongodb',
    public code?: string,
    public originalError?: unknown
  ) {
    super(message);
    this.name = 'CloudServiceError';
  }
}

// Usage in services
try {
  // Cloud operation
} catch (error) {
  throw new CloudServiceError(
    'Operation failed',
    'firebase',
    'auth/invalid-email',
    error
  );
}
```


### 5. Testing Strategy

#### Unit Tests

```typescript
// Example: Testing Firebase Auth Service
import { firebaseAuthService } from '@/lib/firebase/auth';

describe('FirebaseAuthService', () => {
  it('should sign in with valid credentials', async () => {
    const result = await firebaseAuthService.signIn(
      'test@example.com',
      'password123'
    );
    expect(result.user).toBeDefined();
  });
  
  it('should throw error with invalid credentials', async () => {
    await expect(
      firebaseAuthService.signIn('invalid@example.com', 'wrong')
    ).rejects.toThrow();
  });
});
```

#### Integration Tests

```typescript
// Example: Testing MongoDB Repository
import { customerRepository } from '@/lib/mongodb/repositories/CustomerRepository';

describe('CustomerRepository', () => {
  beforeAll(async () => {
    await connectToDatabase();
  });
  
  afterAll(async () => {
    await closeDatabase();
  });
  
  it('should create and retrieve customer', async () => {
    const customerId = await customerRepository.createCustomer({
      socialSecurityNumber: '123-45-6789',
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      address: '123 Main St',
    });
    
    const customer = await customerRepository.findById(customerId);
    expect(customer?.firstName).toBe('John');
  });
});
```


---

## Implementation Roadmap

### Phase 1: Foundation (Week 1-2)

#### Tasks:
1. **Environment Setup**
   - Create `.env.local` with all configuration
   - Set up Firebase project
   - Set up GCP project
   - Set up MongoDB instance

2. **Configuration Files**
   - Implement `firebase.config.ts`
   - Implement `gcp.config.ts`
   - Implement `mongodb.config.ts`
   - Update `.gitignore` for credentials

3. **Basic Initialization**
   - Implement Firebase initialization
   - Implement GCP client setup
   - Implement MongoDB connection

#### Deliverables:
- All configuration files created
- Successful connection to all services
- Environment variables documented

### Phase 2: Firebase Integration (Week 3-4)

#### Tasks:
1. **Authentication**
   - Implement `FirebaseAuthService`
   - Create `useFirebaseAuth` hook
   - Build login/signup components
   - Add protected routes

2. **Firestore Database**
   - Implement `FirestoreService`
   - Create collection schemas
   - Build CRUD operations
   - Add real-time listeners

3. **Storage**
   - Implement file upload utilities
   - Create upload components
   - Add file management UI

#### Deliverables:
- Working authentication system
- Firestore CRUD operations
- File upload functionality


### Phase 3: GCP Integration (Week 5-6)

#### Tasks:
1. **Cloud Storage**
   - Implement `GCPStorageService`
   - Create API routes for uploads
   - Add signed URL generation
   - Build file browser UI

2. **BigQuery (Optional)**
   - Set up BigQuery client
   - Create analytics queries
   - Build reporting dashboard

3. **Cloud Functions (Optional)**
   - Deploy serverless functions
   - Integrate with frontend
   - Add webhook handlers

#### Deliverables:
- GCP Storage integration
- API routes for file operations
- Optional: Analytics dashboard

### Phase 4: MongoDB Integration (Week 7-8)

#### Tasks:
1. **Database Setup**
   - Implement MongoDB client
   - Create base repository
   - Define collection schemas

2. **Repositories**
   - Implement `CustomerRepository`
   - Implement `TransactionRepository`
   - Add indexing strategies

3. **API Routes**
   - Create REST endpoints
   - Add validation middleware
   - Implement error handling

#### Deliverables:
- MongoDB connection established
- Repository pattern implemented
- REST API for data operations

### Phase 5: Integration & Testing (Week 9-10)

#### Tasks:
1. **Integration**
   - Connect all services
   - Implement hybrid data flows
   - Add caching strategies

2. **Testing**
   - Write unit tests
   - Write integration tests
   - Perform load testing

3. **Documentation**
   - Update README
   - Create API documentation
   - Write deployment guide

#### Deliverables:
- Fully integrated system
- Comprehensive test coverage
- Complete documentation


---

## Example Use Cases

### Use Case 1: User Authentication with Firebase

```typescript
// Component: LoginPage
'use client';

import { useState } from 'react';
import { useFirebaseAuth } from '@/hooks/useFirebaseAuth';
import { useRouter } from 'next/navigation';

export default function LoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const { signIn, loading, error } = useFirebaseAuth();
  const router = useRouter();
  
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    await signIn(email, password);
    router.push('/dashboard');
  };
  
  return (
    <form onSubmit={handleSubmit}>
      <input
        type="email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        placeholder="Email"
      />
      <input
        type="password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        placeholder="Password"
      />
      <button type="submit" disabled={loading}>
        {loading ? 'Signing in...' : 'Sign In'}
      </button>
      {error && <p className="error">{error.message}</p>}
    </form>
  );
}
```

### Use Case 2: Storing Customer Data in Firestore

```typescript
// Service: CustomerFirestoreService
import { firestoreService } from '@/lib/firebase/firestore';
import { where } from 'firebase/firestore';

export class CustomerFirestoreService {
  private collectionName = 'customers';
  
  async getCustomerBySSN(ssn: string) {
    const customers = await firestoreService.getCollection(
      this.collectionName,
      where('socialSecurityNumber', '==', ssn)
    );
    return customers[0] || null;
  }
  
  async createCustomer(customer: any) {
    const customerId = `customer_${Date.now()}`;
    await firestoreService.setDocument(
      this.collectionName,
      customerId,
      {
        ...customer,
        createdAt: new Date().toISOString(),
      }
    );
    return customerId;
  }
  
  async updateCustomer(customerId: string, updates: any) {
    await firestoreService.updateDocument(
      this.collectionName,
      customerId,
      {
        ...updates,
        updatedAt: new Date().toISOString(),
      }
    );
  }
}

export const customerFirestoreService = new CustomerFirestoreService();
```


### Use Case 3: File Upload to GCP Storage

```typescript
// Component: FileUpload
'use client';

import { useState } from 'react';

export default function FileUpload() {
  const [file, setFile] = useState<File | null>(null);
  const [uploading, setUploading] = useState(false);
  const [fileUrl, setFileUrl] = useState<string | null>(null);
  
  const handleUpload = async () => {
    if (!file) return;
    
    setUploading(true);
    try {
      const formData = new FormData();
      formData.append('file', file);
      
      const response = await fetch('/api/gcp/upload', {
        method: 'POST',
        body: formData,
      });
      
      const data = await response.json();
      setFileUrl(data.fileUrl);
    } catch (error) {
      console.error('Upload failed:', error);
    } finally {
      setUploading(false);
    }
  };
  
  return (
    <div>
      <input
        type="file"
        onChange={(e) => setFile(e.target.files?.[0] || null)}
      />
      <button onClick={handleUpload} disabled={!file || uploading}>
        {uploading ? 'Uploading...' : 'Upload to GCP'}
      </button>
      {fileUrl && <p>File uploaded: {fileUrl}</p>}
    </div>
  );
}
```

### Use Case 4: Hybrid Data Flow (MongoDB Cache + Backend API)

```typescript
// Service: HybridCustomerService
import { customerRepository } from '@/lib/mongodb/repositories/CustomerRepository';
import { dataAggregatorClient } from '@/client/DataAggregatorClient';

export class HybridCustomerService {
  /**
   * Gets customer info with MongoDB caching.
   * First checks MongoDB, then falls back to backend API.
   */
  async getCustomerInfo(ssn: string) {
    // Try MongoDB cache first
    const cachedCustomer = await customerRepository.findBySSN(ssn);
    
    if (cachedCustomer) {
      console.log('Cache hit: returning from MongoDB');
      return cachedCustomer;
    }
    
    // Cache miss: fetch from backend API
    console.log('Cache miss: fetching from backend');
    const customer = await dataAggregatorClient.getCustomerCreditInfo(ssn);
    
    // Store in MongoDB for future requests
    await customerRepository.createCustomer({
      socialSecurityNumber: ssn,
      firstName: customer.first_name,
      lastName: customer.last_name,
      email: '', // Would need to be added to API response
      address: customer.address,
    });
    
    return customer;
  }
  
  /**
   * Invalidates cache for a customer.
   */
  async invalidateCache(ssn: string) {
    const customer = await customerRepository.findBySSN(ssn);
    if (customer) {
      await customerRepository.delete(customer._id);
    }
  }
}

export const hybridCustomerService = new HybridCustomerService();
```


---

## Performance Optimization

### 1. Caching Strategies

#### Client-Side Caching (React Query)

```bash
npm install @tanstack/react-query
```

```typescript
// hooks/useCustomerWithCache.ts
import { useQuery } from '@tanstack/react-query';
import { customerService } from '@/services/CustomerService';

export const useCustomerWithCache = (ssn: string) => {
  return useQuery({
    queryKey: ['customer', ssn],
    queryFn: () => customerService.getCustomerCreditInfo(ssn),
    staleTime: 5 * 60 * 1000, // 5 minutes
    cacheTime: 10 * 60 * 1000, // 10 minutes
  });
};
```

#### Server-Side Caching (Redis)

```typescript
// lib/cache/redis.ts
import { createClient } from 'redis';

const redisClient = createClient({
  url: process.env.REDIS_URL || 'redis://localhost:6379',
});

export async function getCachedData<T>(key: string): Promise<T | null> {
  const cached = await redisClient.get(key);
  return cached ? JSON.parse(cached) : null;
}

export async function setCachedData<T>(
  key: string,
  data: T,
  expirationSeconds: number = 300
): Promise<void> {
  await redisClient.setEx(key, expirationSeconds, JSON.stringify(data));
}
```

### 2. Connection Pooling

#### MongoDB Connection Pool

```typescript
// Already implemented in mongodb.config.ts
export const mongodbConfig: MongoDBConfig = {
  uri: process.env.MONGODB_URI || 'mongodb://localhost:27017',
  database: process.env.MONGODB_DATABASE || 'surptech_banking',
  options: {
    maxPoolSize: 10,      // Maximum connections
    minPoolSize: 2,       // Minimum connections
    serverSelectionTimeoutMS: 5000,
  },
};
```

### 3. Lazy Loading

```typescript
// Dynamic imports for heavy components
import dynamic from 'next/dynamic';

const HeavyChart = dynamic(() => import('@/components/HeavyChart'), {
  loading: () => <p>Loading chart...</p>,
  ssr: false, // Disable server-side rendering if not needed
});
```


---

## Monitoring and Logging

### 1. Firebase Analytics

```typescript
// lib/firebase/analytics.ts
import { getAnalytics, logEvent } from 'firebase/analytics';
import { initializeFirebase } from './firebase';

export class FirebaseAnalyticsService {
  private analytics;
  
  constructor() {
    const app = initializeFirebase();
    this.analytics = getAnalytics(app);
  }
  
  logPageView(pageName: string) {
    logEvent(this.analytics, 'page_view', {
      page_name: pageName,
      page_location: window.location.href,
    });
  }
  
  logCustomerSearch(ssn: string) {
    logEvent(this.analytics, 'customer_search', {
      search_term: ssn.substring(0, 3) + '-XX-XXXX', // Masked for privacy
    });
  }
  
  logError(error: Error, context: string) {
    logEvent(this.analytics, 'error', {
      error_message: error.message,
      error_context: context,
    });
  }
}

export const analyticsService = new FirebaseAnalyticsService();
```

### 2. Structured Logging

```typescript
// utils/logger.ts
export enum LogLevel {
  DEBUG = 'DEBUG',
  INFO = 'INFO',
  WARN = 'WARN',
  ERROR = 'ERROR',
}

export class Logger {
  private context: string;
  
  constructor(context: string) {
    this.context = context;
  }
  
  private log(level: LogLevel, message: string, data?: any) {
    const logEntry = {
      timestamp: new Date().toISOString(),
      level,
      context: this.context,
      message,
      data,
    };
    
    // In production, send to logging service
    if (process.env.NODE_ENV === 'production') {
      // Send to external logging service (e.g., Cloud Logging)
      this.sendToLoggingService(logEntry);
    } else {
      console.log(JSON.stringify(logEntry, null, 2));
    }
  }
  
  debug(message: string, data?: any) {
    this.log(LogLevel.DEBUG, message, data);
  }
  
  info(message: string, data?: any) {
    this.log(LogLevel.INFO, message, data);
  }
  
  warn(message: string, data?: any) {
    this.log(LogLevel.WARN, message, data);
  }
  
  error(message: string, error?: Error, data?: any) {
    this.log(LogLevel.ERROR, message, {
      error: error?.message,
      stack: error?.stack,
      ...data,
    });
  }
  
  private sendToLoggingService(logEntry: any) {
    // Implement integration with Cloud Logging or similar
  }
}

// Usage
const logger = new Logger('CustomerService');
logger.info('Fetching customer data', { ssn: '***-**-1234' });
```


---

## Deployment Considerations

### 1. Environment-Specific Configuration

```typescript
// config/environment.config.ts
export type Environment = 'development' | 'staging' | 'production';

export const getEnvironment = (): Environment => {
  return (process.env.NEXT_PUBLIC_ENVIRONMENT as Environment) || 'development';
};

export const isProduction = (): boolean => {
  return getEnvironment() === 'production';
};

export const getApiConfig = () => {
  const env = getEnvironment();
  
  const configs = {
    development: {
      apiUrl: 'http://localhost:5555',
      firebaseConfig: { /* dev config */ },
    },
    staging: {
      apiUrl: 'https://staging-api.surptech.com',
      firebaseConfig: { /* staging config */ },
    },
    production: {
      apiUrl: 'https://api.surptech.com',
      firebaseConfig: { /* prod config */ },
    },
  };
  
  return configs[env];
};
```

### 2. Docker Configuration

```dockerfile
# Dockerfile
FROM node:18-alpine AS base

# Install dependencies only when needed
FROM base AS deps
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci

# Rebuild the source code only when needed
FROM base AS builder
WORKDIR /app
COPY --from=deps /app/node_modules ./node_modules
COPY . .

# Set environment variables
ENV NEXT_TELEMETRY_DISABLED 1
ENV NODE_ENV production

RUN npm run build

# Production image
FROM base AS runner
WORKDIR /app

ENV NODE_ENV production
ENV NEXT_TELEMETRY_DISABLED 1

RUN addgroup --system --gid 1001 nodejs
RUN adduser --system --uid 1001 nextjs

COPY --from=builder /app/public ./public
COPY --from=builder --chown=nextjs:nodejs /app/.next/standalone ./
COPY --from=builder --chown=nextjs:nodejs /app/.next/static ./.next/static

USER nextjs

EXPOSE 3000

ENV PORT 3000

CMD ["node", "server.js"]
```

```yaml
# docker-compose.yml
version: '3.8'

services:
  frontend:
    build: .
    ports:
      - "3000:3000"
    environment:
      - NODE_ENV=production
      - MONGODB_URI=mongodb://mongodb:27017
      - MONGODB_DATABASE=surptech_banking
    depends_on:
      - mongodb
    networks:
      - surptech-network

  mongodb:
    image: mongo:7
    ports:
      - "27017:27017"
    volumes:
      - mongodb_data:/data/db
    networks:
      - surptech-network

volumes:
  mongodb_data:

networks:
  surptech-network:
    driver: bridge
```


### 3. CI/CD Pipeline

```yaml
# .github/workflows/deploy.yml
name: Deploy to Production

on:
  push:
    branches: [main]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-node@v3
        with:
          node-version: '18'
      - run: npm ci
      - run: npm run lint
      - run: npm run type-check
      - run: npm test

  build:
    needs: test
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-node@v3
        with:
          node-version: '18'
      - run: npm ci
      - run: npm run build
        env:
          NEXT_PUBLIC_FIREBASE_API_KEY: ${{ secrets.FIREBASE_API_KEY }}
          NEXT_PUBLIC_FIREBASE_PROJECT_ID: ${{ secrets.FIREBASE_PROJECT_ID }}

  deploy:
    needs: build
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Deploy to Firebase Hosting
        uses: FirebaseExtended/action-hosting-deploy@v0
        with:
          repoToken: '${{ secrets.GITHUB_TOKEN }}'
          firebaseServiceAccount: '${{ secrets.FIREBASE_SERVICE_ACCOUNT }}'
          channelId: live
          projectId: ${{ secrets.FIREBASE_PROJECT_ID }}
```

---

## Security Checklist

### Pre-Deployment Security Review

- [ ] **Environment Variables**
  - [ ] All sensitive data in environment variables
  - [ ] No hardcoded credentials in code
  - [ ] `.env.local` in `.gitignore`
  - [ ] Service account keys secured

- [ ] **Firebase Security**
  - [ ] Firebase Security Rules configured
  - [ ] Authentication enabled
  - [ ] API keys restricted by domain
  - [ ] Rate limiting enabled

- [ ] **GCP Security**
  - [ ] IAM roles properly configured
  - [ ] Service accounts with minimal permissions
  - [ ] Storage buckets not publicly accessible
  - [ ] VPC firewall rules configured

- [ ] **MongoDB Security**
  - [ ] Authentication enabled
  - [ ] Network access restricted
  - [ ] SSL/TLS enabled
  - [ ] Regular backups configured

- [ ] **Application Security**
  - [ ] Input validation on all forms
  - [ ] SQL injection prevention
  - [ ] XSS protection enabled
  - [ ] CSRF tokens implemented
  - [ ] Rate limiting on API routes
  - [ ] Sensitive data masked in logs


---

## Cost Optimization

### 1. Firebase Cost Management

```typescript
// Implement pagination to reduce Firestore reads
export class OptimizedFirestoreService {
  async getPaginatedCustomers(
    pageSize: number = 10,
    lastDocId?: string
  ) {
    const constraints = [limit(pageSize)];
    
    if (lastDocId) {
      const lastDoc = await getDoc(doc(this.db, 'customers', lastDocId));
      constraints.push(startAfter(lastDoc));
    }
    
    return await this.getCollection('customers', ...constraints);
  }
}
```

### 2. GCP Cost Optimization

- Use **Cloud Storage lifecycle policies** to move old files to cheaper storage
- Implement **CDN caching** for frequently accessed files
- Use **signed URLs** with expiration to control access
- Monitor usage with **Cloud Billing alerts**

### 3. MongoDB Cost Optimization

- Create **indexes** on frequently queried fields
- Use **projection** to fetch only needed fields
- Implement **connection pooling** (already configured)
- Archive old data to cheaper storage

```typescript
// Example: Efficient MongoDB query with projection
async findCustomerBasicInfo(ssn: string) {
  const collection = await this.getCollection();
  return await collection.findOne(
    { socialSecurityNumber: ssn },
    { projection: { firstName: 1, lastName: 1, email: 1 } }
  );
}
```

---

## Troubleshooting Guide

### Common Issues and Solutions

#### Issue 1: Firebase Connection Timeout

**Symptoms**: "Firebase initialization failed" error

**Solutions**:
1. Check environment variables are set correctly
2. Verify Firebase project configuration
3. Check network connectivity
4. Ensure Firebase SDK version compatibility

```bash
# Debug Firebase configuration
npm run dev
# Check browser console for Firebase errors
```

#### Issue 2: MongoDB Connection Refused

**Symptoms**: "ECONNREFUSED" error

**Solutions**:
1. Verify MongoDB is running: `mongosh`
2. Check connection string in `.env.local`
3. Verify network access (firewall, security groups)
4. Check MongoDB authentication credentials

```bash
# Test MongoDB connection
mongosh "mongodb://localhost:27017/surptech_banking"
```

#### Issue 3: GCP Authentication Failed

**Symptoms**: "Could not load credentials" error

**Solutions**:
1. Verify service account JSON file exists
2. Check `GCP_CREDENTIALS_PATH` environment variable
3. Ensure service account has required permissions
4. Verify GCP project ID is correct

```bash
# Test GCP credentials
gcloud auth application-default login
```


---

## Migration Strategy

### Migrating from Current Architecture to Cloud-Integrated Architecture

#### Phase 1: Parallel Implementation (No Breaking Changes)

1. **Add cloud services alongside existing backend**
   - Keep existing `DataAggregatorClient` unchanged
   - Add new Firebase/GCP/MongoDB services
   - No changes to existing components

2. **Implement feature flags**

```typescript
// config/features.config.ts
export const featureFlags = {
  useFirebaseAuth: process.env.NEXT_PUBLIC_USE_FIREBASE_AUTH === 'true',
  useMongoDBCache: process.env.NEXT_PUBLIC_USE_MONGODB_CACHE === 'true',
  useGCPStorage: process.env.NEXT_PUBLIC_USE_GCP_STORAGE === 'true',
};

// Usage in service
import { featureFlags } from '@/config/features.config';

async getCustomerInfo(ssn: string) {
  if (featureFlags.useMongoDBCache) {
    return await this.getFromMongoDBCache(ssn);
  }
  return await this.getFromBackendAPI(ssn);
}
```

#### Phase 2: Gradual Migration

1. **Start with non-critical features**
   - File uploads → GCP Storage
   - User preferences → Firestore
   - Analytics → Firebase Analytics

2. **Monitor and validate**
   - Compare results between old and new systems
   - Monitor performance metrics
   - Track error rates

3. **Migrate critical features**
   - Authentication → Firebase Auth
   - Customer data caching → MongoDB
   - Transaction logs → Firestore

#### Phase 3: Deprecation

1. **Remove old code**
   - Remove feature flags
   - Delete unused services
   - Update documentation

2. **Final validation**
   - Run full test suite
   - Perform load testing
   - Security audit

---

## Best Practices Summary

### Code Organization

✅ **DO:**
- Keep cloud service code in dedicated directories (`lib/firebase`, `lib/gcp`, `lib/mongodb`)
- Use singleton pattern for service instances
- Follow existing layered architecture
- Implement proper error handling
- Add comprehensive TypeScript types

❌ **DON'T:**
- Mix cloud service logic with UI components
- Hardcode credentials in code
- Skip error handling
- Ignore TypeScript warnings
- Expose sensitive operations to client-side

### Security

✅ **DO:**
- Use environment variables for all credentials
- Implement proper authentication
- Validate all user inputs
- Use HTTPS in production
- Implement rate limiting
- Mask sensitive data in logs

❌ **DON'T:**
- Commit credentials to git
- Trust client-side validation alone
- Expose admin operations to public
- Store sensitive data in localStorage
- Skip security audits

### Performance

✅ **DO:**
- Implement caching strategies
- Use connection pooling
- Optimize database queries
- Implement pagination
- Use lazy loading
- Monitor performance metrics

❌ **DON'T:**
- Fetch all data at once
- Skip indexing in databases
- Ignore memory leaks
- Over-fetch data
- Skip performance testing


---

## Additional Resources

### Official Documentation

- **Firebase**: https://firebase.google.com/docs
- **Google Cloud Platform**: https://cloud.google.com/docs
- **MongoDB**: https://www.mongodb.com/docs
- **Next.js**: https://nextjs.org/docs
- **React**: https://react.dev

### Tutorials and Guides

- [Firebase Authentication with Next.js](https://firebase.google.com/docs/auth/web/start)
- [GCP Storage with Node.js](https://cloud.google.com/storage/docs/reference/libraries#client-libraries-install-nodejs)
- [MongoDB with Next.js](https://www.mongodb.com/developer/languages/javascript/nextjs-with-mongodb/)
- [Next.js API Routes](https://nextjs.org/docs/app/building-your-application/routing/route-handlers)

### Community Resources

- Firebase Discord: https://discord.gg/firebase
- GCP Community: https://www.googlecloudcommunity.com/
- MongoDB Community: https://www.mongodb.com/community/forums/
- Next.js Discussions: https://github.com/vercel/next.js/discussions

---

## Conclusion

This integration baseline provides a comprehensive foundation for adding Firebase, Google Cloud Platform, and MongoDB to the SurpTech Banking Frontend. The implementation follows the existing architectural patterns and maintains consistency with the current codebase.

### Key Takeaways

1. **Layered Architecture**: All cloud integrations follow the existing service-client-repository pattern
2. **Security First**: Credentials are managed through environment variables and server-side operations
3. **Gradual Migration**: Feature flags allow for safe, incremental adoption
4. **Performance Optimized**: Caching, connection pooling, and efficient queries are built-in
5. **Production Ready**: Includes monitoring, logging, deployment, and troubleshooting guides

### Next Steps

1. Review this baseline with your team
2. Set up development environments for Firebase, GCP, and MongoDB
3. Start with Phase 1 of the implementation roadmap
4. Implement feature flags for safe testing
5. Monitor and iterate based on real-world usage

### Support

For questions or issues during implementation:
- Review the troubleshooting guide
- Check official documentation
- Consult with cloud platform support
- Review existing backend architecture for patterns

---

**Document Version**: 1.0  
**Last Updated**: 2026-05-26  
**Author**: SurpTech Development Team  
**Status**: Baseline for Implementation
