package com.example.planning_system.controllers;

import com.example.planning_system.models.Activities;
import com.example.planning_system.models.Notes;
import com.example.planning_system.models.User;
import com.example.planning_system.repositories.ActivitiesRepository;
import com.example.planning_system.repositories.NotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.*;
import java.util.*;

@Controller
public class HomeController {

    @Autowired
    ActivitiesRepository activitiesRepository;

    @Autowired
    NotesRepository notesRepository;

    @GetMapping("/index")
    public String home(Model model){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();
        List<Notes> notes = notesRepository.findByUserId(userId);
        model.addAttribute("notes", notes);

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







        //LISTS CITIES WITH TIME ZONES.   (EXAMPLE: EUROPE/RIGA +3)
        //In index.html
        Set<String> allTimeZones = ZoneId.getAvailableZoneIds();
        TreeSet<String> sortedTimeZones = new TreeSet<>();
        for (String timeZone : allTimeZones) {
            if (timeZone.startsWith("Europe/") || timeZone.startsWith("Asia/") ||
                    timeZone.startsWith("Africa/") || timeZone.startsWith("America/") ||
                    timeZone.startsWith("Australia/")) {
                ZoneOffset offset = ZoneId.of(timeZone).getRules().getOffset(java.time.Instant.now());
                int hours = offset.getTotalSeconds() / 3600;
                String offsetString = String.format("%s%d", hours >= 0 ? "+" : "", hours);

                sortedTimeZones.add(timeZone + " " + offsetString);
            }}
        model.addAttribute("timeZoneOffsets", sortedTimeZones);



        return "index";
    }



}
