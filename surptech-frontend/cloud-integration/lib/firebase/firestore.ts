/**
 * Firebase Firestore Service
 *
 * CRUD operations and real-time listeners.
 * @see https://firebase.google.com/docs/firestore
 */

import {
  Firestore,
  getFirestore,
  collection,
  doc,
  getDoc,
  getDocs,
  addDoc,
  setDoc,
  updateDoc,
  deleteDoc,
  query,
  where,
  orderBy,
  limit,
  QueryConstraint,
  DocumentData,
  DocumentReference,
  onSnapshot,
  Unsubscribe,
} from 'firebase/firestore';
import { getFirebaseApp } from './firebase';

export function getFirestoreDb(): Firestore {
  return getFirestore(getFirebaseApp());
}

export async function getDocument<T = DocumentData>(
  collectionName: string,
  documentId: string
): Promise<T | null> {
  const docRef = doc(getFirestoreDb(), collectionName, documentId);
  const docSnap = await getDoc(docRef);
  return docSnap.exists() ? ({ id: docSnap.id, ...docSnap.data() } as T) : null;
}

export async function getDocuments<T = DocumentData>(
  collectionName: string,
  ...constraints: QueryConstraint[]
): Promise<T[]> {
  const db = getFirestoreDb();
  const collectionRef = collection(db, collectionName);
  const q = constraints.length > 0 ? query(collectionRef, ...constraints) : collectionRef;
  const snapshot = await getDocs(q);
  return snapshot.docs.map((d) => ({ id: d.id, ...d.data() })) as T[];
}

export async function addDocument<T = DocumentData>(
  collectionName: string,
  data: T
): Promise<DocumentReference> {
  return await addDoc(collection(getFirestoreDb(), collectionName), data as DocumentData);
}

export async function setDocument<T = DocumentData>(
  collectionName: string,
  documentId: string,
  data: T
): Promise<void> {
  await setDoc(doc(getFirestoreDb(), collectionName, documentId), data as DocumentData);
}

export async function updateDocument<T = DocumentData>(
  collectionName: string,
  documentId: string,
  data: Partial<T>
): Promise<void> {
  await updateDoc(doc(getFirestoreDb(), collectionName, documentId), data as DocumentData);
}

export async function deleteDocument(
  collectionName: string,
  documentId: string
): Promise<void> {
  await deleteDoc(doc(getFirestoreDb(), collectionName, documentId));
}

export function subscribeToDocument<T = DocumentData>(
  collectionName: string,
  documentId: string,
  callback: (data: T | null) => void
): Unsubscribe {
  const docRef = doc(getFirestoreDb(), collectionName, documentId);
  return onSnapshot(docRef, (snap) => {
    callback(snap.exists() ? ({ id: snap.id, ...snap.data() } as T) : null);
  });
}

export function subscribeToCollection<T = DocumentData>(
  collectionName: string,
  callback: (data: T[]) => void,
  ...constraints: QueryConstraint[]
): Unsubscribe {
  const db = getFirestoreDb();
  const collectionRef = collection(db, collectionName);
  const q = constraints.length > 0 ? query(collectionRef, ...constraints) : collectionRef;
  return onSnapshot(q, (snapshot) => {
    callback(snapshot.docs.map((d) => ({ id: d.id, ...d.data() })) as T[]);
  });
}

// Re-export query helpers for convenience
export { where, orderBy, limit };
