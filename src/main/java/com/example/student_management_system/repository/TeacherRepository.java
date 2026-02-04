package com.example.student_management_system.repository;

import com.example.student_management_system.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    // This gives us standard database methods like save(), findAll(), etc.
    Teacher findByUser(com.example.student_management_system.model.User user);
}