# Complete GitHub Workflow - Step by Step

## Initial Setup (One Time Only)

### 1. Create GitHub Repository

```bash
# On GitHub.com:
# 1. Click "+" → "New repository"
# 2. Name: student-management-system
# 3. Don't initialize with README
# 4. Click "Create repository"
```

### 2. Connect Local Project to GitHub

```bash
cd /Users/nahianzarif/Downloads/student-management-system

# Initialize git (if not done)
git init

# Add all files
git add .

# Make first commit
git commit -m "Initial commit: Student Management System with tests and CI/CD"

# Add GitHub remote (replace YOUR_USERNAME)
git remote add origin https://github.com/YOUR_USERNAME/student-management-system.git

# Push to main branch
git branch -M main
git push -u origin main

# Create develop branch
git checkout -b develop
git push -u origin develop
```

### 3. Enable GitHub Actions

GitHub Actions will automatically detect the `.github/workflows/ci.yml` file and start running on pushes and PRs.

## Daily Development Workflow

### Starting a New Feature

```bash
# 1. Switch to develop and update
git checkout develop
git pull origin develop

# 2. Create feature branch
git checkout -b feature/add-attendance-tracking

# 3. Make your changes
# Edit files, add new features...

# 4. Run tests locally
./mvnw test

# 5. If tests pass, commit
git add .
git commit -m "feat(attendance): add student attendance tracking"

# 6. Push to GitHub
git push origin feature/add-attendance-tracking
```

### Creating a Pull Request

```bash
# After pushing, go to GitHub:
# 1. Navigate to your repository
# 2. Click "Pull requests" tab
# 3. Click "New pull request"
# 4. Set: base: develop ← compare: feature/add-attendance-tracking
# 5. Fill in PR details:
```

**PR Template:**
```markdown
## Description
Added student attendance tracking feature

## Changes
- Added AttendanceController with CRUD operations
- Created Attendance entity and repository
- Added unit tests for attendance functionality

## Testing
- ✅ All unit tests passing locally
- ✅ Manual testing completed

## Checklist
- [x] Tests added/updated
- [x] Code follows project conventions
- [x] Documentation updated
```

### Watching CI/CD Run

After creating the PR:

1. **GitHub automatically runs tests**
   - You'll see: "Some checks haven't completed yet"
   - Wait 2-5 minutes

2. **Check Results**
   - ✅ Green: "All checks have passed" → Ready to merge
   - ❌ Red: "Some checks failed" → Fix and push again

3. **View Details**
   - Click "Details" next to each check
   - See test output and logs

### If Tests Fail

```bash
# 1. Check error in GitHub Actions
# 2. Fix locally
git add .
git commit -m "fix(test): resolve failing attendance tests"
git push

# 3. GitHub automatically re-runs tests
```

### Merging PR

```bash
# After tests pass and review approved:
# 1. Click "Merge pull request" on GitHub
# 2. Click "Confirm merge"
# 3. Delete feature branch: Click "Delete branch"

# 4. Locally, update develop
git checkout develop
git pull origin develop

# 5. Delete local feature branch
git branch -d feature/add-attendance-tracking
```

## Release Workflow

### Creating a Release

```bash
# 1. All features merged to develop and tested
git checkout develop
git pull origin develop

# 2. Create release PR from develop to main
# On GitHub:
# - Base: main ← Compare: develop
# - Title: "Release v1.0.0"

# 3. Wait for CI/CD to pass

# 4. Merge to main

# 5. Tag the release
git checkout main
git pull origin main
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```

### Creating GitHub Release

```bash
# On GitHub:
# 1. Go to "Releases" → "Draft a new release"
# 2. Choose tag: v1.0.0
# 3. Release title: "Version 1.0.0 - Initial Release"
# 4. Description:
```

```markdown
## Features
- Student management (CRUD)
- Course enrollment system
- Teacher management
- Authentication and authorization
- Role-based access control

## Tests
- 60+ unit and integration tests
- Full CI/CD pipeline

## Download
- student-management-system-0.0.1-SNAPSHOT.jar
```

## Hotfix Workflow (Production Bug)

```bash
# 1. Create hotfix from main
git checkout main
git pull origin main
git checkout -b hotfix/fix-login-redirect

# 2. Make the urgent fix
# Edit files...

# 3. Test locally
./mvnw test

# 4. Commit and push
git add .
git commit -m "hotfix(auth): fix login redirect loop"
git push origin hotfix/fix-login-redirect

# 5. Create PR to main
# GitHub → PR → base: main ← compare: hotfix/fix-login-redirect

# 6. After merge, backport to develop
git checkout develop
git pull origin develop
git merge hotfix/fix-login-redirect
git push origin develop

# 7. Delete hotfix branch
git branch -d hotfix/fix-login-redirect
git push origin --delete hotfix/fix-login-redirect
```

