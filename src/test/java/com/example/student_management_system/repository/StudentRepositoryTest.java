package com.example.student_management_system.repository;

import com.example.student_management_system.model.Student;
import com.example.student_management_system.model.User;
import com.example.student_management_system.model.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("StudentRepository Unit Tests")
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Student testStudent;
    private User testUser;
    private Department testDepartment;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        departmentRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User();
        testUser.setUsername("studentuser");
        testUser.setPassword("password123");
        testUser.setRole("ROLE_STUDENT");
        testUser = userRepository.save(testUser);

        testDepartment = new Department();
        testDepartment.setName("Computer Science");
        testDepartment = departmentRepository.save(testDepartment);

        testStudent = new Student();
        testStudent.setName("John Doe");
        testStudent.setEmail("john@example.com");
        testStudent.setUser(testUser);
        testStudent.setDepartment(testDepartment);
    }

    @Test
    @DisplayName("Should save student successfully")
    void saveStudent_Success() {
        // When
        Student savedStudent = studentRepository.save(testStudent);

        // Then
        assertNotNull(savedStudent.getId());
        assertEquals("John Doe", savedStudent.getName());
        assertEquals("john@example.com", savedStudent.getEmail());
    }

    @Test
    @DisplayName("Should find student by user")
    void findByUser_Success() {
        // Given
        studentRepository.save(testStudent);

        // When
        Student foundStudent = studentRepository.findByUser(testUser);

        // Then
        assertNotNull(foundStudent);
        assertEquals("John Doe", foundStudent.getName());
        assertEquals(testUser.getUsername(), foundStudent.getUser().getUsername());
    }

    @Test
    @DisplayName("Should return null when student not found by user")
    void findByUser_NotFound() {
        // Given
        User anotherUser = new User();
        anotherUser.setUsername("anotheruser");
        anotherUser.setPassword("pass");
        anotherUser.setRole("ROLE_STUDENT");
        anotherUser = userRepository.save(anotherUser);

        // When
        Student foundStudent = studentRepository.findByUser(anotherUser);

        // Then
        assertNull(foundStudent);
    }

    @Test
    @DisplayName("Should find all students")
    void findAll_Success() {
        // Given
        studentRepository.save(testStudent);

        User anotherUser = new User();
        anotherUser.setUsername("student2");
        anotherUser.setPassword("pass");
        anotherUser.setRole("ROLE_STUDENT");
        anotherUser = userRepository.save(anotherUser);

        Student anotherStudent = new Student();
        anotherStudent.setName("Jane Smith");
        anotherStudent.setEmail("jane@example.com");
        anotherStudent.setUser(anotherUser);
        studentRepository.save(anotherStudent);

        // When
        List<Student> students = studentRepository.findAll();

        // Then
        assertEquals(2, students.size());
    }

    @Test
    @DisplayName("Should find student by id")
    void findById_Success() {
        // Given
        Student savedStudent = studentRepository.save(testStudent);

        // When
        Optional<Student> foundStudent = studentRepository.findById(savedStudent.getId());

        // Then
        assertTrue(foundStudent.isPresent());
        assertEquals("John Doe", foundStudent.get().getName());
    }

    @Test
    @DisplayName("Should delete student by id")
    void deleteById_Success() {
        // Given
        Student savedStudent = studentRepository.save(testStudent);
        Long studentId = savedStudent.getId();

        // When
        studentRepository.deleteById(studentId);

        // Then
        assertFalse(studentRepository.findById(studentId).isPresent());
    }

    @Test
    @DisplayName("Should update student successfully")
    void updateStudent_Success() {
        // Given
        Student savedStudent = studentRepository.save(testStudent);

        // When
        savedStudent.setName("John Updated");
        savedStudent.setEmail("john.updated@example.com");
        Student updatedStudent = studentRepository.save(savedStudent);

        // Then
        assertEquals("John Updated", updatedStudent.getName());
        assertEquals("john.updated@example.com", updatedStudent.getEmail());
    }
}
