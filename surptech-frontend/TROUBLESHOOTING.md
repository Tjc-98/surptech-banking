# Troubleshooting Guide

Common issues and their solutions for the SurpTech Banking Frontend.

## "Unable to connect to the server" Error

### Symptoms
- Error message: "Unable to connect to the server. Please ensure the data aggregator service is running."
- Works in Insomnia/Postman but not in browser
- Browser console shows network errors

### Root Cause
This is typically a **CORS (Cross-Origin Resource Sharing)** issue. Browsers block requests from one origin (localhost:3000) to another (localhost:5555) for security reasons. API testing tools like Insomnia don't enforce CORS.

### Solution (Already Implemented)
The frontend now uses **Next.js API routes as a proxy** to avoid CORS issues:

```
Browser → Next.js API (localhost:3000/api/*) → Backend (localhost:5555)
```

### Verification Steps

1. **Ensure backend is running**
   ```bash
   curl http://localhost:5555/data-aggregator/management/health
   ```
   Should return: `{"status":"UP"}`

2. **Restart Next.js dev server**
   ```bash
   # Stop the server (Ctrl+C)
   npm run dev
   ```

3. **Check Next.js terminal for errors**
   Look for messages like:
   - `Proxy error: ...`
   - `Failed to connect to backend: ...`

4. **Test the proxy endpoint directly**
   ```bash
   curl "http://localhost:3000/api/customer/info?socialSecurityNumber=123-45-6789"
   ```

5. **Check browser console** (F12 → Console tab)
   - Should NOT see CORS errors
   - Should see requests to `/api/customer/info` (not localhost:5555)

### If Still Not Working

**Check 1: Backend services are running**
```bash
# Customer Profile (port 5551)
curl http://localhost:5551/customer-profile/management/health

# Credit Profile (port 5552)
curl http://localhost:5552/credit-profile/management/health

# Data Aggregator (port 5555)
curl http://localhost:5555/data-aggregator/management/health
```

**Check 2: Environment variables**
```bash
# In surptech-frontend directory
cat .env.local

# Should contain (or use defaults):
# DATA_AGGREGATOR_HOST=localhost
# DATA_AGGREGATOR_PORT=5555
```

**Check 3: Port conflicts**
```bash
# Windows
netstat -ano | findstr :3000
netstat -ano | findstr :5555

# If port 3000 is in use by another app, use different port:
npm run dev -- -p 3001
```

**Check 4: Clear Next.js cache**
```bash
rm -rf .next
npm run dev
```

## "Customer not found" Error

### Symptoms
- Error message: "Customer not found. Please check the Social Security Number and try again."
- Backend is running and accessible

### Solution

**Option 1: Create test data**
```bash
# Create customer profile
curl -X POST http://localhost:5551/customer-profile/customer/create \
  -H "Content-Type: application/json" \
  -d '{
    "social_security_number": "123-45-6789",
    "first_name": "John",
    "last_name": "Doe",
    "address": "123 Main St, City, ST 12345"
  }'

# Create credit profile
curl -X POST http://localhost:5552/credit-profile/credit/create \
  -H "Content-Type: application/json" \
  -d '{
    "social_security_number": "123-45-6789",
    "full_credit_balance": 5000.00,
    "spend_balance": 3000.00,
    "interest_rate": 0.15
  }'
```

**Option 2: Check existing data**
```bash
# Query customer directly
curl "http://localhost:5551/customer-profile/customer/get?socialSecurityNumber=123-45-6789"

# Query credit directly
curl "http://localhost:5552/credit-profile/credit/get?socialSecurityNumber=123-45-6789"
```

## Invalid SSN Format

### Symptoms
- Error message: "Social Security Number must be in format XXX-XX-XXXX"
- Form validation error

### Solution
Ensure SSN is in the correct format:
- ✅ Correct: `123-45-6789`
- ❌ Wrong: `123456789`
- ❌ Wrong: `123 45 6789`

The form will auto-format if you enter 9 digits without dashes.

## Port Already in Use

### Symptoms
- Error: `Port 3000 is already in use`
- Next.js won't start

### Solution

**Option 1: Use different port**
```bash
npm run dev -- -p 3001
```

**Option 2: Kill process using port 3000**
```bash
# Windows
netstat -ano | findstr :3000
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:3000 | xargs kill -9
```

## TypeScript Errors

### Symptoms
- Red squiggly lines in editor
- Build fails with type errors

### Solution

**Check types**
```bash
npm run type-check
```

**Reinstall dependencies**
```bash
rm -rf node_modules package-lock.json
npm install
```

