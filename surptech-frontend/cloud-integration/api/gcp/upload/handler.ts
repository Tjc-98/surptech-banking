/**
 * GCP File Upload Handler
 *
 * POST /api/cloud/gcp/upload  — upload a file to GCP Cloud Storage
 * GET  /api/cloud/gcp/upload  — endpoint info
 *
 * Registered via app/api/cloud/gcp/upload/route.ts (Next.js requirement).
 */

import { NextRequest, NextResponse } from 'next/server';
import { uploadFile, generateSignedURL } from '@/cloud-integration/lib/gcp/storage';

export async function POST(request: NextRequest) {
  try {
    const formData = await request.formData();
    const file = formData.get('file') as File;

    if (!file) {
      return NextResponse.json({ error: 'No file provided' }, { status: 400 });
    }

    const maxSize = 10 * 1024 * 1024; // 10 MB
    if (file.size > maxSize) {
      return NextResponse.json(
        { error: 'File size exceeds 10MB limit' },
        { status: 400 }
      );
    }

    const timestamp = Date.now();
    const randomString = Math.random().toString(36).substring(2, 8);
    const extension = file.name.split('.').pop();
    const filename = `uploads/${timestamp}_${randomString}.${extension}`;

    const buffer = Buffer.from(await file.arrayBuffer());

    await uploadFile(buffer, filename, {
      originalName: file.name,
      contentType: file.type,
      uploadedAt: new Date().toISOString(),
    });

    const signedURL = await generateSignedURL(filename, 60);

    return NextResponse.json({
      success: true,
      filename,
      signedURL,
      size: file.size,
      contentType: file.type,
    });
  } catch (error) {
    console.error('Upload error:', error);
    return NextResponse.json({ error: 'Failed to upload file' }, { status: 500 });
  }
}

export async function GET() {
  return NextResponse.json({
    endpoint: '/api/cloud/gcp/upload',
    method: 'POST',
    contentType: 'multipart/form-data',
    maxFileSize: '10MB',
    acceptedFormats: 'all',
  });
}
