/**
 * Next.js API Route - Customer Info Proxy
 * 
 * This API route acts as a proxy between the frontend and the data-aggregator service.
 * It solves CORS issues by making server-side requests to the backend.
 * 
 * The browser makes requests to this Next.js API route (same origin),
 * and this route forwards them to the backend service.
 */

import { NextRequest, NextResponse } from 'next/server';

/**
 * GET handler for customer information requests.
 * 
 * Proxies requests from the frontend to the data-aggregator service.
 */
export async function GET(request: NextRequest) {
  try {
    // Extract SSN from query parameters
    const searchParams = request.nextUrl.searchParams;
    const socialSecurityNumber = searchParams.get('socialSecurityNumber');
    
    if (!socialSecurityNumber) {
      return NextResponse.json(
        { error: 'Social Security Number is required' },
        { status: 400 }
      );
    }
    
    // Build backend URL
    const host = process.env.DATA_AGGREGATOR_HOST || 'localhost';
    const port = process.env.DATA_AGGREGATOR_PORT || '5555';
    const backendUrl = `http://${host}:${port}/data-aggregator/customer/info?socialSecurityNumber=${encodeURIComponent(socialSecurityNumber)}`;
    
    // Forward request to backend
    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), 30000);
    
    const response = await fetch(backendUrl, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
      },
      signal: controller.signal,
    });
    
    clearTimeout(timeoutId);
    
    // Handle backend response
    if (!response.ok) {
      const errorText = await response.text().catch(() => 'Unknown error');
      return NextResponse.json(
        { error: errorText || `Backend returned ${response.status}` },
        { status: response.status }
      );
    }
    
    // Parse and return successful response
    const data = await response.json();
    return NextResponse.json(data, { status: 200 });
    
  } catch (error) {
    console.error('Proxy error:', error);
    
    if (error instanceof Error) {
      if (error.name === 'AbortError') {
        return NextResponse.json(
          { error: 'Request timeout' },
          { status: 408 }
        );
      }
      
      return NextResponse.json(
        { error: `Failed to connect to backend: ${error.message}` },
        { status: 503 }
      );
    }
    
    return NextResponse.json(
      { error: 'Internal server error' },
      { status: 500 }
    );
  }
}
