package com.example.planning_system.controllers;

import com.example.planning_system.models.Activities;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@Controller
public class ActivitiesController {

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
            Model model){

        String errorMessage="";
        try {
            LocalDateTime startDateTime = LocalDateTime.parse(start_time);
            LocalDateTime endDateTime = LocalDateTime.parse(end_time);

            model.addAttribute("activity_name", activity_name);
            model.addAttribute("is_public", is_public);

            if (startDateTime.isAfter(endDateTime)) {
                errorMessage = "Please choose correct dates or time.";
                model.addAttribute("errorMessage", errorMessage);
                return "day-details";
            }
        }catch (DateTimeParseException e) {
            errorMessage = "Invalid date and time format.";
            model.addAttribute("errorMessage", errorMessage);
            return "day-details";
        }

        errorMessage = "";
        return "day-details";

    }


}
