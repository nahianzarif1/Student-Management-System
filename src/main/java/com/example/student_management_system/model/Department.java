package com.example.student_management_system.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import java.util.List;

@Entity
@Data
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // One Department has many Students
    // "mappedBy" tells Hibernate: "Go look at the 'department' field in the Student class to find the configuration."
    @OneToMany(mappedBy = "department")
    @ToString.Exclude
    private List<Student> students;

    // One Department has many Teachers
    @OneToMany(mappedBy = "department")
    @ToString.Exclude
    private List<Teacher> teachers;
}