package com.example.student_management_system.controller;

import com.example.student_management_system.model.Course;
import com.example.student_management_system.model.Student;
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
import java.util.Optional;

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
    private Student testStudent;
    private User teacherUser;
    private User studentUser;

    @BeforeEach
    void setUp() {
        teacherUser = new User();
        teacherUser.setId(1L);
        teacherUser.setUsername("teacher");
        teacherUser.setPassword("password");
        teacherUser.setRole("ROLE_TEACHER");

        studentUser = new User();
        studentUser.setId(2L);
        studentUser.setUsername("student");
        studentUser.setPassword("password");
        studentUser.setRole("ROLE_STUDENT");

        testTeacher = new Teacher();
        testTeacher.setId(1L);
        testTeacher.setName("Prof. Smith");
        testTeacher.setEmail("smith@example.com");
        testTeacher.setUser(teacherUser);

        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setName("John Doe");
        testStudent.setEmail("john@example.com");
        testStudent.setUser(studentUser);
        testStudent.setCourses(new ArrayList<>());

        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setTitle("Introduction to Java");
        testCourse.setDescription("Learn Java programming");
        testCourse.setTeacher(testTeacher);
        testCourse.setStudents(new ArrayList<>());
    }

    @Test
    @DisplayName("Should list all courses - GET /courses")
    void listCourses_Success() {
        when(courseRepository.findAll()).thenReturn(Arrays.asList(testCourse));
        when(principal.getName()).thenReturn("student");
        when(userRepository.findByUsername("student")).thenReturn(studentUser);
        when(studentRepository.findByUser(studentUser)).thenReturn(testStudent);

        String viewName = courseController.listCourses(model, principal);

        assertEquals("courses", viewName);
        verify(model, times(1)).addAttribute(eq("courses"), any());
    }

    @Test
    @DisplayName("Should list courses without principal - GET /courses")
    void listCourses_NoPrincipal() {
        when(courseRepository.findAll()).thenReturn(Arrays.asList(testCourse));

        String viewName = courseController.listCourses(model, null);

        assertEquals("courses", viewName);
        verify(model, times(1)).addAttribute(eq("courses"), any());
    }

    @Test
    @DisplayName("Should show create course form - GET /courses/new")
    void showCreateForm_Success() {
        String viewName = courseController.showCreateForm(model);

        assertEquals("create_course", viewName);
        verify(model, times(1)).addAttribute(eq("course"), any(Course.class));
    }

    @Test
    @DisplayName("Should save new course - POST /courses/save")
    void saveCourse_Success() {
        Course newCourse = new Course();
        newCourse.setTitle("New Course");
        newCourse.setDescription("Description");

        when(principal.getName()).thenReturn("teacher");
        when(userRepository.findByUsername("teacher")).thenReturn(teacherUser);
        when(teacherRepository.findByUser(teacherUser)).thenReturn(testTeacher);
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);

        String result = courseController.saveCourse(newCourse, principal);

        assertEquals("redirect:/courses", result);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    @DisplayName("Should toggle enrollment - enroll student - POST /courses/enroll/{id}")
    void toggleEnrollment_Enroll() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(principal.getName()).thenReturn("student");
        when(userRepository.findByUsername("student")).thenReturn(studentUser);
        when(studentRepository.findByUser(studentUser)).thenReturn(testStudent);
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        String result = courseController.toggleEnrollment(1L, principal);

        assertEquals("redirect:/courses", result);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    @DisplayName("Should toggle enrollment - unenroll student - POST /courses/enroll/{id}")
    void toggleEnrollment_Unenroll() {
        // Student already enrolled
        testStudent.getCourses().add(testCourse);
        testCourse.getStudents().add(testStudent);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(principal.getName()).thenReturn("student");
        when(userRepository.findByUsername("student")).thenReturn(studentUser);
        when(studentRepository.findByUser(studentUser)).thenReturn(testStudent);
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        String result = courseController.toggleEnrollment(1L, principal);

        assertEquals("redirect:/courses", result);
        verify(studentRepository, times(1)).save(any(Student.class));
    }
}
