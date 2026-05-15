# Test Reports Guide

This document explains how test reports are generated, stored, and accessed.

## Report Types

### 1. Maven Surefire Reports
- **Location:** `target/surefire-reports/`
- **Format:** XML (JUnit) and TXT
- **Usage:** CI/CD integration, test result parsing

### 2. Allure Reports
- **Results:** `target/allure-results/`
- **Report:** `target/allure-report/`
- **Format:** JSON (results), HTML (report)
- **Usage:** Rich interactive test reports

### 3. Test Logs
- **Location:** `target/test-logs/test-execution.log`
- **Format:** Plain text
- **Usage:** Detailed test execution logs

## Important: Reports Are NOT in Repository

All test reports are **excluded from Git** via `.gitignore`:

```gitignore
target/                  # All Maven build artifacts
surefire-reports/        # Test reports
allure-results/          # Allure data
allure-report/           # Allure HTML
.allure/                 # Allure CLI (downloaded, not committed)
*.log                    # Log files
*.pid                    # Process IDs
```

### Why Reports Are Excluded:

1. **Size** - Reports can be large and grow over time
2. **Redundancy** - Reports regenerate on every test run
3. **CI/CD** - Reports stored as artifacts in CI/CD systems
4. **Best Practice** - Build artifacts never in version control

## Where Reports Exist

### Local Development

When you run tests locally:

```bash
mvn clean test
```

Reports are created in `target/` directory:
- ✅ Local to your machine only
- ✅ Excluded from Git
- ✅ Deleted by `mvn clean`
- ❌ Never committed

**View reports:**
```bash
# Allure (recommended)
mvn allure:serve

# Or static report
mvn allure:report
open target/allure-report/index.html

# Maven HTML report
mvn surefire-report:report
open target/site/surefire-report.html
```

### GitHub Actions

When tests run in GitHub Actions:

1. Reports generated in ephemeral runner
2. Uploaded as **GitHub Actions Artifacts**
3. Stored in GitHub Actions (NOT in repository)
4. Retained for 30 days
5. Runner destroyed after workflow

**Access reports:**
1. Go to Actions tab → workflow run
2. Scroll to "Artifacts" section
3. Download artifact ZIP
4. Extract and open `allure-report/index.html`

## Verification

### Verify Reports Are Excluded

```bash
# Run tests
mvn clean test

# Check git status (should show nothing)
git status

# Verify .gitignore is working
git check-ignore target/
git check-ignore target/surefire-reports/
# Should return the paths (meaning they're ignored)
```

### Check What's in Git

```bash
# List tracked files (should show NO reports)
git ls-files | grep -E "(target|surefire|allure|\.log)"
# Should return empty
```

## Summary

✅ **Reports generated locally in `target/` (excluded from Git)**  
✅ **Reports uploaded as GitHub Actions artifacts (not in repo)**  
✅ **`.gitignore` prevents accidental commits**  
✅ **`mvn clean` removes all local reports**  
✅ **Repository contains only source code**  

For more details, see [README.md](README.md) and [TESTING-GUIDE.md](TESTING-GUIDE.md).
