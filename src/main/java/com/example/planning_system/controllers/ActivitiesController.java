package com.example.planning_system.controllers;

import com.example.planning_system.models.Activities;
import com.example.planning_system.models.User;
import com.example.planning_system.repositories.ActivitiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;



@Controller
public class ActivitiesController {

    @Autowired
    private ActivitiesRepository activitiesRepository;


    @GetMapping("/calendar/activities")
    public String showActivityForm() {
        return "day-details";
    }
    @PostMapping("/calendar/activities")
    public String createActivity(
            @RequestParam String activity_name,
            @RequestParam String start_time,
            @RequestParam String end_time,
            @RequestParam(defaultValue = "false") boolean is_public,
            Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long userId = null;
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            userId = user.getId();


            String errorMessage = "";
            try {
                startDateTime = LocalDateTime.parse(start_time);
                endDateTime = LocalDateTime.parse(end_time);

                //model.addAttribute("activity_name", activity_name);
                //model.addAttribute("start_time", start_time);
                //model.addAttribute("end_time", end_time);
                //model.addAttribute("is_public", is_public);

                if (startDateTime.isAfter(endDateTime)) {
                    errorMessage = "Please choose correct dates or time.";
                    model.addAttribute("errorMessage", errorMessage);
                    return "day-details";
                }
            } catch (DateTimeParseException e) {
                errorMessage = "Invalid date and time format.";
                model.addAttribute("errorMessage", errorMessage);
                return "day-details";
            }


        }


        Activities activities = new Activities(activity_name, startDateTime.toString(), endDateTime.toString(), is_public);
        activitiesRepository.save(activities);


        return "day-details";


    }

}
