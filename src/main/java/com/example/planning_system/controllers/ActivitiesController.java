package com.example.planning_system.controllers;

import com.example.planning_system.models.Activities;
import com.example.planning_system.models.User;
import com.example.planning_system.repositories.ActivitiesRepository;
import com.example.planning_system.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;


@Controller
public class ActivitiesController {

    @Autowired
    private ActivitiesRepository activitiesRepository;
    @Autowired
    private UserRepository userRepository;

    String errorMessage = "";

    @GetMapping("/calendar/activities")
    public String showActivityForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();


        List<Activities> activities = activitiesRepository.findByUserId(userId);
        model.addAttribute("activities", activities);
        model.addAttribute("errorMessage", errorMessage);
        System.out.println(activities);
        System.out.println("test");

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
        //String errorMessage = "";
        Long userId = null;
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            userId = user.getId();


            try {
                startDateTime = LocalDateTime.parse(start_time);
                endDateTime = LocalDateTime.parse(end_time);

                if (startDateTime.isAfter(endDateTime)) {
                    errorMessage = "Please choose correct dates or time.";
                    model.addAttribute("errorMessage", errorMessage);
                    return "redirect:/calendar/activities";
                }
            } catch (DateTimeParseException e) {
                errorMessage = "Invalid date and time format.";
                model.addAttribute("errorMessage", errorMessage);
                return "day-details";
            }


        }

        Activities activity = new Activities(activity_name, startDateTime.toString(), endDateTime.toString(), is_public);

        activitiesRepository.save(activity);
        //System.out.println("dasdaddada" + userId);
        User userEntity = userRepository.findById(userId).orElse(null);
        if (userEntity != null) {
            activity.setUser(userEntity);
            activitiesRepository.save(activity);
        } else {
            errorMessage = "User not found.";
            model.addAttribute("errorMessage", errorMessage);
            return "redirect:/calendar/activities";
        }
        errorMessage=" ";
        return "redirect:/calendar/activities";
    }


    @PostMapping("/calendar/activities/delete/{act_id}")
    public String deleteActivity(@PathVariable("act_id") Long act_id) {
        Optional<Activities> activities = activitiesRepository.findById(act_id);
        if (activities.isPresent()) {
            activitiesRepository.delete(activities.get());
        }
        return "redirect:/calendar/activities";
    }

    @GetMapping("/calendar/activities/edit/{act_id}")
    public String showActivityEditForm(@PathVariable("act_id") Long act_id, Model model) {
        Optional<Activities> activities = activitiesRepository.findById(act_id);
        model.addAttribute("activities", activities);
        Activities activity = activities.get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();
        User activityUser = activity.getUser();
        if (!activity.getUser().getId().equals(userId)) {
            return "redirect:/calendar/activities";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String startTimeString = activity.getStartTime().format(timeFormatter);
        String startDateString = activity.getStartDate().format(formatter);
        String startDateTimeString = startDateString + 'T' + startTimeString;

        String endTimeString = activity.getEndTime().format(timeFormatter);
        String endDateString = activity.getEndDate().format(formatter);
        String endDateTimeString = endDateString + 'T' + endTimeString;


        model.addAttribute("name", activity.getName());
        model.addAttribute("startDateTimeString", startDateTimeString);
        model.addAttribute("endDateTimeString", endDateTimeString);
        model.addAttribute("isPublic", activity.isPublic());
        model.addAttribute("errorMessage", errorMessage);

        return "edit-activities";
    }

    @PostMapping("/calendar/activities/edit/{act_id}")
    public String editActivity(@PathVariable("act_id") Long act_id, Model model,
                               @RequestParam String activity_name,
                               @RequestParam String start_time,
                               @RequestParam String end_time,
                               @RequestParam(name = "isPublic", defaultValue = "false") boolean isPublic) {

        Optional<Activities> activities = activitiesRepository.findById(act_id);
        Activities activity = activities.get();
        model.addAttribute("act_id", act_id);


        String[] startParts = start_time.split("T");
        String[] endParts = end_time.split("T");
        LocalDate startDate = LocalDate.parse(startParts[0]);
        LocalTime startTime = LocalTime.parse(startParts[1]);
        LocalDate endDate = LocalDate.parse(endParts[0]);
        LocalTime endTime = LocalTime.parse(endParts[1]);

        LocalDateTime startDateTimeEdit = null;
        LocalDateTime endDateTimeEdit = null;

        String urlAgain = "/calendar/activities/edit/" + act_id;

        try {
            startDateTimeEdit = LocalDateTime.parse(start_time);
            endDateTimeEdit = LocalDateTime.parse(end_time);
            if (startDateTimeEdit.isAfter(endDateTimeEdit)) {
                errorMessage = "Please choose correct dates or time.";
                //model.addAttribute("errorMessage", errorMessage);
                return "redirect:"+urlAgain;
            }
        } catch (DateTimeParseException e) {
            errorMessage = "Invalid date and time format.";
            //model.addAttribute("errorMessage", errorMessage);
            return "redirect:"+urlAgain;
        }

        activity.setName(activity_name);
        activity.setStartTime(startTime);
        activity.setStartDate(startDate);
        activity.setEndTime(endTime);
        activity.setEndDate(endDate);
        activity.setPublic(isPublic);
        errorMessage = " ";
        activitiesRepository.save(activity);

        return "redirect:/calendar/activities";
    }

}
