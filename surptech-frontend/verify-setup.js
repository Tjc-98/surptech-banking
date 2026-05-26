#!/usr/bin/env node

/**
 * Setup Verification Script for SurpTech Banking Frontend
 * 
 * This script verifies that the development environment is properly configured
 * and all required files are in place.
 */

const fs = require('fs');
const path = require('path');

// ANSI color codes for terminal output
const colors = {
  reset: '\x1b[0m',
  green: '\x1b[32m',
  red: '\x1b[31m',
  yellow: '\x1b[33m',
  blue: '\x1b[34m',
  bold: '\x1b[1m',
};

// Helper functions
const log = (message, color = colors.reset) => {
  console.log(`${color}${message}${colors.reset}`);
};

const success = (message) => log(`✓ ${message}`, colors.green);
const error = (message) => log(`✗ ${message}`, colors.red);
const warning = (message) => log(`⚠ ${message}`, colors.yellow);
const info = (message) => log(`ℹ ${message}`, colors.blue);

// Check if a file exists
const fileExists = (filePath) => {
  try {
    return fs.existsSync(filePath);
  } catch {
    return false;
  }
};

// Check if a directory exists
const dirExists = (dirPath) => {
  try {
    return fs.existsSync(dirPath) && fs.statSync(dirPath).isDirectory();
  } catch {
    return false;
  }
};

// Main verification function
const verifySetup = () => {
  log('\n' + colors.bold + '='.repeat(60) + colors.reset);
  log(colors.bold + '  SurpTech Banking Frontend - Setup Verification' + colors.reset);
  log(colors.bold + '='.repeat(60) + colors.reset + '\n');

  let allChecksPass = true;

  // 1. Check Node.js version
  info('Checking Node.js version...');
  const nodeVersion = process.version;
  const majorVersion = parseInt(nodeVersion.slice(1).split('.')[0]);
  
  if (majorVersion >= 18) {
    success(`Node.js version: ${nodeVersion} (>= 18.0.0 required)`);
  } else {
    error(`Node.js version: ${nodeVersion} (>= 18.0.0 required)`);
    allChecksPass = false;
  }

  // 2. Check required directories
  info('\nChecking directory structure...');
  const requiredDirs = [
    'app',
    'components',
    'domain',
    'domain/entity',
    'domain/request',
    'domain/response',
    'domain/error',
    'client',
    'services',
    'mapper',
    'hooks',
    'utils',
    'config',
    'public',
  ];

  requiredDirs.forEach((dir) => {
    if (dirExists(dir)) {
      success(`Directory exists: ${dir}/`);
    } else {
      error(`Directory missing: ${dir}/`);
      allChecksPass = false;
    }
  });

  // 3. Check required files
  info('\nChecking required files...');
  const requiredFiles = [
    'package.json',
    'tsconfig.json',
    'next.config.js',
    'tailwind.config.ts',
    '.eslintrc.json',
    'postcss.config.js',
    'app/layout.tsx',
    'app/page.tsx',
    'app/globals.css',
    'components/SearchForm.tsx',
    'components/CustomerInfoCard.tsx',
    'components/LoadingSpinner.tsx',
    'components/ErrorMessage.tsx',
    'domain/entity/CustomerCreditInfo.ts',
    'domain/response/CustomerCreditInfoResponse.ts',
    'domain/request/CustomerInfoRequest.ts',
    'domain/error/ApiError.ts',
    'client/DataAggregatorClient.ts',
    'services/CustomerService.ts',
    'mapper/CustomerCreditInfoMapper.ts',
    'hooks/useCustomerCreditInfo.ts',
    'utils/validation.utils.ts',
    'utils/format.utils.ts',
    'config/api.config.ts',
  ];

  requiredFiles.forEach((file) => {
    if (fileExists(file)) {
      success(`File exists: ${file}`);
    } else {
      error(`File missing: ${file}`);
      allChecksPass = false;
    }
  });

  // 4. Check documentation files
  info('\nChecking documentation...');
  const docFiles = [
    'README.md',
    'ARCHITECTURE.md',
    'QUICKSTART.md',
    'CONTRIBUTING.md',
    'PROJECT_STRUCTURE.md',
    'SYSTEM_ARCHITECTURE.md',
    'BUILD_SUMMARY.md',
  ];

  docFiles.forEach((file) => {
    if (fileExists(file)) {
      success(`Documentation: ${file}`);
    } else {
      warning(`Documentation missing: ${file}`);
    }
  });

  // 5. Check node_modules
  info('\nChecking dependencies...');
  if (dirExists('node_modules')) {
    success('Dependencies installed (node_modules/ exists)');
  } else {
    warning('Dependencies not installed. Run: npm install');
  }

  // 6. Check package.json scripts
  info('\nChecking npm scripts...');
  try {
    const packageJson = JSON.parse(fs.readFileSync('package.json', 'utf8'));
    const requiredScripts = ['dev', 'build', 'start', 'lint'];
    
    requiredScripts.forEach((script) => {
      if (packageJson.scripts && packageJson.scripts[script]) {
        success(`Script available: npm run ${script}`);
      } else {
        error(`Script missing: ${script}`);
        allChecksPass = false;
      }
    });
  } catch (err) {
    error('Could not read package.json');
    allChecksPass = false;
  }

  // 7. Check environment configuration
  info('\nChecking environment configuration...');
  if (fileExists('.env.example')) {
    success('Environment template exists: .env.example');
  } else {
    warning('Environment template missing: .env.example');
  }

  if (fileExists('.env.local')) {
    success('Local environment configured: .env.local');
  } else {
    info('Local environment not configured (optional)');
  }

  // Final summary
  log('\n' + colors.bold + '='.repeat(60) + colors.reset);
  if (allChecksPass) {
    success(colors.bold + '\n✓ All checks passed! Setup is complete.\n');
    info('Next steps:');
    info('  1. Install dependencies: npm install');
    info('  2. Start backend services (ports 5551, 5552, 5555)');
    info('  3. Start frontend: npm run dev');
    info('  4. Open browser: http://localhost:3000\n');
  } else {
    error(colors.bold + '\n✗ Some checks failed. Please review the errors above.\n');
  }
  log(colors.bold + '='.repeat(60) + colors.reset + '\n');

  return allChecksPass;
};

// Run verification
const success = verifySetup();
process.exit(success ? 0 : 1);
