package com.example.student_management_system.controller;

import com.example.student_management_system.model.Student;
import com.example.student_management_system.model.Teacher;
import com.example.student_management_system.model.User;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController Unit Tests")
class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Model model;

    @InjectMocks
    private AuthController authController;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("encodedPassword");
        testUser.setRole("ROLE_STUDENT");
    }

    @Test
    @DisplayName("Should show signup form - GET /signup")
    void showSignupForm_Success() {
        String viewName = authController.showSignupForm(model);
        assertEquals("signup", viewName);
        verify(model, times(1)).addAttribute(eq("user"), any(User.class));
    }

    @Test
    @DisplayName("Should show login form - GET /login")
    void showLoginForm_Success() {
        String viewName = authController.showLoginForm();
        assertEquals("login", viewName);
    }

    @Test
    @DisplayName("Should register new student - POST /register")
    void registerUser_Student_Success() {
        User newUser = new User();
        newUser.setUsername("newstudent");
        newUser.setPassword("password123");
        newUser.setRole("STUDENT");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(studentRepository.save(any(Student.class))).thenReturn(new Student());

        String result = authController.registerUser(newUser);

        assertEquals("redirect:/login", result);
        verify(userRepository, times(1)).save(any(User.class));
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    @DisplayName("Should register new teacher - POST /register")
    void registerUser_Teacher_Success() {
        User newUser = new User();
        newUser.setUsername("newteacher");
        newUser.setPassword("password123");
        newUser.setRole("TEACHER");

        User savedTeacherUser = new User();
        savedTeacherUser.setId(2L);
        savedTeacherUser.setUsername("newteacher");
        savedTeacherUser.setPassword("encodedPassword");
        savedTeacherUser.setRole("ROLE_TEACHER");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedTeacherUser);
        when(teacherRepository.save(any(Teacher.class))).thenReturn(new Teacher());

        String result = authController.registerUser(newUser);

        assertEquals("redirect:/login", result);
        verify(teacherRepository, times(1)).save(any(Teacher.class));
    }

    @Test
    @DisplayName("Should encode password during registration")
    void registerUser_PasswordEncoded() {
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setPassword("rawpassword");
        newUser.setRole("STUDENT");

        when(passwordEncoder.encode("rawpassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(studentRepository.save(any(Student.class))).thenReturn(new Student());

        authController.registerUser(newUser);

        verify(passwordEncoder, times(1)).encode("rawpassword");
    }
}
