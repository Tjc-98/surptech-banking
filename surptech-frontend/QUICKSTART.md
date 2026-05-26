# Quick Start Guide

Get the SurpTech Banking Frontend up and running in 5 minutes.

## Prerequisites Check

Before starting, ensure you have:

```bash
# Check Node.js version (should be >= 18.0.0)
node --version

# Check npm version (should be >= 9.0.0)
npm --version
```

If you don't have Node.js installed, download it from [nodejs.org](https://nodejs.org/).

## Step 1: Install Dependencies

```bash
cd surptech-frontend
npm install
```

This will install all required packages including React, Next.js, TypeScript, and Tailwind CSS.

## Step 2: Configure Environment (Optional)

The application works with default settings, but you can customize:

```bash
# Copy the example environment file
cp .env.example .env.local

# Edit .env.local if needed
# Default values:
# DATA_AGGREGATOR_HOST=localhost
# DATA_AGGREGATOR_PORT=5555
```

## Step 3: Start Backend Services

The frontend requires the data-aggregator service to be running.

**Terminal 1 - Customer Profile:**
```bash
cd ../customer-profile
mvn spring-boot:run
```

**Terminal 2 - Credit Profile:**
```bash
cd ../credit-profile
mvn spring-boot:run
```

**Terminal 3 - Data Aggregator:**
```bash
cd ../data-aggregator
mvn spring-boot:run
```

Wait for all services to start (look for "Started Application" messages).

## Step 4: Start Frontend

**Terminal 4 - Frontend:**
```bash
cd ../surptech-frontend
npm run dev
```

You should see:

```
  ▲ Next.js 14.x.x
  - Local:        http://localhost:3000
  - Ready in X.Xs
```

## Step 5: Open in Browser

Open your browser and navigate to:

```
http://localhost:3000
```

## Step 6: Test the Application

1. **Enter a Social Security Number** in the search form
   - Format: `XXX-XX-XXXX` (e.g., `123-45-6789`)
   
2. **Click "Search Customer"**

3. **View the Results**
   - Personal information (name, address)
   - Credit information (balance, interest rate)

## Sample Test Data

If you have sample data in your backend, try these SSNs:
- `123-45-6789`
- `987-65-4321`

## Troubleshooting

### "Unable to connect to the server"

**Problem**: Frontend can't reach the backend.

**Solution**:
1. Verify data-aggregator is running on port 5555
2. Check console for errors: `F12` → Console tab
3. Verify the URL in `config/api.config.ts`

```bash
# Test backend manually
curl http://localhost:5555/data-aggregator/management/health
```

### "Customer not found"

**Problem**: SSN doesn't exist in the database.

**Solution**:
1. Verify the SSN format is correct (XXX-XX-XXXX)
2. Check if customer exists in the backend database
3. Create a customer using the backend API:

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

### Port 3000 already in use

**Problem**: Another application is using port 3000.

**Solution**:
```bash
# Use a different port
npm run dev -- -p 3001

# Or kill the process using port 3000 (Windows)
netstat -ano | findstr :3000
taskkill /PID <PID> /F
```

### TypeScript errors

**Problem**: Type checking errors during development.

**Solution**:
```bash
# Run type check
npm run type-check

# If errors persist, delete node_modules and reinstall
rm -rf node_modules package-lock.json
npm install
```

### Styling not working

**Problem**: Tailwind CSS styles not applied.

**Solution**:
1. Restart the dev server
2. Clear Next.js cache:
```bash
rm -rf .next
npm run dev
```

## Development Workflow

### Making Changes

1. **Edit files** in your code editor
2. **Save** - Next.js will auto-reload
3. **Check browser** - changes appear automatically

### Hot Reload

Next.js supports hot module replacement (HMR):
- Component changes reload instantly
- State is preserved when possible
- No manual refresh needed

### Viewing Logs

**Browser Console** (F12):
- Client-side errors
- Network requests
- Console.log output

**Terminal**:
- Server-side errors
- Build warnings
- Request logs

## Next Steps

Now that you're up and running:

1. **Read the [README.md](README.md)** for detailed documentation
2. **Explore [ARCHITECTURE.md](ARCHITECTURE.md)** to understand the design
3. **Check [CONTRIBUTING.md](CONTRIBUTING.md)** if you want to contribute
4. **Review the code** in the `components/` and `services/` directories

## Common Commands

```bash
# Development
npm run dev              # Start dev server
npm run build            # Build for production
npm start                # Start production server

# Code Quality
npm run lint             # Run ESLint
npm run type-check       # Run TypeScript compiler

# Maintenance
npm install              # Install dependencies
npm update               # Update dependencies
npm outdated             # Check for outdated packages
```

## Production Build

To create a production build:

```bash
# Build the application
npm run build

# Start production server
npm start
```

The production build is optimized for performance with:
- Minified JavaScript and CSS
- Optimized images
- Server-side rendering
- Static page generation

## Getting Help

If you're stuck:

1. **Check the documentation** in this repository
2. **Review error messages** carefully
3. **Search for similar issues** on GitHub
4. **Ask for help** by opening an issue

## Success Checklist

- [ ] Node.js and npm installed
- [ ] Dependencies installed (`npm install`)
- [ ] Backend services running (ports 5551, 5552, 5555)
- [ ] Frontend running (`npm run dev`)
- [ ] Browser open at `http://localhost:3000`
- [ ] Successfully searched for a customer
- [ ] Customer information displayed correctly

Congratulations! You're ready to develop with the SurpTech Banking Frontend! 🎉