## Viewing CI/CD Results

### In Terminal (Local)

```bash
# Run same tests that CI/CD runs
./mvnw test -Dspring.profiles.active=test

# Expected output:
[INFO] Tests run: 60, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

### On GitHub

**Method 1: Actions Tab**
```
1. Click "Actions" tab
2. See list of all workflow runs
3. Click any run to see details
```

**Method 2: Pull Request**
```
1. Open your PR
2. Scroll to bottom
3. See "Checks" section with status
```

**Method 3: Commit Page**
```
1. Go to "Commits" tab
2. Each commit shows ✅ or ❌
3. Click icon for details
```

## Understanding Test Output

### Successful Test Run

```
[INFO] Tests run: 60, Failures: 0, Errors: 0, Skipped: 0

Results:
Tests run: 60, Failures: 0, Errors: 0, Skipped: 0

[INFO] BUILD SUCCESS
Total time: 3.456 s
```

### Failed Test Run

```
[ERROR] Tests run: 60, Failures: 1, Errors: 0, Skipped: 0

[ERROR] Failures: 
[ERROR]   AuthControllerTest.registerUser_Student_Success:85
  Expected: redirect:/login
  Actual: redirect:/error

[ERROR] BUILD FAILURE
```

## Branch Protection in Action

With protection enabled:

```
❌ Cannot push to main directly
✅ Must create PR
✅ Must pass CI/CD checks
✅ Must get approval
✅ Then can merge
```

## Common Commands Reference

### Git Commands

```bash
# Check current branch
git branch

# See what changed
git status
git diff

# Update from remote
git pull

# View commit history
git log --oneline

# Undo last commit (keep changes)
git reset --soft HEAD~1

# Undo all local changes
git checkout .

# Create and switch branch
git checkout -b feature/new-feature

# Delete local branch
git branch -d feature/old-feature

# Delete remote branch
git push origin --delete feature/old-feature
```

### Maven Commands

```bash
# Run all tests
./mvnw test

# Run specific test
./mvnw test -Dtest=AuthControllerTest

# Clean and rebuild
./mvnw clean install

# Skip tests
./mvnw clean install -DskipTests

# Run with specific profile
./mvnw test -Dspring.profiles.active=test

# Generate coverage report
./mvnw clean test jacoco:report
```

### Docker Commands

```bash
# Start database
docker compose up -d

# Stop database
docker compose down

# View logs
docker logs student_postgres

# Check running containers
docker ps
```

## Team Collaboration

### Code Review Checklist

When reviewing PRs:

- [ ] Tests added for new features
- [ ] All tests passing
- [ ] Code follows conventions
- [ ] No commented-out code
- [ ] Meaningful variable names
- [ ] Documentation updated
- [ ] No sensitive data (passwords, keys)

### PR Review Comments

```markdown
# Approve
Looks good! ✅

# Request changes
Could you add a test for the edge case when student is null?

# Nitpick (optional)
Minor: Consider renaming `temp` to `selectedStudent` for clarity
```

## Troubleshooting

### Problem: Tests pass locally but fail in CI/CD

**Solution:**
```bash
# Run tests exactly like CI/CD does
./mvnw clean test -Dspring.profiles.active=test

# Check Java version
java -version
```

### Problem: Cannot push to main

**Solution:**
```bash
# This is correct! Use PR instead
git checkout -b feature/my-change
git push origin feature/my-change
# Then create PR on GitHub
```

### Problem: Merge conflicts

**Solution:**
```bash
git checkout develop
git pull origin develop
git checkout your-branch
git merge develop
# Resolve conflicts in files
git add .
git commit -m "Merge develop and resolve conflicts"
git push
```

### Problem: Forgot to create branch

**Solution:**
```bash
# Move changes to new branch
git stash
git checkout -b feature/my-feature
git stash pop
git add .
git commit -m "Add my feature"
```

## Summary

**Development Flow:**
1. Create feature branch from develop
2. Make changes and test locally
3. Push to GitHub
4. Create PR
5. Wait for CI/CD ✅
6. Get review approval
7. Merge to develop
8. Delete feature branch

**Release Flow:**
1. Merge develop to main via PR
2. CI/CD runs all tests
3. Create GitHub release with tag
4. Download artifact from Actions

**Your tests run automatically:**
- ✅ On every push
- ✅ On every PR
- ✅ Before any merge to main/develop
- ✅ Visible to all team members
