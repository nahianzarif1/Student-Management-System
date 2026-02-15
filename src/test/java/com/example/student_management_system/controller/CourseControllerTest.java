package com.example.student_management_system.controller;

import com.example.student_management_system.model.Course;
import com.example.student_management_system.model.Teacher;
import com.example.student_management_system.model.User;
import com.example.student_management_system.repository.CourseRepository;
import com.example.student_management_system.repository.StudentRepository;
import com.example.student_management_system.repository.TeacherRepository;
import com.example.student_management_system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CourseController Unit Tests")
class CourseControllerTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private Model model;

    @Mock
    private Principal principal;

    @InjectMocks
    private CourseController courseController;

    private Course testCourse;
    private Teacher testTeacher;
    private User teacherUser;

    @BeforeEach
    void setUp() {
        teacherUser = new User();
        teacherUser.setId(1L);
        teacherUser.setUsername("teacher");
        teacherUser.setRole("ROLE_TEACHER");

        testTeacher = new Teacher();
        testTeacher.setId(1L);
        testTeacher.setName("Prof. Smith");
        testTeacher.setUser(teacherUser);

        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setTitle("Java Programming");
        testCourse.setDescription("Learn Java");
        testCourse.setTeacher(testTeacher);
        testCourse.setStudents(new ArrayList<>());
    }

    @Test
    @DisplayName("Should list all courses")
    void listCourses_Success() {
        when(courseRepository.findAll()).thenReturn(Arrays.asList(testCourse));

        String viewName = courseController.listCourses(model, null);

        assertEquals("courses", viewName);
        verify(model).addAttribute(eq("courses"), any());
    }

    @Test
    @DisplayName("Should show create course form")
    void showCreateForm_Success() {
        String viewName = courseController.showCreateForm(model);

        assertEquals("create_course", viewName);
        verify(model).addAttribute(eq("course"), any(Course.class));
    }

    @Test
    @DisplayName("Should save new course")
    void saveCourse_Success() {
        when(principal.getName()).thenReturn("teacher");
        when(userRepository.findByUsername("teacher")).thenReturn(teacherUser);
        when(teacherRepository.findByUser(teacherUser)).thenReturn(testTeacher);
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);

        String result = courseController.saveCourse(testCourse, principal);

        assertEquals("redirect:/courses", result);
        verify(courseRepository).save(any(Course.class));
    }
}
