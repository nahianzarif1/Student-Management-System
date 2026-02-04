package com.example.student_management_system.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    // A Course is taught by ONE Teacher
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    @ToString.Exclude
    private Teacher teacher;

    // A Course has MANY Students
    @ManyToMany(mappedBy = "courses")
    @ToString.Exclude
    private List<Student> students = new ArrayList<>();
}