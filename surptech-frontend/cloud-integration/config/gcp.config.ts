/**
 * Google Cloud Platform Configuration
 *
 * All sensitive values are loaded from environment variables.
 * @see https://cloud.google.com/docs/authentication
 */

export interface GCPConfig {
  projectId: string;
  keyFilename?: string;
  bucketName: string;
  region?: string;
}

export const gcpConfig: GCPConfig = {
  projectId: process.env.GCP_PROJECT_ID || '',
  keyFilename: process.env.GCP_CREDENTIALS_PATH,
  bucketName: process.env.GCP_BUCKET_NAME || '',
  region: process.env.GCP_REGION || 'us-central1',
};

export function validateGCPConfig(): void {
  const requiredFields: (keyof GCPConfig)[] = ['projectId', 'bucketName'];

  const missingFields = requiredFields.filter((field) => !gcpConfig[field]);

  if (missingFields.length > 0) {
    throw new Error(
      `Missing required GCP configuration: ${missingFields.join(', ')}`
    );
  }
}

export function isGCPConfigured(): boolean {
  try {
    validateGCPConfig();
    return true;
  } catch {
    return false;
  }
}
