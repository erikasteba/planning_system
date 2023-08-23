package com.example.planning_system.controllers;

import com.example.planning_system.models.Activities;
import com.example.planning_system.models.User;
import com.example.planning_system.repositories.ActivitiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Comparator;
@Controller
public class HomeController {

    @Autowired
    ActivitiesRepository activitiesRepository;

    @GetMapping("/index")
    public String home(Model model, @AuthenticationPrincipal User user){

        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        DayOfWeek currentDayOfWeek = today.getDayOfWeek();

        List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
        int currentMonthIndex = today.getMonthValue() - 1;
        String currentMonth = months.get(currentMonthIndex);
        int currentDay = today.getDayOfMonth();



        LocalDateTime currentDateTime = LocalDateTime.now();

        List<Activities> todaysActivities = activitiesRepository.findByUserAndStartDateEquals(user, today);

        todaysActivities.forEach(activity -> {
            LocalDateTime activityStartDateTime = LocalDateTime.of(activity.getStartDate(), activity.getStartTime());
            LocalDateTime activityEndDateTime = LocalDateTime.of(activity.getEndDate(), activity.getEndTime());

            boolean isActiveNow = currentDateTime.isAfter(activityStartDateTime) && currentDateTime.isBefore(activityEndDateTime);
            activity.setActiveNow(isActiveNow);
        });

        todaysActivities.sort(Comparator.comparing(Activities::getStartTime));

        model.addAttribute("currentMonth", currentMonth);
        model.addAttribute("currentDay", currentDay);
        model.addAttribute("currentDayOfWeek", currentDayOfWeek);
        model.addAttribute("todaysActivities", todaysActivities);
        model.addAttribute("user", user);

        return "index";
    }

}
