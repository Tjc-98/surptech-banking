'use client';

/**
 * useFirebaseStorage Hook
 *
 * File uploads with progress tracking, deletion, and listing.
 */

import { useState } from 'react';
import { StorageReference } from 'firebase/storage';
import {
  uploadFile,
  uploadFileWithProgress,
  getFileURL,
  deleteFile,
  listFilesWithURLs,
} from '@/cloud-integration/lib/firebase/storage';

export function useFirebaseStorage() {
  const [uploading, setUploading] = useState(false);
  const [progress, setProgress] = useState(0);
  const [error, setError] = useState<Error | null>(null);
  const [uploadedURL, setUploadedURL] = useState<string | null>(null);

  const upload = async (file: File, path: string): Promise<string> => {
    try {
      setUploading(true); setError(null); setProgress(0);
      const result = await uploadFile(file, path);
      setUploadedURL(result.url);
      setProgress(100);
      return result.url;
    } catch (err) {
      setError(err as Error); throw err;
    } finally {
      setUploading(false);
    }
  };

  const uploadWithProgress = (file: File, path: string): Promise<string> => {
    setUploading(true); setError(null); setProgress(0);

    return new Promise((resolve, reject) => {
      const task = uploadFileWithProgress(file, path, (p) => setProgress(p));

      task.on(
        'state_changed',
        null,
        (err) => { setError(err as Error); setUploading(false); reject(err); },
        async () => {
          try {
            const url = await getFileURL(path);
            setUploadedURL(url); setUploading(false); resolve(url);
          } catch (err) {
            setError(err as Error); setUploading(false); reject(err);
          }
        }
      );
    });
  };

  const remove = async (path: string): Promise<void> => {
    try {
      setError(null);
      await deleteFile(path);
      if (uploadedURL?.includes(path)) setUploadedURL(null);
    } catch (err) {
      setError(err as Error); throw err;
    }
  };

  const getURL = async (path: string): Promise<string> => {
    try { setError(null); return await getFileURL(path); }
    catch (err) { setError(err as Error); throw err; }
  };

  const list = async (
    path: string
  ): Promise<Array<{ ref: StorageReference; url: string; name: string }>> => {
    try { setError(null); return await listFilesWithURLs(path); }
    catch (err) { setError(err as Error); throw err; }
  };

  return {
    uploading,
    progress,
    error,
    uploadedURL,
    uploadFile: upload,
    uploadFileWithProgress: uploadWithProgress,
    deleteFile: remove,
    getFileURL: getURL,
    listFiles: list,
  };
}
