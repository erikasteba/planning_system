package com.example.planning_system.controllers;

import com.example.planning_system.models.Activities;
import com.example.planning_system.models.Day;
import com.example.planning_system.models.User;
import com.example.planning_system.repositories.ActivitiesRepository;
import com.example.planning_system.models.User;
import com.example.planning_system.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import com.example.planning_system.service.CalendarService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import static com.example.planning_system.service.WeekService.generateDateTimeList;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final CalendarService calendarService;
    private final ActivityService activityService;

    @Autowired
    private ActivitiesRepository activitiesRepository;

    @GetMapping("/")
    public String main(Model model){
        return "test";
    }

    @GetMapping("/calendar")
    public String showCalendar(@RequestParam(name = "month", defaultValue = "1") int month, Model model) {
        int year = LocalDate.now().getYear();
        List<List<LocalDate>> weeks = calendarService.generateCalendarData(year, month);
        model.addAttribute("header", calendarService.getMonthName(month) + " " + year);
        model.addAttribute("weeks", weeks);
        model.addAttribute("selectedMonth", month);

        // Get the current authenticated user (if available)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            List<LocalDate> activityDates = activityService.getActivitiesDatesForUser(user);
            for (LocalDate s: activityDates) {
                System.out.println(s.toString());
            }
            model.addAttribute("activityDates", activityDates);
        }

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
        int monthValue = startDate.getMonth().getValue();
        model.addAttribute("dateNumbers", dateNumbers);
        model.addAttribute("month", month);
        model.addAttribute("monthValue", monthValue);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        User user = (User) authentication.getPrincipal();
        userId = user.getId();

        List<Activities> activities = activitiesRepository.findByUserId(userId);

        System.out.println(activities);

        if (!activities.isEmpty()) {
            Activities activity = activities.get(2);
            model.addAttribute("activity", activity);

            System.out.println(activity.getStartDate());
            System.out.println(activity.getStartTime());
            System.out.println(activity.getEndDate());
            System.out.println(activity.getEndTime());

        }

        List<List<LocalDateTime>> dateTimeLists = new ArrayList<>();

        for (Activities activity : activities) {
            List<LocalDateTime> dateTimeList = generateDateTimeList(
                    activity.getStartDate(),
                    activity.getStartTime(),
                    activity.getEndDate(),
                    activity.getEndTime()
            );
            dateTimeLists.add(dateTimeList);
        }
        model.addAttribute("dateTimeLists", dateTimeLists);
        model.addAttribute("activities", activities);

        for (int i = 0; i < activities.size(); i++) {
            Activities activity = activities.get(i);
            List<LocalDateTime> dateTimeList = dateTimeLists.get(i);
            System.out.println("Activity: " + activity.getName());
            for (LocalDateTime dateTime : dateTimeList) {
                System.out.println(dateTime);
            }
            System.out.println();
        }




        //LocalDate startDatee = LocalDate.of(2023, 8, 14);
        //LocalTime startTime = LocalTime.of(19, 38);
        //LocalDate endDate = LocalDate.of(2023, 8, 17);
        //LocalTime endTime = LocalTime.of(21, 38);
        //
        //List<LocalDateTime> dateTimeList = generateDateTimeList(startDatee, startTime, endDate, endTime);
        //
        //for (LocalDateTime dateTime : dateTimeList) {
        //    System.out.println(dateTime);
        //}


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

