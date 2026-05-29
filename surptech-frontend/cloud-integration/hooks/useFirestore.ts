'use client';

/**
 * useFirestore Hooks
 *
 * Hooks for reading and mutating Firestore documents and collections.
 */

import { useState, useEffect } from 'react';
import { DocumentData, QueryConstraint } from 'firebase/firestore';
import {
  getDocument,
  getDocuments,
  addDocument,
  updateDocument,
  deleteDocument,
  subscribeToDocument,
  subscribeToCollection,
} from '@/cloud-integration/lib/firebase/firestore';

// ─── Document hook ────────────────────────────────────────────────────────────

export function useFirestoreDocument<T = DocumentData>(
  collectionName: string,
  documentId: string,
  realtime = false
) {
  const [data, setData] = useState<T | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  const fetchDocument = async () => {
    try {
      setLoading(true);
      setError(null);
      setData(await getDocument<T>(collectionName, documentId));
    } catch (err) {
      setError(err as Error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (!realtime) {
      fetchDocument();
      return;
    }
    const unsubscribe = subscribeToDocument<T>(collectionName, documentId, (doc) => {
      setData(doc);
      setLoading(false);
    });
    return () => unsubscribe();
  }, [collectionName, documentId, realtime]);

  return { data, loading, error, refresh: fetchDocument };
}

// ─── Collection hook ──────────────────────────────────────────────────────────

export function useFirestoreCollection<T = DocumentData>(
  collectionName: string,
  constraints: QueryConstraint[] = [],
  realtime = false
) {
  const [data, setData] = useState<T[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  const fetchCollection = async () => {
    try {
      setLoading(true);
      setError(null);
      setData(await getDocuments<T>(collectionName, ...constraints));
    } catch (err) {
      setError(err as Error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (!realtime) {
      fetchCollection();
      return;
    }
    const unsubscribe = subscribeToCollection<T>(
      collectionName,
      (docs) => { setData(docs); setLoading(false); },
      ...constraints
    );
    return () => unsubscribe();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [collectionName, JSON.stringify(constraints), realtime]);

  return { data, loading, error, refresh: fetchCollection };
}

// ─── Mutations hook ───────────────────────────────────────────────────────────

export function useFirestoreMutations<T = DocumentData>(collectionName: string) {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<Error | null>(null);

  const add = async (data: T) => {
    try { setLoading(true); setError(null); return await addDocument(collectionName, data); }
    catch (err) { setError(err as Error); throw err; }
    finally { setLoading(false); }
  };

  const update = async (documentId: string, data: Partial<T>) => {
    try { setLoading(true); setError(null); await updateDocument(collectionName, documentId, data); }
    catch (err) { setError(err as Error); throw err; }
    finally { setLoading(false); }
  };

  const remove = async (documentId: string) => {
    try { setLoading(true); setError(null); await deleteDocument(collectionName, documentId); }
    catch (err) { setError(err as Error); throw err; }
    finally { setLoading(false); }
  };

  return { add, update, remove, loading, error };
}
