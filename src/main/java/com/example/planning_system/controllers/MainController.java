package com.example.planning_system.controllers;

import com.example.planning_system.models.Activities;
import com.example.planning_system.models.Day;
import com.example.planning_system.models.User;
import com.example.planning_system.repositories.ActivitiesRepository;
import com.example.planning_system.models.User;
import com.example.planning_system.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.planning_system.service.WeekService.generateDateTimeList;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final CalendarService calendarService;
    private final ActivityService activityService;
    private static final Logger logger = LoggerFactory.getLogger(ActivitiesController.class);


    @Autowired
    private ActivitiesRepository activitiesRepository;

    @GetMapping("/")
    public String main(Model model) {
        return "test";
    }

    @GetMapping("/calendar")
    public String showCalendar(@RequestParam(name = "month", required = false) Integer month, Model model) {

        int defaultMonth = LocalDate.now().getMonthValue();
        if (month == null) {
            month = defaultMonth;
        }

        int year = LocalDate.now().getYear();
        List<List<LocalDate>> weeks = calendarService.generateCalendarData(year, month);
        model.addAttribute("header", calendarService.getMonthName(month) + " " + year);
        model.addAttribute("weeks", weeks);
        model.addAttribute("selectedMonth", month);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            logger.info("Month calendar displayed for user with id: {}", user.getId());
            List<LocalDate> activityDates = activityService.getActivitiesDatesForUser(user);
            logger.info("Activity dates: {}", activityDates);
            model.addAttribute("activityDates", activityDates);
        }

        return "calendar";
    }


    @GetMapping("/calendar-week")
    public String showCalendarWeek(//, defaultValue = "2023-W33"
            @RequestParam(name = "week", required = false) String week,
            Model model) {

        String defaultweek = String.valueOf(LocalDate.now().getYear()) + "-W" + String.valueOf(LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()));
        if (week == null || week.isEmpty()) {
            week = defaultweek;
        }

        model.addAttribute("selectedWeek", week);
        String[] weekParts = week.split("-W");
        int year = Integer.parseInt(weekParts[0]);
        int weekNumber = Integer.parseInt(weekParts[1]);

        LocalDate startDate = LocalDate.ofYearDay(year, 1).with(java.time.temporal.TemporalAdjusters.firstInMonth(java.time.DayOfWeek.MONDAY));
        startDate = startDate.plusWeeks(weekNumber - 1);
        List<Integer> dateNumbers = new ArrayList<>();
        List<Integer> dateMonths = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            dateNumbers.add(startDate.getDayOfMonth());

            //System.out.println(startDate.getDayOfMonth());
            dateMonths.add(startDate.getMonthValue());
            //System.out.println(startDate.getMonthValue());

            startDate = startDate.plusDays(1);
        }
        String month = startDate.getMonth().toString();
        int monthValue = startDate.getMonth().getValue();
        model.addAttribute("dateNumbers", dateNumbers);
            model.addAttribute("dateMonths", dateMonths);
        model.addAttribute("month", month);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();
        logger.info("Week calendar displayed for user with id: {}", user.getId());
        List<Activities> activities = activitiesRepository.findByUserId(userId);
        logger.info("Activities dates: {}", activities);

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

       //for (int i = 0; i < activities.size(); i++) {
       //    Activities activity = activities.get(i);
       //    List<LocalDateTime> dateTimeList = dateTimeLists.get(i);
       //    System.out.println("Activity: " + activity.getName());
       //    for (LocalDateTime dateTime : dateTimeList) {
       //        System.out.println(dateTime);
       //    }
       //    System.out.println();
       //}
        return "calendar-week";
    }

    @GetMapping("/btnNextWeek")
    public RedirectView btnNextWeek(//, defaultValue = "2023-W33"
            @RequestParam(name = "week") String week) {

        String defaultweek = String.valueOf(LocalDate.now().getYear()) + "-W" + String.valueOf(LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()));
        if (week == null || week.isEmpty()) {
            week = defaultweek;
        }


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
    public RedirectView btnPreviousWeek(//, defaultValue = "2023-W33"
            @RequestParam(name = "week") String week) {

        String defaultweek = String.valueOf(LocalDate.now().getYear()) + "-W" + String.valueOf(LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()));
        if (week == null || week.isEmpty()) {
            week = defaultweek;
        }


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

