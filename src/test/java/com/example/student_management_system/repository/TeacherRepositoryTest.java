package com.example.student_management_system.repository;

import com.example.student_management_system.model.Teacher;
import com.example.student_management_system.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("TeacherRepository Unit Tests")
class TeacherRepositoryTest {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    private Teacher testTeacher;
    private User testUser;

    @BeforeEach
    void setUp() {
        teacherRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User();
        testUser.setUsername("teacheruser");
        testUser.setPassword("password123");
        testUser.setRole("ROLE_TEACHER");
        testUser = userRepository.save(testUser);

        testTeacher = new Teacher();
        testTeacher.setName("Prof. Smith");
        testTeacher.setEmail("smith@example.com");
        testTeacher.setUser(testUser);
    }

    @Test
    @DisplayName("Should save teacher successfully")
    void saveTeacher_Success() {
        // When
        Teacher savedTeacher = teacherRepository.save(testTeacher);

        // Then
        assertNotNull(savedTeacher.getId());
        assertEquals("Prof. Smith", savedTeacher.getName());
        assertEquals("smith@example.com", savedTeacher.getEmail());
    }

    @Test
    @DisplayName("Should find teacher by user")
    void findByUser_Success() {
        // Given
        teacherRepository.save(testTeacher);

        // When
        Teacher foundTeacher = teacherRepository.findByUser(testUser);

        // Then
        assertNotNull(foundTeacher);
        assertEquals("Prof. Smith", foundTeacher.getName());
    }

    @Test
    @DisplayName("Should return null when teacher not found by user")
    void findByUser_NotFound() {
        // Given
        User anotherUser = new User();
        anotherUser.setUsername("anotheruser");
        anotherUser.setPassword("pass");
        anotherUser.setRole("ROLE_TEACHER");
        anotherUser = userRepository.save(anotherUser);

        // When
        Teacher foundTeacher = teacherRepository.findByUser(anotherUser);

        // Then
        assertNull(foundTeacher);
    }

    @Test
    @DisplayName("Should find all teachers")
    void findAll_Success() {
        // Given
        teacherRepository.save(testTeacher);

        User anotherUser = new User();
        anotherUser.setUsername("teacher2");
        anotherUser.setPassword("pass");
        anotherUser.setRole("ROLE_TEACHER");
        anotherUser = userRepository.save(anotherUser);

        Teacher anotherTeacher = new Teacher();
        anotherTeacher.setName("Prof. Johnson");
        anotherTeacher.setEmail("johnson@example.com");
        anotherTeacher.setUser(anotherUser);
        teacherRepository.save(anotherTeacher);

        // When
        List<Teacher> teachers = teacherRepository.findAll();

        // Then
        assertEquals(2, teachers.size());
    }
}
