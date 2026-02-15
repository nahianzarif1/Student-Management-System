package com.example.student_management_system.repository;

import com.example.student_management_system.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("UserRepository Unit Tests")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
        testUser.setRole("ROLE_STUDENT");
    }

    @Test
    @DisplayName("Should save user successfully")
    void saveUser_Success() {
        // When
        User savedUser = userRepository.save(testUser);

        // Then
        assertNotNull(savedUser.getId());
        assertEquals("testuser", savedUser.getUsername());
        assertEquals("ROLE_STUDENT", savedUser.getRole());
    }

    @Test
    @DisplayName("Should find user by username")
    void findByUsername_Success() {
        // Given
        userRepository.save(testUser);

        // When
        User foundUser = userRepository.findByUsername("testuser");

        // Then
        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
    }

    @Test
    @DisplayName("Should return null when username not found")
    void findByUsername_NotFound() {
        // When
        User foundUser = userRepository.findByUsername("nonexistent");

        // Then
        assertNull(foundUser);
    }

    @Test
    @DisplayName("Should find all users")
    void findAll_Success() {
        // Given
        userRepository.save(testUser);

        User anotherUser = new User();
        anotherUser.setUsername("anotheruser");
        anotherUser.setPassword("password456");
        anotherUser.setRole("ROLE_TEACHER");
        userRepository.save(anotherUser);

        // When
        var users = userRepository.findAll();

        // Then
        assertEquals(2, users.size());
    }

    @Test
    @DisplayName("Should delete user by id")
    void deleteById_Success() {
        // Given
        User savedUser = userRepository.save(testUser);
        Long userId = savedUser.getId();

        // When
        userRepository.deleteById(userId);

        // Then
        assertFalse(userRepository.findById(userId).isPresent());
    }
}
