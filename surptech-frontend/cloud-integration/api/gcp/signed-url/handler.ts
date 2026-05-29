/**
 * GCP Signed URL Handler
 *
 * GET /api/cloud/gcp/signed-url?filename=...&expires=...&action=read|write
 *
 * Registered via app/api/cloud/gcp/signed-url/route.ts (Next.js requirement).
 */

import { NextRequest, NextResponse } from 'next/server';
import { generateSignedURL } from '@/cloud-integration/lib/gcp/storage';

export async function GET(request: NextRequest) {
  try {
    const searchParams = request.nextUrl.searchParams;
    const filename = searchParams.get('filename');
    const expiresInMinutes = parseInt(searchParams.get('expires') || '15');
    const action = (searchParams.get('action') || 'read') as 'read' | 'write';

    if (!filename) {
      return NextResponse.json({ error: 'Filename is required' }, { status: 400 });
    }

    const maxExpiration = 7 * 24 * 60; // 7 days in minutes
    if (expiresInMinutes > maxExpiration) {
      return NextResponse.json(
        { error: 'Expiration time cannot exceed 7 days' },
        { status: 400 }
      );
    }

    const signedURL = await generateSignedURL(filename, expiresInMinutes, action);

    return NextResponse.json({
      success: true,
      signedURL,
      filename,
      expiresInMinutes,
      action,
    });
  } catch (error) {
    console.error('Signed URL generation error:', error);
    return NextResponse.json({ error: 'Failed to generate signed URL' }, { status: 500 });
  }
}
