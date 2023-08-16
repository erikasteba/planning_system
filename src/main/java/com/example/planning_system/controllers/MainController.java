package com.example.planning_system.controllers;

import com.example.planning_system.models.Day;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {
    @GetMapping("/")
    public String main(Model model){
        return "main";
    }

    @GetMapping("/calendar")
    public String showCalendar(@RequestParam(name = "month", defaultValue = "1") int month, Model model) {
        int year = LocalDate.now().getYear();
        List<List<Day>> weeks = generateCalendarData(year, month);
        model.addAttribute("header", getMonthName(month) + " " + year);
        model.addAttribute("weeks", weeks);
        return "calendar";
    }

    private String getMonthName(int month) {
        String[] monthNames = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        return monthNames[month - 1];
    }

    private List<List<Day>> generateCalendarData(int year, int month) {
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
}

