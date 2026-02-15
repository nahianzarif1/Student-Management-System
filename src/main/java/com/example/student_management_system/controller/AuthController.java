package com.example.student_management_system.controller;

import com.example.student_management_system.model.Student;
import com.example.student_management_system.model.Teacher;
import com.example.student_management_system.model.User;
import com.example.student_management_system.repository.StudentRepository;
import com.example.student_management_system.repository.TeacherRepository;
import com.example.student_management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired private UserRepository userRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private TeacherRepository teacherRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    // 1. Show the Sign-Up Form
    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    // 1.1 Show the Login Form
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // 2. Process the Sign-Up
    @PostMapping("/register")
    public String registerUser(User user) {
        // A. Encode the password (Security Best Practice)
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // B. Ensure the role has the "ROLE_" prefix
        if (!user.getRole().startsWith("ROLE_")) {
            user.setRole("ROLE_" + user.getRole());
        }

        // C. Save the User Login Info first
        User savedUser = userRepository.save(user);

        // D. Create the Linked Profile (Student or Teacher)
        if (user.getRole().equals("ROLE_STUDENT")) {
            Student student = new Student();
            student.setName(user.getUsername());
            student.setEmail(user.getUsername() + "@example.com"); // Placeholder email
            student.setUser(savedUser); // <--- LINKING HAPPENS HERE
            studentRepository.save(student);
        }
        else if (user.getRole().equals("ROLE_TEACHER")) {
            Teacher teacher = new Teacher();
            teacher.setName(user.getUsername());
            teacher.setUser(savedUser); // <--- LINKING HAPPENS HERE
            teacherRepository.save(teacher);
        }

        return "redirect:/login"; // Success! Go to log in
    }
}
