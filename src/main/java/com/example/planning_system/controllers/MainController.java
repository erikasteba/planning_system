package com.example.planning_system.controllers;

import com.example.planning_system.models.Day;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
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



    private String getMonthName(int month) {
        String[] monthNames = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        return monthNames[month - 1];
    }
    @GetMapping("/calendar/{day}/{month}")
    public String showEventDetails(@PathVariable int day, @PathVariable int month, Model model
    ) {

        String eventDetails = "Event details for " + getMonthName(month) + " " + day;
        model.addAttribute("eventDetails", eventDetails);
        return "day-details";
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

