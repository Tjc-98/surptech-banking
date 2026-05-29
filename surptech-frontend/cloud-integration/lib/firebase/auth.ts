/**
 * Firebase Authentication Service
 *
 * Supports email/password authentication and user management.
 * @see https://firebase.google.com/docs/auth
 */

import {
  Auth,
  getAuth,
  createUserWithEmailAndPassword,
  signInWithEmailAndPassword,
  signOut as firebaseSignOut,
  onAuthStateChanged,
  User,
  UserCredential,
  updateProfile,
  sendPasswordResetEmail,
  sendEmailVerification,
} from 'firebase/auth';
import { getFirebaseApp } from './firebase';

export function getFirebaseAuth(): Auth {
  return getAuth(getFirebaseApp());
}

export async function signUp(
  email: string,
  password: string,
  displayName?: string
): Promise<UserCredential> {
  const auth = getFirebaseAuth();
  const userCredential = await createUserWithEmailAndPassword(auth, email, password);

  if (displayName && userCredential.user) {
    await updateProfile(userCredential.user, { displayName });
  }

  return userCredential;
}

export async function signIn(
  email: string,
  password: string
): Promise<UserCredential> {
  return await signInWithEmailAndPassword(getFirebaseAuth(), email, password);
}

export async function signOut(): Promise<void> {
  await firebaseSignOut(getFirebaseAuth());
}

export function getCurrentUser(): User | null {
  return getFirebaseAuth().currentUser;
}

export function onAuthStateChange(
  callback: (user: User | null) => void
): () => void {
  return onAuthStateChanged(getFirebaseAuth(), callback);
}

export async function resetPassword(email: string): Promise<void> {
  await sendPasswordResetEmail(getFirebaseAuth(), email);
}

export async function sendVerificationEmail(): Promise<void> {
  const user = getCurrentUser();
  if (!user) throw new Error('No user is currently signed in');
  await sendEmailVerification(user);
}

export async function updateUserProfile(updates: {
  displayName?: string;
  photoURL?: string;
}): Promise<void> {
  const user = getCurrentUser();
  if (!user) throw new Error('No user is currently signed in');
  await updateProfile(user, updates);
}
