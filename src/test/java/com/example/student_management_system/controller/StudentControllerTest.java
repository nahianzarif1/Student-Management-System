package com.example.student_management_system.controller;

import com.example.student_management_system.model.Department;
import com.example.student_management_system.model.Student;
import com.example.student_management_system.model.User;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    @DisplayName("Should list all students - GET /students")
    void listStudents_Success() {
        List<Student> students = Arrays.asList(testStudent);
        when(studentRepository.findAll()).thenReturn(students);

        String viewName = studentController.listStudents(model);

        assertEquals("students", viewName);
        verify(model, times(1)).addAttribute(eq("students"), eq(students));
    }

    @Test
    @DisplayName("Should show create student form - GET /students/new")
    void createStudentForm_Success() {
        String viewName = studentController.createStudentForm(model);
        assertEquals("create_student", viewName);
        verify(model, times(1)).addAttribute(eq("student"), any(Student.class));
    }

    @Test
    @DisplayName("Should save new student with new department")
    void saveStudent_NewDepartment() {
        Student newStudent = new Student();
        newStudent.setName("Jane Doe");
        newStudent.setEmail("jane@example.com");

        when(departmentRepository.findByName("New Department")).thenReturn(null);
        when(departmentRepository.save(any(Department.class))).thenReturn(testDepartment);
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        String result = studentController.saveStudent(newStudent, "New Department");

        assertEquals("redirect:/students", result);
        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    @DisplayName("Should save new student with existing department")
    void saveStudent_ExistingDepartment() {
        Student newStudent = new Student();
        newStudent.setName("Jane Doe");
        newStudent.setEmail("jane@example.com");

        when(departmentRepository.findByName("Computer Science")).thenReturn(testDepartment);
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        String result = studentController.saveStudent(newStudent, "Computer Science");

        assertEquals("redirect:/students", result);
        verify(departmentRepository, never()).save(any(Department.class));
    }

    @Test
    @DisplayName("Should delete student - GET /students/delete/{id}")
    void deleteStudent_Success() {
        doNothing().when(studentRepository).deleteById(1L);

        String result = studentController.deleteStudent(1L);

        assertEquals("redirect:/students", result);
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should show edit form when student exists")
    void showEditForm_StudentExists() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));

        String viewName = studentController.showEditForm(1L, model);

        assertEquals("edit_student", viewName);
        verify(model, times(1)).addAttribute(eq("student"), eq(testStudent));
    }

    @Test
    @DisplayName("Should redirect when student not found for edit")
    void showEditForm_StudentNotFound() {
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        String viewName = studentController.showEditForm(999L, model);

        assertEquals("redirect:/students", viewName);
    }

    @Test
    @DisplayName("Should update student")
    void updateStudent_Success() {
        Student updatedStudent = new Student();
        updatedStudent.setName("John Updated");
        updatedStudent.setEmail("john.updated@example.com");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(departmentRepository.findByName("Computer Science")).thenReturn(testDepartment);
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        String result = studentController.updateStudent(1L, updatedStudent, "Computer Science");

        assertEquals("redirect:/students", result);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    @DisplayName("Should redirect when student not found for update")
    void updateStudent_StudentNotFound() {
        Student updatedStudent = new Student();
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        String result = studentController.updateStudent(999L, updatedStudent, "Computer Science");

        assertEquals("redirect:/students", result);
        verify(studentRepository, never()).save(any(Student.class));
    }
}
