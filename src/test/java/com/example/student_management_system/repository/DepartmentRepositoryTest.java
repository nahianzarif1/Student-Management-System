package com.example.student_management_system.repository;

import com.example.student_management_system.model.Department;
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
@DisplayName("DepartmentRepository Unit Tests")
class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department testDepartment;

    @BeforeEach
    void setUp() {
        departmentRepository.deleteAll();

        testDepartment = new Department();
        testDepartment.setName("Computer Science");
    }

    @Test
    @DisplayName("Should save department successfully")
    void saveDepartment_Success() {
        // When
        Department savedDepartment = departmentRepository.save(testDepartment);

        // Then
        assertNotNull(savedDepartment.getId());
        assertEquals("Computer Science", savedDepartment.getName());
    }

    @Test
    @DisplayName("Should find department by name")
    void findByName_Success() {
        // Given
        departmentRepository.save(testDepartment);

        // When
        Department foundDepartment = departmentRepository.findByName("Computer Science");

        // Then
        assertNotNull(foundDepartment);
        assertEquals("Computer Science", foundDepartment.getName());
    }

    @Test
    @DisplayName("Should return null when department not found")
    void findByName_NotFound() {
        // When
        Department foundDepartment = departmentRepository.findByName("NonExistent");

        // Then
        assertNull(foundDepartment);
    }

    @Test
    @DisplayName("Should find all departments")
    void findAll_Success() {
        // Given
        departmentRepository.save(testDepartment);

        Department anotherDepartment = new Department();
        anotherDepartment.setName("Mathematics");
        departmentRepository.save(anotherDepartment);

        // When
        List<Department> departments = departmentRepository.findAll();

        // Then
        assertEquals(2, departments.size());
    }
}
