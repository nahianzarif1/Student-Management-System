package com.example.student_management_system.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private int credits;

    // Many Students can be in this Course
    @ManyToMany(mappedBy = "courses")
    private List<Student> students;

    // One Teacher teaches this Course
    @ManyToOne
    @JoinColumn(name = "teacher_id") // Creates a Foreign Key column
    private Teacher teacher;
}