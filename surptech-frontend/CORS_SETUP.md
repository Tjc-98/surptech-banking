# CORS Configuration Guide

## Problem

When the frontend (running on `http://localhost:3000`) tries to make requests directly to the backend (running on `http://localhost:5555`), browsers block these requests due to CORS (Cross-Origin Resource Sharing) policy.

## Current Solution: Next.js API Proxy

The frontend now uses Next.js API routes as a proxy to avoid CORS issues:

```
Browser → Next.js API Route → Backend Service
(3000)    (3000/api/*)         (5555)
```

**How it works:**
1. Browser makes request to `http://localhost:3000/api/customer/info`
2. Next.js API route (`app/api/customer/info/route.ts`) receives the request
3. API route makes server-side request to `http://localhost:5555/data-aggregator/customer/info`
4. Backend responds to Next.js
5. Next.js forwards response to browser

**Advantages:**
- ✅ No CORS issues (same origin)
- ✅ No backend changes required
- ✅ Works immediately

**Disadvantages:**
- ⚠️ Extra hop (slight latency)
- ⚠️ Next.js server must be running

## Alternative Solution: Backend CORS Configuration

For production or if you prefer direct backend calls, configure CORS on the backend services.

### Option 1: Spring Boot CORS Configuration (Recommended)

Add a CORS configuration class to each backend service:

**File**: `src/main/java/org/surptech/[service]/config/CorsConfiguration.java`

```java
package org.surptech.[service].config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * CORS Configuration for allowing frontend access.
 */
@Configuration
public class WebCorsConfiguration {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // Allow frontend origin
        config.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",  // Development
            "https://yourdomain.com"  // Production
        ));
        
        // Allow specific HTTP methods
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // Allow specific headers
        config.setAllowedHeaders(Arrays.asList("*"));
        
        // Allow credentials (cookies, authorization headers)
        config.setAllowCredentials(true);
        
        // Cache preflight response for 1 hour
        config.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}
```

**Apply to these services:**
- `data-aggregator` (most important)
- `customer-profile` (if accessed directly)
- `credit-profile` (if accessed directly)

### Option 2: Controller-Level CORS

Add `@CrossOrigin` annotation to controllers:

```java
@RestController
@RequestMapping("/customer")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CustomerBankingController extends BaseController {
    // ... existing code
}
```

### Option 3: Global CORS in Application Properties

Add to `application.properties`:

```properties
# CORS Configuration
spring.web.cors.allowed-origins=http://localhost:3000
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
spring.web.cors.allowed-headers=*
spring.web.cors.allow-credentials=true
spring.web.cors.max-age=3600
```

## Switching Between Solutions

### Using Next.js Proxy (Current - Default)

No changes needed. The frontend is already configured to use the proxy.

**Configuration**: `config/api.config.ts`
```typescript
// Browser requests go to Next.js API routes
const isBrowser = typeof window !== 'undefined';
export const getDataAggregatorBaseUrl = (): string => {
  if (isBrowser) {
    return ''; // Same origin (Next.js API)
  }
  // Server-side calls backend directly
  return `http://${host}:${port}`;
};
```

### Using Direct Backend Calls (After CORS Setup)

If you configure CORS on the backend, you can switch to direct calls:

**Update**: `config/api.config.ts`
```typescript
export const getDataAggregatorBaseUrl = (): string => {
  const host = process.env.NEXT_PUBLIC_DATA_AGGREGATOR_HOST || 'localhost';
  const port = process.env.NEXT_PUBLIC_DATA_AGGREGATOR_PORT || '5555';
  return `http://${host}:${port}`;
};

export const ApiConfig = {
  baseUrl: getDataAggregatorBaseUrl(),
  contextPath: '/data-aggregator', // Direct backend path
  // ... rest of config
};
```

**Note**: Use `NEXT_PUBLIC_` prefix for environment variables that need to be available in the browser.

## Testing CORS Configuration

### Test with curl

```bash
# Test preflight request
curl -X OPTIONS http://localhost:5555/data-aggregator/customer/info \
  -H "Origin: http://localhost:3000" \
  -H "Access-Control-Request-Method: GET" \
  -v

# Should return:
# Access-Control-Allow-Origin: http://localhost:3000
# Access-Control-Allow-Methods: GET, POST, ...
```

### Test with browser console

```javascript
fetch('http://localhost:5555/data-aggregator/customer/info?socialSecurityNumber=123-45-6789')
  .then(response => response.json())
  .then(data => console.log(data))
  .catch(error => console.error('CORS Error:', error));
```

If CORS is configured correctly, this should work without errors.

## Production Considerations

### Security Best Practices

1. **Specific Origins**: Don't use `*` for allowed origins
   ```java
   config.setAllowedOrigins(Arrays.asList("https://yourdomain.com"));
   ```

2. **Specific Methods**: Only allow needed HTTP methods
   ```java
   config.setAllowedMethods(Arrays.asList("GET", "POST"));
   ```

3. **Specific Headers**: Limit allowed headers
   ```java
   config.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
   ```

4. **Environment-Based**: Use different origins for dev/prod
   ```java
   @Value("${cors.allowed.origins}")
   private String allowedOrigins;
   ```

### Environment Variables

**Backend** (`application.properties`):
```properties
# Development
cors.allowed.origins=http://localhost:3000

# Production
cors.allowed.origins=https://banking.yourdomain.com
```

**Frontend** (`.env.local`):
```env
# Development
NEXT_PUBLIC_DATA_AGGREGATOR_HOST=localhost
NEXT_PUBLIC_DATA_AGGREGATOR_PORT=5555

# Production
NEXT_PUBLIC_DATA_AGGREGATOR_HOST=api.yourdomain.com
NEXT_PUBLIC_DATA_AGGREGATOR_PORT=443
```

## Troubleshooting

### "No 'Access-Control-Allow-Origin' header"

**Problem**: CORS not configured on backend

**Solution**: 
1. Add CORS configuration to backend (see above)
2. Or use Next.js proxy (current default)

### "CORS policy: credentials mode is 'include'"

**Problem**: Credentials required but not allowed

**Solution**: Set `allowCredentials = true` in CORS config

### "Method not allowed"

**Problem**: HTTP method not in allowed methods

**Solution**: Add method to `allowedMethods` list

### Preflight request fails

**Problem**: OPTIONS request not handled

**Solution**: Ensure CORS filter handles OPTIONS requests

## Recommended Approach

**Development**: Use Next.js proxy (current setup)
- Simple, no backend changes
- Works immediately

**Production**: Configure CORS on backend
- Better performance (no extra hop)
- More control over security
- Standard practice for microservices

## Current Status

✅ **Next.js API proxy is configured and working**

The frontend is currently using the proxy approach, so CORS configuration on the backend is optional but recommended for production deployments.
