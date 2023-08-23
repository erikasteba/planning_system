package com.example.planning_system.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalendarServiceTest {

    @Test
    public void testGenerateCalendarData_Success() {
        CalendarService calendarService = new CalendarService();

        int year = 2023;
        int month = 8;

        List<List<LocalDate>> calendarData = calendarService.generateCalendarData(year, month);

        assertNotNull(calendarData);
        assertEquals(5, calendarData.size()); // August 2023 has 5 weeks

        List<LocalDate> firstWeek = calendarData.get(0);
        assertEquals(7, firstWeek.size());
    }

    @Test
    public void testGetMonthName_ValidMonth() {
        CalendarService calendarService = new CalendarService();

        int month = 4;
        String monthName = calendarService.getMonthName(month);

        assertNotNull(monthName);
        assertEquals("April", monthName);
    }
}