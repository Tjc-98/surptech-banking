/**
 * Firebase Initialization
 *
 * Singleton pattern — only one Firebase app instance is created.
 * @see https://firebase.google.com/docs/web/setup
 */

import { FirebaseApp, initializeApp, getApps, getApp } from 'firebase/app';
import { firebaseConfig, validateFirebaseConfig } from '@/cloud-integration/config/firebase.config';

let firebaseApp: FirebaseApp | null = null;

export function initializeFirebase(): FirebaseApp {
  if (firebaseApp) return firebaseApp;

  const existingApps = getApps();
  if (existingApps.length > 0) {
    firebaseApp = getApp();
    return firebaseApp;
  }

  validateFirebaseConfig();
  firebaseApp = initializeApp(firebaseConfig);
  return firebaseApp;
}

export function getFirebaseApp(): FirebaseApp {
  return firebaseApp ?? initializeFirebase();
}

export function isFirebaseInitialized(): boolean {
  return firebaseApp !== null || getApps().length > 0;
}
