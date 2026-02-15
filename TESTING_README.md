# Testing & CI/CD Quick Reference

## 📚 Documentation Files

| File | Description |
|------|-------------|
| [TESTING_GUIDE.md](TESTING_GUIDE.md) | How unit testing works with Mockito & JUnit |
| [BRANCHING_STRATEGY.md](BRANCHING_STRATEGY.md) | Git branching model and workflow |
| [GITHUB_ACTIONS_GUIDE.md](GITHUB_ACTIONS_GUIDE.md) | CI/CD pipeline setup and monitoring |
| [COMPLETE_WORKFLOW.md](COMPLETE_WORKFLOW.md) | Step-by-step development workflow |

## 🧪 Running Tests

```bash
# Run all tests (60+ test cases)
./mvnw test

# Run specific test class
./mvnw test -Dtest=AuthControllerTest

# Run with coverage report
./mvnw clean test jacoco:report
```

## 🌿 Git Branching

```bash
# Create feature
git checkout -b feature/my-feature

# Push and create PR
git push origin feature/my-feature
```

## 🚀 CI/CD Pipeline

**Automatically runs on:**
- Push to `main`, `master`, `develop`
- Pull Requests

**Pipeline stages:**
1. ✅ Run Tests (60+ tests)
2. ✅ Build JAR artifact

**View results:**
- GitHub → Actions tab
- Or on Pull Request page

## 📊 Test Coverage

```
Controller Tests:  27+ tests
Service Tests:      3+ tests
Repository Tests:  30+ tests
─────────────────────────────
Total:            60+ tests
```

## 🔗 Quick Links

- **Run App:** `./mvnw spring-boot:run`
- **UI:** http://localhost:8080
- **Tests:** `./mvnw test`
- **Build:** `./mvnw clean install`

## 💡 Key Points

1. **Always test locally before pushing**
2. **Use feature branches, never commit to main directly**
3. **Wait for CI/CD ✅ before merging**
4. **Write tests for new features**
5. **Follow naming conventions**

---

**Need help?** Check the detailed guides above!
