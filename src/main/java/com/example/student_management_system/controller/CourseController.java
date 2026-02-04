package com.example.student_management_system.controller;

import com.example.student_management_system.model.Course;
import com.example.student_management_system.model.Student;
import com.example.student_management_system.model.Teacher;
import com.example.student_management_system.model.User;
import com.example.student_management_system.repository.CourseRepository;
import com.example.student_management_system.repository.StudentRepository;
import com.example.student_management_system.repository.TeacherRepository;
import com.example.student_management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired private CourseRepository courseRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private TeacherRepository teacherRepository;
    @Autowired private StudentRepository studentRepository;

    @GetMapping
    public String listCourses(Model model, Principal principal) {
        model.addAttribute("courses", courseRepository.findAll());

        // If a user is logged in, send their Student profile to the view
        if (principal != null) {
            User user = userRepository.findByUsername(principal.getName());
            // Check if they are a student before adding
            if (user.getRole().equals("ROLE_STUDENT")) {
                Student student = studentRepository.findByUser(user);
                model.addAttribute("student", student);
            }
        }

        return "courses";
    }

    // 2. Show "Create Course" Form (Teachers Only)
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());
        return "create_course";
    }

    // 3. Save New Course (Teachers Only)
    @PostMapping("/save")
    public String saveCourse(@ModelAttribute Course course, Principal principal) {
        // Find the currently logged-in Teacher
        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        Teacher teacher = teacherRepository.findByUser(user); // We need to add this method to Repo!

        course.setTeacher(teacher); // Set the teacher as the creator
        courseRepository.save(course);
        return "redirect:/courses";
    }

    // 4. Toggle Enrollment (Enroll / Unenroll)
    @PostMapping("/enroll/{id}")
    public String toggleEnrollment(@PathVariable Long id, Principal principal) {
        // Find the Course
        Course course = courseRepository.findById(id).orElseThrow();

        // Find the logged-in Student
        User user = userRepository.findByUsername(principal.getName());
        Student student = studentRepository.findByUser(user);

        // CHECK: Is the student already enrolled?
        if (student.getCourses().contains(course)) {
            // YES -> UNENROLL (Remove the course)
            student.getCourses().remove(course);
        } else {
            // NO -> ENROLL (Add the course)
            student.getCourses().add(course);
        }

        // Save changes to update the 'student_courses' table
        studentRepository.save(student);

        return "redirect:/courses";
    }
}