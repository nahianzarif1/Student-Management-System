# Git Branching Strategy

## Branch Structure

```
main (production-ready code)
  └── develop (integration branch)
       ├── feature/add-enrollment-system
       ├── feature/update-student-profile
       ├── bugfix/fix-login-error
       └── hotfix/urgent-security-patch
```

## Branch Types

### 1. **main** (Protected)
- Production-ready code only
- All code must pass CI/CD tests
- Direct commits are forbidden
- Only accepts merges from `develop` or `hotfix/*`

### 2. **develop** (Protected)
- Integration branch for features
- All new features merge here first
- Must pass all tests before merging to `main`

### 3. **feature/** (Short-lived)
- Format: `feature/description-of-feature`
- Examples:
  - `feature/add-course-enrollment`
  - `feature/student-dashboard`
  - `feature/teacher-grading-system`
- Created from: `develop`
- Merges into: `develop`

### 4. **bugfix/** (Short-lived)
- Format: `bugfix/description-of-bug`
- Examples:
  - `bugfix/fix-login-validation`
  - `bugfix/correct-enrollment-count`
- Created from: `develop`
- Merges into: `develop`

### 5. **hotfix/** (Short-lived)
- Format: `hotfix/critical-fix-description`
- For urgent production fixes
- Created from: `main`
- Merges into: both `main` AND `develop`

## Workflow Example

### Creating a New Feature

```bash
# 1. Start from develop branch
git checkout develop
git pull origin develop

# 2. Create feature branch
git checkout -b feature/add-student-search

# 3. Make your changes and commit
git add .
git commit -m "Add student search functionality"

# 4. Push to GitHub
git push origin feature/add-student-search

# 5. Create Pull Request on GitHub
# Target: feature/add-student-search → develop
```

### Creating a Pull Request

1. Go to GitHub repository
2. Click "Pull requests" → "New pull request"
3. Base: `develop` ← Compare: `feature/add-student-search`
4. Fill in PR description
5. Wait for CI/CD checks to pass ✅
6. Request code review
7. Merge after approval

### Hotfix Process

```bash
# 1. Create hotfix from main
git checkout main
git pull origin main
git checkout -b hotfix/fix-security-vulnerability

# 2. Make the fix
git add .
git commit -m "Fix critical security issue"

# 3. Push and create PR to main
git push origin hotfix/fix-security-vulnerability

# 4. After merging to main, also merge to develop
git checkout develop
git pull origin develop
git merge hotfix/fix-security-vulnerability
git push origin develop
```

## Branch Protection Rules (Set in GitHub)

### For `main` branch:
- ✅ Require pull request before merging
- ✅ Require status checks to pass (CI/CD tests)
- ✅ Require branches to be up to date
- ✅ Require conversation resolution
- ✅ Do not allow bypassing

### For `develop` branch:
- ✅ Require pull request before merging
- ✅ Require status checks to pass
- ✅ Require at least 1 approval

## Commit Message Convention

```
<type>(<scope>): <subject>

<body>

<footer>
```

**Types:**
- `feat`: New feature
- `fix`: Bug fix
- `test`: Adding tests
- `refactor`: Code refactoring
- `docs`: Documentation
- `style`: Formatting
- `chore`: Maintenance

**Examples:**
```bash
git commit -m "feat(student): add search by department functionality"
git commit -m "fix(auth): resolve login validation error"
git commit -m "test(course): add unit tests for course enrollment"
```
