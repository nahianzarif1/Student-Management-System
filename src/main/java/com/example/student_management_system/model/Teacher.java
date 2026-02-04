package com.example.student_management_system.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString; // Import this to prevent potential loops later
import java.util.List;

@Entity
@Data
@Table(name = "teachers")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    // You can add a specific field for teachers if you want, e.g.:
    // private String designation;

    // --- NEW LINK TO USER (Login Info) ---
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    // -------------------------------------

    @ManyToOne
    @JoinColumn(name = "department_id")
    @ToString.Exclude // Good practice to exclude relations
    private Department department;

    @OneToMany(mappedBy = "teacher")
    @ToString.Exclude // Good practice to exclude relations
    private List<Course> courses;
}