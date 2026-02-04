package com.example.student_management_system.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import java.util.List;

@Entity
@Data
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    // Security Role
    private String role = "STUDENT";

    // LINK TO DEPARTMENT (Many Students -> One Department)
    @ManyToOne
    @JoinColumn(name = "department_id")
    @ToString.Exclude
    private Department department;

    // LINK TO COURSES (Many Students <-> Many Courses)
    // This automatically creates a middle table called "student_courses"
    @ManyToMany
    @JoinTable(
            name = "student_courses",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @ToString.Exclude
    private List<Course> courses;
}