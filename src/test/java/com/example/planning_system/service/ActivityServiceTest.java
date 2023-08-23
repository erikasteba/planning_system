package com.example.planning_system.service;

import com.example.planning_system.models.Activities;
import com.example.planning_system.models.Activity;
import com.example.planning_system.models.User;
import com.example.planning_system.repositories.ActivitiesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @Mock
    private ActivitiesRepository activitiesRepository;


    @InjectMocks
    private ActivityService underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetActivitiesDatesForUser_Success() {
        User user = new User();
        user.setId(1L);

        Activities activity1 = new Activities();
        activity1.setUser(user);
        activity1.setStartDate(LocalDate.of(2023, 8, 1));
        activity1.setEndDate(LocalDate.of(2023, 8, 3));

        Activities activity2 = new Activities();
        activity2.setUser(user);
        activity2.setStartDate(LocalDate.of(2023, 8, 5));
        activity2.setEndDate(LocalDate.of(2023, 8, 7));

        List<Activities> userActivities = new ArrayList<>();
        userActivities.add(activity1);
        userActivities.add(activity2);

        when(activitiesRepository.findByUser(user)).thenReturn(userActivities);

        List<LocalDate> result = underTest.getActivitiesDatesForUser(user);

        assertNotNull(result);
        assertEquals(6, result.size()); // 3 days for activity1 and 3 days for activity2
        assertTrue(result.contains(LocalDate.of(2023, 8, 1)));
        assertTrue(result.contains(LocalDate.of(2023, 8, 2)));
        assertTrue(result.contains(LocalDate.of(2023, 8, 3)));
        assertTrue(result.contains(LocalDate.of(2023, 8, 5)));
        assertTrue(result.contains(LocalDate.of(2023, 8, 6)));
        assertTrue(result.contains(LocalDate.of(2023, 8, 7)));
    }

    @Test
    public void testGetActivitiesDatesForUser_NoActivities() {
        User user = new User();
        user.setId(1L);

        when(activitiesRepository.findByUser(user)).thenReturn(new ArrayList<>());

        List<LocalDate> result = underTest.getActivitiesDatesForUser(user);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}