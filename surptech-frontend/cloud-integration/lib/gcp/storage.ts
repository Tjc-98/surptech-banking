/**
 * Google Cloud Storage Service
 *
 * Server-side only — never import this in client components.
 * @see https://cloud.google.com/storage/docs
 */

import { Storage, Bucket, File } from '@google-cloud/storage';
import { gcpConfig, validateGCPConfig } from '@/cloud-integration/config/gcp.config';

let storageInstance: Storage | null = null;
let bucketInstance: Bucket | null = null;

export function getGCPStorage(): Storage {
  if (storageInstance) return storageInstance;

  validateGCPConfig();

  storageInstance = new Storage({
    projectId: gcpConfig.projectId,
    keyFilename: gcpConfig.keyFilename,
  });

  return storageInstance;
}

export function getGCPBucket(): Bucket {
  if (bucketInstance) return bucketInstance;
  bucketInstance = getGCPStorage().bucket(gcpConfig.bucketName);
  return bucketInstance;
}

export async function uploadFile(
  file: Buffer | string,
  destination: string,
  metadata?: Record<string, string>
): Promise<File> {
  const bucket = getGCPBucket();

  if (typeof file === 'string') {
    await bucket.upload(file, {
      destination,
      metadata: metadata ? { metadata } : undefined,
    });
  } else {
    const fileRef = bucket.file(destination);
    await fileRef.save(file, {
      metadata: metadata ? { metadata } : undefined,
    });
  }

  return bucket.file(destination);
}

export async function downloadFile(filename: string): Promise<Buffer> {
  const [buffer] = await getGCPBucket().file(filename).download();
  return buffer;
}

export async function deleteFile(filename: string): Promise<void> {
  await getGCPBucket().file(filename).delete();
}

export async function generateSignedURL(
  filename: string,
  expiresInMinutes: number = 15,
  action: 'read' | 'write' = 'read'
): Promise<string> {
  const [url] = await getGCPBucket().file(filename).getSignedUrl({
    version: 'v4',
    action,
    expires: Date.now() + expiresInMinutes * 60 * 1000,
  });
  return url;
}

export async function listFiles(prefix?: string): Promise<string[]> {
  const [files] = await getGCPBucket().getFiles({ prefix });
  return files.map((f) => f.name);
}

export async function fileExists(filename: string): Promise<boolean> {
  const [exists] = await getGCPBucket().file(filename).exists();
  return exists;
}

export async function getFileMetadata(filename: string): Promise<Record<string, unknown>> {
  const [metadata] = await getGCPBucket().file(filename).getMetadata();
  return metadata;
}

export async function makeFilePublic(filename: string): Promise<void> {
  await getGCPBucket().file(filename).makePublic();
}

export function getPublicURL(filename: string): string {
  return `https://storage.googleapis.com/${gcpConfig.bucketName}/${filename}`;
}
