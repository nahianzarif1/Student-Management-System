package com.example.student_management_system.controller;

import com.example.student_management_system.model.Department; // <--- NEW IMPORT
import com.example.student_management_system.model.Student;
import com.example.student_management_system.repository.DepartmentRepository; // <--- NEW IMPORT
import com.example.student_management_system.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DepartmentRepository departmentRepository; // <--- NEW REPOSITORY INJECTED

    // 1. List all students
    @GetMapping
    public String listStudents(Model model) {
        List<Student> students = studentRepository.findAll();
        model.addAttribute("students", students);
        return "students";
    }

    // 2. Show the "Add Student" Form
    @GetMapping("/new")
    public String createStudentForm(Model model) {
        Student student = new Student();
        model.addAttribute("student", student);
        return "create_student";
    }

    // 3. Save the Student (UPDATED METHOD)
    @PostMapping
    public String saveStudent(@ModelAttribute("student") Student student,
                              @RequestParam("deptName") String deptName) { // We get the text name separately

        // A. Look for the department in the database
        Department department = departmentRepository.findByName(deptName);

        // B. If it doesn't exist, create a new one!
        if (department == null) {
            department = new Department();
            department.setName(deptName);
            departmentRepository.save(department);
        }

        // C. Attach the department to the student
        student.setDepartment(department);

        // D. Save the student
        studentRepository.save(student);

        return "redirect:/students";
    }

    // 4. Delete a Student
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentRepository.deleteById(id);
        return "redirect:/students";
    }

    // 5. Show Edit Form (GET)
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student != null) {
            model.addAttribute("student", student);
            return "edit_student"; // We will create this file next!
        }
        return "redirect:/students";
    }

    // 6. Update Student (POST)
    @PostMapping("/{id}")
    public String updateStudent(@PathVariable Long id,
                                @ModelAttribute("student") Student student,
                                @RequestParam("deptName") String deptName) {

        // Find existing student
        Student existingStudent = studentRepository.findById(id).orElse(null);

        if (existingStudent != null) {
            existingStudent.setName(student.getName());
            existingStudent.setEmail(student.getEmail());

            // Update Department Logic (Same as before)
            Department department = departmentRepository.findByName(deptName);
            if (department == null) {
                department = new Department();
                department.setName(deptName);
                departmentRepository.save(department);
            }
            existingStudent.setDepartment(department);

            studentRepository.save(existingStudent);
        }
        return "redirect:/students";
    }
}