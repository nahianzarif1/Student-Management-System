package com.example.student_management_system.controller;

import com.example.student_management_system.model.Department;
import com.example.student_management_system.model.Student;
import com.example.student_management_system.repository.DepartmentRepository;
import com.example.student_management_system.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StudentController Unit Tests")
class StudentControllerTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private Model model;

    @InjectMocks
    private StudentController studentController;

    private Student testStudent;
    private Department testDepartment;

    @BeforeEach
    void setUp() {
        testDepartment = new Department();
        testDepartment.setId(1L);
        testDepartment.setName("Computer Science");

        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setName("John Doe");
        testStudent.setEmail("john@example.com");
        testStudent.setDepartment(testDepartment);
    }

    @Test
    @DisplayName("Should list all students")
    void listStudents_Success() {
        when(studentRepository.findAll()).thenReturn(Arrays.asList(testStudent));

        String viewName = studentController.listStudents(model);

        assertEquals("students", viewName);
        verify(model).addAttribute(eq("students"), any());
    }

    @Test
    @DisplayName("Should show create student form")
    void createStudentForm_Success() {
        String viewName = studentController.createStudentForm(model);

        assertEquals("create_student", viewName);
        verify(model).addAttribute(eq("student"), any(Student.class));
    }

    @Test
    @DisplayName("Should save new student")
    void saveStudent_Success() {
        when(departmentRepository.findByName("CS")).thenReturn(testDepartment);
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        String result = studentController.saveStudent(testStudent, "CS");

        assertEquals("redirect:/students", result);
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    @DisplayName("Should delete student")
    void deleteStudent_Success() {
        doNothing().when(studentRepository).deleteById(1L);

        String result = studentController.deleteStudent(1L);

        assertEquals("redirect:/students", result);
        verify(studentRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should show edit form")
    void showEditForm_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));

        String viewName = studentController.showEditForm(1L, model);

        assertEquals("edit_student", viewName);
        verify(model).addAttribute("student", testStudent);
    }

    @Test
    @DisplayName("Should update student")
    void updateStudent_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(departmentRepository.findByName("CS")).thenReturn(testDepartment);
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        String result = studentController.updateStudent(1L, testStudent, "CS");

        assertEquals("redirect:/students", result);
        verify(studentRepository).save(any(Student.class));
    }
}
