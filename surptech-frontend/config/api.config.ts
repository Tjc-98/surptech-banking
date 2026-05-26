/**
 * API Configuration for the SurpTech Banking Frontend.
 * 
 * Centralizes all API-related configuration including base URLs,
 * endpoints, and connection settings.
 */

/**
 * Determines if we should use the Next.js API proxy or direct backend calls.
 * 
 * In browser environments, we use the Next.js API proxy to avoid CORS issues.
 * In server-side rendering, we can call the backend directly.
 */
const isBrowser = typeof window !== 'undefined';

/**
 * Gets the data aggregator base URL from environment variables.
 * 
 * For browser requests: Uses Next.js API routes as a proxy (same origin, no CORS)
 * For server requests: Calls backend directly
 */
export const getDataAggregatorBaseUrl = (): string => {
  // In browser, use Next.js API proxy to avoid CORS
  if (isBrowser) {
    return ''; // Empty string means same origin (Next.js API routes)
  }
  
  // Server-side: call backend directly
  const host = process.env.DATA_AGGREGATOR_HOST || 
               process.env.NEXT_PUBLIC_DATA_AGGREGATOR_HOST || 
               'localhost';
  const port = process.env.DATA_AGGREGATOR_PORT || 
               process.env.NEXT_PUBLIC_DATA_AGGREGATOR_PORT || 
               '5555';
  
  return `http://${host}:${port}`;
};

/**
 * API Configuration object containing all endpoint definitions.
 */
export const ApiConfig = {
  /** Base URL for the data aggregator service */
  baseUrl: getDataAggregatorBaseUrl(),
  
  /** 
   * Context path for the data aggregator service.
   * When using Next.js API proxy (browser), this is '/api'
   * When calling backend directly (server), this is '/data-aggregator'
   */
  contextPath: isBrowser ? '/api' : '/data-aggregator',
  
  /** API endpoints */
  endpoints: {
    /** Get customer credit information by SSN */
    customerInfo: '/customer/info',
    
    /** Health check endpoint */
    health: '/management/health',
    
    /** External services health check */
    servicesHealth: '/services/management/health',
  },
  
  /** Request timeout in milliseconds */
  timeout: 30000,
  
  /** Default headers for all requests */
  defaultHeaders: {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
  },
} as const;

/**
 * Constructs the full URL for a given endpoint.
 * 
 * @param endpoint - The endpoint path (e.g., '/customer/info')
 * @returns The complete URL including base URL and context path
 */
export const buildApiUrl = (endpoint: string): string => {
  return `${ApiConfig.baseUrl}${ApiConfig.contextPath}${endpoint}`;
};
