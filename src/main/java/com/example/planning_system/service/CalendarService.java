package com.example.planning_system.service;

import com.example.planning_system.models.Day;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CalendarService {
    public List<List<Day>> generateCalendarData(int year, int month) {
        List<List<Day>> weeks = new ArrayList<>();

        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        int daysInMonth = firstDayOfMonth.getMonth().length(firstDayOfMonth.isLeapYear());
        int dayOfWeek = firstDayOfMonth.getDayOfWeek().getValue(); // 1 = Monday, 7 = Sunday
        if (dayOfWeek == 7) {
            dayOfWeek = 1;
        } else {
            dayOfWeek += 1;
        }

        int day = 1;
        for (int week = 0; day <= daysInMonth; week++) {
            List<Day> weekData = new ArrayList<>();
            for (int dow = 1; dow <= 7; dow++) {
                if ((week == 0 && dow < dayOfWeek) || day > daysInMonth) {
                    weekData.add(null); // Padding days before and after the current month
                } else {
                    weekData.add(new Day(day));
                    day++;
                }
            }
            weeks.add(weekData);
        }

        return weeks;
    }

    public String getMonthName(int month) {
        String[] monthNames = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        return monthNames[month - 1];
    }
}
