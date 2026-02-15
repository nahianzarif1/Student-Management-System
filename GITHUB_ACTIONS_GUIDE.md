# GitHub Actions CI/CD Pipeline Guide

## What Happens Automatically

The CI/CD pipeline runs automatically when:
- ✅ You push code to `main`, `master`, or `develop` branches
- ✅ You create a Pull Request targeting these branches

## Pipeline Stages

### Stage 1: Test (Runs First)
```yaml
- Run Unit Tests (all 50+ test cases)
- Generate Test Report
- Fail if any test fails ❌
```

### Stage 2: Build (Only if tests pass)
```yaml
- Build the application JAR
- Upload artifact for deployment
```

## How to Check CI/CD Pipeline Status

### Method 1: On GitHub Repository

1. **Go to your repository on GitHub**
   ```
   https://github.com/YOUR_USERNAME/student-management-system
   ```

2. **Click "Actions" tab** (top navigation)

3. **You'll see all workflow runs:**
   - ✅ Green checkmark = All tests passed
   - ❌ Red X = Tests failed
   - 🟡 Yellow dot = Currently running

4. **Click on any workflow run to see details:**
   - Test results
   - Build logs
   - Which tests failed (if any)

### Method 2: On Pull Requests

When you create a PR, you'll see:

```
✅ All checks have passed
   ✓ Run Tests — completed in 2m 15s
   ✓ Build Application — completed in 1m 30s
```

Or if tests fail:

```
❌ Some checks were not successful
   ✗ Run Tests — failed
   ✓ Build Application — skipped
```

## Local Testing Before Push

**Always run tests locally before pushing:**

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=AuthControllerTest

# Run specific test method
./mvnw test -Dtest=AuthControllerTest#registerUser_Student_Success

# Run with coverage
./mvnw clean test jacoco:report
```

## Setting Up GitHub Repository

### Step 1: Initialize Git (if not done)

```bash
cd /Users/nahianzarif/Downloads/student-management-system
git init
git add .
git commit -m "Initial commit: Student Management System"
```

### Step 2: Create GitHub Repository

1. Go to https://github.com/new
2. Repository name: `student-management-system`
3. Description: "Spring Boot Student Management System with CI/CD"
4. Visibility: Public or Private
5. **DO NOT** initialize with README (you already have one)
6. Click "Create repository"

### Step 3: Connect Local to GitHub

```bash
# Add GitHub as remote
git remote add origin https://github.com/YOUR_USERNAME/student-management-system.git

# Create and push main branch
git branch -M main
git push -u origin main

# Create develop branch
git checkout -b develop
git push -u origin develop
```

### Step 4: Set Up Branch Protection

1. Go to repository **Settings** → **Branches**
2. Click **Add branch protection rule**

**For `main` branch:**
```
Branch name pattern: main

☑ Require a pull request before merging
  ☑ Require approvals (1)
  ☑ Dismiss stale pull request approvals when new commits are pushed

☑ Require status checks to pass before merging
  ☑ Require branches to be up to date before merging
  Status checks that are required:
    - Run Tests
    - Build Application

☑ Require conversation resolution before merging

☑ Do not allow bypassing the above settings
```

**For `develop` branch:**
```
Branch name pattern: develop

☑ Require a pull request before merging
☑ Require status checks to pass before merging
  - Run Tests
```

## Viewing Test Results

### In GitHub Actions

1. Click **Actions** tab
2. Click on a workflow run
3. Click **Run Tests** job
4. Expand **Run Unit Tests** step
5. See test output:

```
[INFO] Tests run: 50, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 50, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

### Test Reports

The pipeline generates HTML test reports. To view them:

1. Go to workflow run → **Summary** tab
2. Scroll down to **Artifacts**
3. Download **test-reports** (if configured)

## Common Workflow Scenarios

### Scenario 1: Feature Development

```bash
# 1. Create feature branch
git checkout develop
git pull origin develop
git checkout -b feature/add-grade-system

# 2. Make changes and run tests locally
./mvnw test

# 3. Commit and push
git add .
git commit -m "feat(grade): add grading system for courses"
git push origin feature/add-grade-system

# 4. Create PR on GitHub
# → CI/CD runs automatically
# → Wait for ✅ green checkmark
# → Request review
# → Merge to develop
```

### Scenario 2: Failed Tests

If tests fail in CI/CD:

1. **Check the error in GitHub Actions:**
   - Click the ❌ failed workflow
   - Read the error message

2. **Fix locally:**
   ```bash
   # Run the failing test
   ./mvnw test -Dtest=AuthControllerTest
   
   # Fix the code
   # Run tests again
   ./mvnw test
   ```

3. **Push the fix:**
   ```bash
   git add .
   git commit -m "fix(test): resolve failing AuthController test"
   git push
   ```

4. **CI/CD runs again automatically**

### Scenario 3: Release to Production

```bash
# 1. Create release PR from develop to main
git checkout main
git pull origin main

# 2. Merge develop into main via PR
# → This triggers CI/CD
# → All tests run
# → Build artifact is created

# 3. Deploy the artifact from GitHub Actions
```

## Monitoring Test Coverage

Add this to your workflow to track coverage:

```yaml
- name: Generate Coverage Report
  run: mvn clean test jacoco:report

- name: Upload Coverage to Codecov
  uses: codecov/codecov-action@v3
  with:
    files: ./target/site/jacoco/jacoco.xml
```

## Troubleshooting CI/CD

### Tests pass locally but fail in CI/CD

**Common causes:**
1. Different Java version
2. Missing test resources
3. Timezone differences
4. Database configuration

**Solution:** Check the exact error in GitHub Actions logs

### Tests are slow

**Optimize:**
```yaml
- name: Run Tests with parallel execution
  run: mvn test -T 4 -Dspring.profiles.active=test
```

### Build fails due to memory

**Increase memory:**
```yaml
- name: Run Tests
  run: mvn test -Dspring.profiles.active=test
  env:
    MAVEN_OPTS: "-Xmx1024m"
```

## Summary of Test Statistics

Current test coverage:
- **Controller Tests:** 4 classes, 27+ test methods
- **Service Tests:** 1 class, 3+ test methods  
- **Repository Tests:** 5 classes, 30+ test methods
- **Total:** ~60 test cases covering all major functionality

All tests use:
- ✅ JUnit 5
- ✅ Mockito for mocking
- ✅ H2 in-memory database for integration tests
- ✅ Spring Boot Test framework
