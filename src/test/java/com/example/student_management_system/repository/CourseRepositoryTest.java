package com.example.student_management_system.repository;

import com.example.student_management_system.model.Course;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("CourseRepository Unit Tests")
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    private Course testCourse;
    private Teacher testTeacher;

    @BeforeEach
    void setUp() {
        courseRepository.deleteAll();
        teacherRepository.deleteAll();
        userRepository.deleteAll();

        User teacherUser = new User();
        teacherUser.setUsername("teacheruser");
        teacherUser.setPassword("password123");
        teacherUser.setRole("ROLE_TEACHER");
        teacherUser = userRepository.save(teacherUser);

        testTeacher = new Teacher();
        testTeacher.setName("Prof. Smith");
        testTeacher.setEmail("smith@example.com");
        testTeacher.setUser(teacherUser);
        testTeacher = teacherRepository.save(testTeacher);

        testCourse = new Course();
        testCourse.setTitle("Introduction to Java");
        testCourse.setDescription("Learn Java programming from scratch");
        testCourse.setTeacher(testTeacher);
    }

    @Test
    @DisplayName("Should save course successfully")
    void saveCourse_Success() {
        // When
        Course savedCourse = courseRepository.save(testCourse);

        // Then
        assertNotNull(savedCourse.getId());
        assertEquals("Introduction to Java", savedCourse.getTitle());
        assertEquals("Learn Java programming from scratch", savedCourse.getDescription());
    }

    @Test
    @DisplayName("Should find all courses")
    void findAll_Success() {
        // Given
        courseRepository.save(testCourse);

        Course anotherCourse = new Course();
        anotherCourse.setTitle("Spring Boot Basics");
        anotherCourse.setDescription("Learn Spring Boot framework");
        anotherCourse.setTeacher(testTeacher);
        courseRepository.save(anotherCourse);

        // When
        List<Course> courses = courseRepository.findAll();

        // Then
        assertEquals(2, courses.size());
    }

    @Test
    @DisplayName("Should find course by id")
    void findById_Success() {
        // Given
        Course savedCourse = courseRepository.save(testCourse);

        // When
        Optional<Course> foundCourse = courseRepository.findById(savedCourse.getId());

        // Then
        assertTrue(foundCourse.isPresent());
        assertEquals("Introduction to Java", foundCourse.get().getTitle());
    }

    @Test
    @DisplayName("Should return empty when course not found")
    void findById_NotFound() {
        // When
        Optional<Course> foundCourse = courseRepository.findById(999L);

        // Then
        assertFalse(foundCourse.isPresent());
    }

    @Test
    @DisplayName("Should delete course by id")
    void deleteById_Success() {
        // Given
        Course savedCourse = courseRepository.save(testCourse);
        Long courseId = savedCourse.getId();

        // When
        courseRepository.deleteById(courseId);

        // Then
        assertFalse(courseRepository.findById(courseId).isPresent());
    }

    @Test
    @DisplayName("Should update course successfully")
    void updateCourse_Success() {
        // Given
        Course savedCourse = courseRepository.save(testCourse);

        // When
        savedCourse.setTitle("Advanced Java");
        savedCourse.setDescription("Advanced Java programming concepts");
        Course updatedCourse = courseRepository.save(savedCourse);

        // Then
        assertEquals("Advanced Java", updatedCourse.getTitle());
        assertEquals("Advanced Java programming concepts", updatedCourse.getDescription());
    }
}
