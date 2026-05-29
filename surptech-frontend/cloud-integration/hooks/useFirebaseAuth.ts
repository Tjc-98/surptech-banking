'use client';

/**
 * useFirebaseAuth Hook
 *
 * Manages Firebase authentication state and exposes auth methods.
 */

import { useState, useEffect } from 'react';
import { User } from 'firebase/auth';
import {
  signUp as firebaseSignUp,
  signIn as firebaseSignIn,
  signOut as firebaseSignOut,
  onAuthStateChange,
  getCurrentUser,
  resetPassword as firebaseResetPassword,
  sendVerificationEmail as firebaseSendVerificationEmail,
  updateUserProfile as firebaseUpdateUserProfile,
} from '@/cloud-integration/lib/firebase/auth';

interface UseFirebaseAuthReturn {
  user: User | null;
  loading: boolean;
  error: Error | null;
  signUp: (email: string, password: string, displayName?: string) => Promise<void>;
  signIn: (email: string, password: string) => Promise<void>;
  signOut: () => Promise<void>;
  resetPassword: (email: string) => Promise<void>;
  sendVerificationEmail: () => Promise<void>;
  updateProfile: (updates: { displayName?: string; photoURL?: string }) => Promise<void>;
}

export function useFirebaseAuth(): UseFirebaseAuthReturn {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<Error | null>(null);

  useEffect(() => {
    const unsubscribe = onAuthStateChange((authUser) => {
      setUser(authUser);
      setLoading(false);
    });
    return () => unsubscribe();
  }, []);

  const signUp = async (email: string, password: string, displayName?: string) => {
    try {
      setError(null);
      setLoading(true);
      await firebaseSignUp(email, password, displayName);
    } catch (err) {
      setError(err as Error);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  const signIn = async (email: string, password: string) => {
    try {
      setError(null);
      setLoading(true);
      await firebaseSignIn(email, password);
    } catch (err) {
      setError(err as Error);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  const signOut = async () => {
    try {
      setError(null);
      await firebaseSignOut();
    } catch (err) {
      setError(err as Error);
      throw err;
    }
  };

  const resetPassword = async (email: string) => {
    try {
      setError(null);
      await firebaseResetPassword(email);
    } catch (err) {
      setError(err as Error);
      throw err;
    }
  };

  const sendVerificationEmail = async () => {
    try {
      setError(null);
      await firebaseSendVerificationEmail();
    } catch (err) {
      setError(err as Error);
      throw err;
    }
  };

  const updateProfile = async (updates: { displayName?: string; photoURL?: string }) => {
    try {
      setError(null);
      await firebaseUpdateUserProfile(updates);
      setUser(getCurrentUser());
    } catch (err) {
      setError(err as Error);
      throw err;
    }
  };

  return { user, loading, error, signUp, signIn, signOut, resetPassword, sendVerificationEmail, updateProfile };
}
