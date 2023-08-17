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


    @GetMapping("/calendar-week")
    public String showCalendarWeek(
            @RequestParam(name = "week", required = false, defaultValue = "2023-W33") String week,
            Model model) {
        model.addAttribute("selectedWeek", week);
        String[] weekParts = week.split("-W");
        int year = Integer.parseInt(weekParts[0]);
        int weekNumber = Integer.parseInt(weekParts[1]);
        LocalDate startDate = LocalDate.ofYearDay(year, 1).with(java.time.temporal.TemporalAdjusters.firstInMonth(java.time.DayOfWeek.MONDAY));
        startDate = startDate.plusWeeks(weekNumber - 1);
        List<Integer> dateNumbers = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            dateNumbers.add(startDate.getDayOfMonth());
            startDate = startDate.plusDays(1);}
        String month = startDate.getMonth().toString();
        model.addAttribute("dateNumbers", dateNumbers);
        model.addAttribute("month", month);
        return "calendar-week";
    }

    @GetMapping("/btnNextWeek")
    public RedirectView btnNextWeek(
            @RequestParam(name = "week", defaultValue = "2023-W33") String week) {

        String[] weekParts = week.split("-W");
        int year = Integer.parseInt(weekParts[0]);
        int weekNumber = Integer.parseInt(weekParts[1]) + 1;

        String newWeek = String.format("%d-W%d", year, weekNumber);

        String newUrl = "http://localhost:8080/calendar-week?week=" + newWeek;

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(newUrl);

        return redirectView;
    }


    @GetMapping("/btnPreviousWeek")
    public RedirectView btnPreviousWeek(
            @RequestParam(name = "week", defaultValue = "2023-W33") String week) {

        String[] weekParts = week.split("-W");
        int year = Integer.parseInt(weekParts[0]);
        int weekNumber = Integer.parseInt(weekParts[1]) - 1;

        String newWeek = String.format("%d-W%d", year, weekNumber);

        String newUrl = "http://localhost:8080/calendar-week?week=" + newWeek;

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(newUrl);

        return redirectView;
    }


    @GetMapping("/calendar/{day}/{month}")
    public String showEventDetails(@PathVariable int day, @PathVariable int month, Model model
    ) {

        String eventDetails = calendarService.getMonthName(month) + " " + day;
        model.addAttribute("eventDetails", eventDetails);
        return "day-details";
    }





}

