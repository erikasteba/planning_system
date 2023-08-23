package com.example.planning_system.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WeekServiceTest {

    @InjectMocks
    private WeekService underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testGenerateDateTimeList_Success() {
        LocalDate startDate = LocalDate.of(2023, 8, 1);
        LocalTime startTime = LocalTime.of(10, 0);
        LocalDate endDate = LocalDate.of(2023, 8, 1);
        LocalTime endTime = LocalTime.of(14, 0);

        List<LocalDateTime> dateTimeList = underTest.generateDateTimeList(startDate, startTime, endDate, endTime);

        assertEquals(5, dateTimeList.size()); // 10:00, 11:00, 12:00, 13:00, 14:00

        LocalDateTime expectedFirstDateTime = LocalDateTime.of(2023, 8, 1, 10, 0);
        LocalDateTime expectedLastDateTime = LocalDateTime.of(2023, 8, 1, 14, 0);
        assertEquals(expectedFirstDateTime, dateTimeList.get(0));
        assertEquals(expectedLastDateTime, dateTimeList.get(dateTimeList.size() - 1));
    }

    @Test
    public void testGenerateDateTimeList_EmptyList() {
        LocalDate startDate = LocalDate.of(2023, 8, 1);
        LocalTime startTime = LocalTime.of(14, 0);
        LocalDate endDate = LocalDate.of(2023, 8, 1);
        LocalTime endTime = LocalTime.of(10, 0);

        List<LocalDateTime> dateTimeList = underTest.generateDateTimeList(startDate, startTime, endDate, endTime);

        assertTrue(dateTimeList.isEmpty());
    }
}