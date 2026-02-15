package com.example.student_management_system.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("HomeController Unit Tests")
class HomeControllerTest {

    @InjectMocks
    private HomeController homeController;

    @Test
    @DisplayName("Should return home view - GET /")
    void home_Success() {
        String viewName = homeController.home();
        assertEquals("home", viewName);
    }

    @Test
    @DisplayName("Should return correct view name")
    void home_ReturnsCorrectViewName() {
        String result = homeController.home();
        assertNotNull(result);
        assertEquals("home", result);
    }

    @Test
    @DisplayName("View name should not be null")
    void home_ViewNameNotNull() {
        String viewName = homeController.home();
        assertNotNull(viewName);
    }
}
