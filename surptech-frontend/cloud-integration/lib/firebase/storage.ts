/**
 * Firebase Storage Service
 *
 * File upload, download, deletion, and URL generation.
 * @see https://firebase.google.com/docs/storage
 */

import {
  FirebaseStorage,
  getStorage,
  ref,
  uploadBytes,
  uploadBytesResumable,
  getDownloadURL,
  deleteObject,
  listAll,
  StorageReference,
  UploadTask,
  UploadMetadata,
} from 'firebase/storage';
import { getFirebaseApp } from './firebase';

export function getFirebaseStorage(): FirebaseStorage {
  return getStorage(getFirebaseApp());
}

export async function uploadFile(
  file: File | Blob,
  path: string,
  metadata?: UploadMetadata
): Promise<{ url: string; ref: StorageReference }> {
  const storageRef = ref(getFirebaseStorage(), path);
  const result = await uploadBytes(storageRef, file, metadata);
  const url = await getDownloadURL(result.ref);
  return { url, ref: result.ref };
}

export function uploadFileWithProgress(
  file: File | Blob,
  path: string,
  onProgress?: (progress: number) => void,
  metadata?: UploadMetadata
): UploadTask {
  const storageRef = ref(getFirebaseStorage(), path);
  const uploadTask = uploadBytesResumable(storageRef, file, metadata);

  if (onProgress) {
    uploadTask.on('state_changed', (snapshot) => {
      onProgress((snapshot.bytesTransferred / snapshot.totalBytes) * 100);
    });
  }

  return uploadTask;
}

export async function getFileURL(path: string): Promise<string> {
  return await getDownloadURL(ref(getFirebaseStorage(), path));
}

export async function deleteFile(path: string): Promise<void> {
  await deleteObject(ref(getFirebaseStorage(), path));
}

export async function listFiles(path: string): Promise<StorageReference[]> {
  const result = await listAll(ref(getFirebaseStorage(), path));
  return result.items;
}

export async function listFilesWithURLs(
  path: string
): Promise<Array<{ ref: StorageReference; url: string; name: string }>> {
  const files = await listFiles(path);
  return await Promise.all(
    files.map(async (fileRef) => ({
      ref: fileRef,
      url: await getDownloadURL(fileRef),
      name: fileRef.name,
    }))
  );
}

export function generateUniqueFilePath(directory: string, filename: string): string {
  const timestamp = Date.now();
  const randomString = Math.random().toString(36).substring(2, 8);
  const extension = filename.split('.').pop();
  const nameWithoutExtension = filename.replace(`.${extension}`, '');
  return `${directory}/${nameWithoutExtension}_${timestamp}_${randomString}.${extension}`;
}
