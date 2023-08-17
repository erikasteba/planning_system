package com.example.planning_system.controllers;

import com.example.planning_system.models.Day;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import com.example.planning_system.service.CalendarService;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final CalendarService calendarService;
    @GetMapping("/")
    public String main(Model model){
        return "test";
    }

    @GetMapping("/calendar")
    public String showCalendar(@RequestParam(name = "month", defaultValue = "1") int month, Model model) {
        int year = LocalDate.now().getYear();
        List<List<Day>> weeks = calendarService.generateCalendarData(year, month);
        model.addAttribute("header", calendarService.getMonthName(month) + " " + year);
        model.addAttribute("weeks", weeks);
        model.addAttribute("selectedMonth", month); // Pass selectedMonth to the template
        return "calendar";
        //
    }





    @GetMapping("/calendar/{day}/{month}")
    public String showEventDetails(@PathVariable int day, @PathVariable int month, Model model
    ) {

        String eventDetails = calendarService.getMonthName(month) + " " + day;
        model.addAttribute("eventDetails", eventDetails);
        return "day-details";
    }





}

