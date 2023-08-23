package com.example.planning_system.controllers;

import com.example.planning_system.models.Activities;
import com.example.planning_system.models.User;
import com.example.planning_system.repositories.ActivitiesRepository;
import com.example.planning_system.service.ActivityService;
import com.example.planning_system.service.CalendarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MainControllerTest {
    private MainController underTest;

    @Mock
    private ActivitiesRepository activitiesRepository;

    @Mock
    private CalendarService calendarService;

    @Mock
    private ActivityService activityService;

    @Mock
    private Model model;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new MainController(calendarService, activityService);
    }

    @Test
    public void testBtnNextWeek_Success() {
        RedirectView redirectView = underTest.btnNextWeek("2023-W33");

        String expectedNewUrl = "http://localhost:8080/calendar-week?week=2023-W34";
        assertEquals(expectedNewUrl, redirectView.getUrl());
    }

    @Test
    public void testBtnPreviousWeek_Success() {
        RedirectView redirectView = underTest.btnPreviousWeek("2023-W33");

        String expectedNewUrl = "http://localhost:8080/calendar-week?week=2023-W32";
        assertEquals(expectedNewUrl, redirectView.getUrl());
    }
}