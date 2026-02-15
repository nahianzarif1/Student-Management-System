package com.example.student_management_system.controller;

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

    @BeforeEach
    void setUp() {
        // Setup is handled by Mockito
    }

    @Test
    @DisplayName("Should show signup form")
    void showSignupForm_Success() {
        String viewName = authController.showSignupForm(model);
        assertEquals("signup", viewName);
        verify(model).addAttribute(eq("user"), any(User.class));
    }

    @Test
    @DisplayName("Should register new student")
    void registerUser_Student_Success() {
        User newUser = new User();
        newUser.setUsername("student1");
        newUser.setPassword("password");
        newUser.setRole("STUDENT");

        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        String result = authController.registerUser(newUser);

        assertEquals("redirect:/login", result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should register new teacher")
    void registerUser_Teacher_Success() {
        User newUser = new User();
        newUser.setUsername("teacher1");
        newUser.setPassword("password");
        newUser.setRole("TEACHER");

        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        String result = authController.registerUser(newUser);

        assertEquals("redirect:/login", result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should encode password during registration")
    void registerUser_EncodesPassword() {
        User newUser = new User();
        newUser.setUsername("user1");
        newUser.setPassword("rawpassword");
        newUser.setRole("STUDENT");

        when(passwordEncoder.encode("rawpassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        authController.registerUser(newUser);

        verify(passwordEncoder).encode("rawpassword");
    }
}