**Restart TypeScript server** (VS Code)
- Press `Ctrl+Shift+P`
- Type "TypeScript: Restart TS Server"
- Press Enter

## Styling Not Applied

### Symptoms
- Page looks unstyled
- Tailwind classes not working

### Solution

**Restart dev server**
```bash
# Stop server (Ctrl+C)
npm run dev
```

**Clear Next.js cache**
```bash
rm -rf .next
npm run dev
```

**Verify Tailwind config**
```bash
# Check if tailwind.config.ts exists
ls tailwind.config.ts

# Check if postcss.config.js exists
ls postcss.config.js
```

## Slow Performance

### Symptoms
- Page loads slowly
- Requests take long time

### Solution

**Check backend response time**
```bash
time curl "http://localhost:5555/data-aggregator/customer/info?socialSecurityNumber=123-45-6789"
```

**Check network tab** (F12 → Network)
- Look for slow requests
- Check request/response sizes

**Optimize if needed**
- Use production build: `npm run build && npm start`
- Check backend logs for slow queries

## Module Not Found Errors

### Symptoms
- Error: `Module not found: Can't resolve '@/...'`
- Import errors

### Solution

**Check tsconfig.json paths**
```json
{
  "compilerOptions": {
    "paths": {
      "@/*": ["./*"]
    }
  }
}
```

**Restart dev server**
```bash
npm run dev
```

**Verify file exists**
```bash
# Example: if error is "Can't resolve '@/components/SearchForm'"
ls components/SearchForm.tsx
```

## Environment Variables Not Working

### Symptoms
- API calls go to wrong URL
- Configuration not applied

### Solution

**Use NEXT_PUBLIC_ prefix for browser variables**
```env
# ❌ Wrong (not available in browser)
DATA_AGGREGATOR_HOST=localhost

# ✅ Correct (available in browser)
NEXT_PUBLIC_DATA_AGGREGATOR_HOST=localhost
```

**Restart dev server after changing .env**
```bash
# Environment variables are loaded at startup
npm run dev
```

**Check environment in browser console**
```javascript
console.log(process.env.NEXT_PUBLIC_DATA_AGGREGATOR_HOST);
```

## Build Errors

### Symptoms
- `npm run build` fails
- Production build errors

### Solution

**Check for type errors**
```bash
npm run type-check
```

**Check for lint errors**
```bash
npm run lint
```

**Clear cache and rebuild**
```bash
rm -rf .next
npm run build
```

## Getting More Help

### Enable Debug Logging

**Browser Console** (F12 → Console)
- See network requests
- See error messages
- See component logs

**Next.js Terminal**
- See server-side errors
- See API proxy logs
- See build warnings

### Check Logs

**Backend logs**
```bash
# Check data-aggregator terminal for errors
# Look for stack traces or error messages
```

**Frontend logs**
```bash
# Check Next.js terminal for errors
# Look for compilation errors or warnings
```

### Test Individual Components

**Test backend directly**
```bash
curl "http://localhost:5555/data-aggregator/customer/info?socialSecurityNumber=123-45-6789"
```

**Test Next.js proxy**
```bash
curl "http://localhost:3000/api/customer/info?socialSecurityNumber=123-45-6789"
```

**Test in browser console**
```javascript
fetch('/api/customer/info?socialSecurityNumber=123-45-6789')
  .then(r => r.json())
  .then(console.log)
  .catch(console.error);
```

## Common Error Messages

| Error | Cause | Solution |
|-------|-------|----------|
| "Unable to connect to the server" | Backend not running or CORS | Start backend, restart Next.js |
| "Customer not found" | No data for SSN | Create test data |
| "Invalid SSN format" | Wrong SSN format | Use XXX-XX-XXXX format |
| "Port already in use" | Port conflict | Use different port or kill process |
| "Module not found" | Import path wrong | Check file exists and path is correct |
| "Request timeout" | Backend too slow | Check backend performance |

## Still Having Issues?

1. **Read the documentation**
   - [README.md](README.md) - Main documentation
   - [QUICKSTART.md](QUICKSTART.md) - Getting started
   - [CORS_SETUP.md](CORS_SETUP.md) - CORS configuration

2. **Check the code**
   - Review error messages carefully
   - Check browser console (F12)
   - Check Next.js terminal output

3. **Verify setup**
   ```bash
   node verify-setup.js
   ```

4. **Ask for help**
   - Open an issue on GitHub
   - Include error messages
   - Include steps to reproduce
   - Include environment details (OS, Node version, etc.)
