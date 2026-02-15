# Unit Testing Reference Guide

## Testing Architecture

```
src/test/java/
├── controller/          ← Controller Layer Tests (Unit)
│   ├── AuthControllerTest.java        (8 tests)
│   ├── StudentControllerTest.java     (10 tests)
│   ├── CourseControllerTest.java      (7 tests)
│   └── HomeControllerTest.java        (3 tests)
├── service/             ← Service Layer Tests (Unit)
│   └── CustomUserDetailsServiceTest.java (3 tests)
└── repository/          ← Repository Layer Tests (Integration)
    ├── UserRepositoryTest.java        (5 tests)
    ├── StudentRepositoryTest.java     (7 tests)
    ├── CourseRepositoryTest.java      (6 tests)
    ├── TeacherRepositoryTest.java     (4 tests)
    └── DepartmentRepositoryTest.java  (4 tests)
```

## Test Annotations Explained

### @ExtendWith(MockitoExtension.class)
Enables Mockito framework for creating mock objects

### @Mock
Creates a mock (fake) object that you can control in tests

### @InjectMocks
Creates the actual object being tested and injects all @Mock objects into it

### @BeforeEach
Runs before each test method to set up test data

### @Test
Marks a method as a test case

### @DisplayName
Adds a readable description to the test

## Example Test Breakdown

```java
@ExtendWith(MockitoExtension.class)  // 1. Enable Mockito
class AuthControllerTest {

    @Mock  // 2. Create fake dependency
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks  // 3. Create real controller with fake dependencies
    private AuthController authController;
    
    @BeforeEach  // 4. Setup runs before each test
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
    }
    
    @Test  // 5. Actual test method
    @DisplayName("Should register new student")
    void registerUser_Student_Success() {
        // ARRANGE - Setup
        User newUser = new User();
        newUser.setUsername("newstudent");
        
        // Mock behavior: when method is called, return this
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        // ACT - Execute
        String result = authController.registerUser(newUser);
        
        // ASSERT - Verify
        assertEquals("redirect:/login", result);
        verify(userRepository, times(1)).save(any(User.class));
    }
}
```

## Mockito Methods

### when().thenReturn()
Define what a mock should return when called
```java
when(userRepository.findById(1L)).thenReturn(Optional.of(user));
```

### verify()
Check if a method was called
```java
verify(userRepository, times(1)).save(any(User.class));
verify(userRepository, never()).delete(any());
```

### any(), anyString(), eq()
Argument matchers for flexible matching
```java
when(encoder.encode(anyString())).thenReturn("encoded");
verify(model).addAttribute(eq("user"), any(User.class));
```

## Running Tests

### Run all tests
```bash
./mvnw test
```

### Run specific test class
```bash
./mvnw test -Dtest=AuthControllerTest
```

### Run specific test method
```bash
./mvnw test -Dtest=AuthControllerTest#registerUser_Student_Success
```

### Run with detailed output
```bash
./mvnw test -X
```

### Run tests and generate coverage report
```bash
./mvnw clean test jacoco:report
# View report at: target/site/jacoco/index.html
```

## Test Coverage Goals

- **Line Coverage:** 80%+
- **Branch Coverage:** 70%+
- **Method Coverage:** 90%+

## Writing New Tests

### For a New Controller Method

```java
@Test
@DisplayName("Should update student profile")
void updateStudentProfile_Success() {
    // Arrange
    Student student = new Student();
    student.setId(1L);
    student.setName("John");
    
    when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
    when(studentRepository.save(any())).thenReturn(student);
    
    // Act
    String result = studentController.updateStudent(1L, student, "CS");
    
    // Assert
    assertEquals("redirect:/students", result);
    verify(studentRepository).save(any(Student.class));
}
```

### For a New Service Method

```java
@Test
@DisplayName("Should find all active students")
void findActiveStudents_Success() {
    // Arrange
    List<Student> students = Arrays.asList(student1, student2);
    when(studentRepository.findByStatus("ACTIVE")).thenReturn(students);
    
    // Act
    List<Student> result = studentService.findActiveStudents();
    
    // Assert
    assertEquals(2, result.size());
    verify(studentRepository).findByStatus("ACTIVE");
}
```

### For a New Repository Method

```java
@Test
@DisplayName("Should find students by department")
void findByDepartment_Success() {
    // Arrange
    Department dept = departmentRepository.save(testDepartment);
    studentRepository.save(testStudent);
    
    // Act
    List<Student> result = studentRepository.findByDepartment(dept);
    
    // Assert
    assertEquals(1, result.size());
    assertEquals("John Doe", result.get(0).getName());
}
```

## Test Data Builders

Create helper methods for test data:

```java
private User createTestUser(String username, String role) {
    User user = new User();
    user.setUsername(username);
    user.setPassword("password");
    user.setRole(role);
    return user;
}

private Student createTestStudent(String name, String email) {
    Student student = new Student();
    student.setName(name);
    student.setEmail(email);
    return student;
}
```

## Common Testing Patterns

### Testing Exceptions

```java
@Test
@DisplayName("Should throw exception when user not found")
void findUser_NotFound_ThrowsException() {
    when(userRepository.findById(999L)).thenReturn(Optional.empty());
    
    assertThrows(UserNotFoundException.class, () -> {
        userService.findById(999L);
    });
}
```

### Testing with Multiple Mocks

```java
@Test
@DisplayName("Should enroll student in course")
void enrollStudent_Success() {
    when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
    when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
    when(studentRepository.save(any())).thenReturn(student);
    
    courseService.enrollStudent(1L, 1L);
    
    verify(studentRepository).save(any());
}
```

### Testing Collections

```java
@Test
@DisplayName("Should return all students")
void findAll_ReturnsMultipleStudents() {
    List<Student> students = Arrays.asList(student1, student2, student3);
    when(studentRepository.findAll()).thenReturn(students);
    
    List<Student> result = studentService.findAll();
    
    assertEquals(3, result.size());
}
```

## Best Practices

1. **One assertion per test** (when possible)
2. **Use descriptive test names** that explain what's being tested
3. **Follow AAA pattern:** Arrange, Act, Assert
4. **Test both success and failure scenarios**
5. **Keep tests independent** - don't rely on other tests
6. **Use @BeforeEach** for common setup
7. **Don't test framework code** - test your logic
8. **Mock external dependencies** - database, APIs, etc.

## Test Naming Convention

```
methodName_StateUnderTest_ExpectedBehavior
```

Examples:
- `registerUser_ValidStudent_ReturnsRedirect`
- `findById_UserNotFound_ThrowsException`
- `saveStudent_DuplicateEmail_ReturnsFalse`

## Debugging Failed Tests

1. **Read the error message carefully**
2. **Check which assertion failed**
3. **Verify mock setup** - are you mocking the right methods?
4. **Add debug prints** temporarily
5. **Run test in debug mode** in your IDE
6. **Check test data** - is it what you expect?
