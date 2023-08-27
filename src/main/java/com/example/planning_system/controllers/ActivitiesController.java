package com.example.planning_system.controllers;

import com.example.planning_system.models.Activities;
import com.example.planning_system.models.User;
import com.example.planning_system.repositories.ActivitiesRepository;
import com.example.planning_system.repositories.UserRepository;
import org.slf4j.MDC;
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


import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller
public class ActivitiesController {

    @Autowired
    private ActivitiesRepository activitiesRepository;
    @Autowired
    private UserRepository userRepository;

    String errorMessage = "";

    private static final Logger logger = LoggerFactory.getLogger(ActivitiesController.class);


    @GetMapping("/calendar/activities")
    public String showActivityForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();
        String userName = user.getName();

        List<Activities> activities = activitiesRepository.findByUserId(userId);
        Collections.sort(activities, Comparator.comparing(Activities::getStartDate));
        model.addAttribute("activities", activities);
        model.addAttribute("errorMessage", errorMessage);

        logger.info("Displaying activities for user with ID: {} and username {}", userId, userName);

        //LISTS CITIES WITH TIME ZONES.   (EXAMPLE: EUROPE/RIGA +3)
        //In html
        Set<String> allTimeZones = ZoneId.getAvailableZoneIds();
        List<Integer> allHours = new ArrayList<>();
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

        for (String input: sortedTimeZones) {
            String regex = "[-+]?\\b\\d+\\b";

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(input);

            while (matcher.find()) {
                String integerStr = matcher.group();
                int integerValue = Integer.parseInt(integerStr);
                allHours.add(integerValue);
            }
        }
        model.addAttribute("timeZoneOffsets", sortedTimeZones);
        model.addAttribute("allHours", allHours);

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
                    logger.warn("Failed to add activity for userId: {} with error: {}", userId, errorMessage);
                    return "redirect:/calendar/activities";
                }
            } catch (DateTimeParseException e) {
                errorMessage = "Invalid date and time format.";
                model.addAttribute("errorMessage", errorMessage);
                logger.warn("Failed to add activity for userId: {} with error: {}", userId, errorMessage);
                return "day-details";
            }


        }

        Activities activity = new Activities(activity_name, startDateTime.toString(), endDateTime.toString(), is_public);
        activitiesRepository.save(activity);

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
        logger.info("New activity added for userId: {}", userId);
        logger.info("Parametrs: name: {}, start-time: {}, end-time: {}, is-public: {}", activity_name, start_time, end_time, is_public);

        return "redirect:/calendar/activities";
    }


    @PostMapping("/calendar/activities/delete/{act_id}")
    public String deleteActivity(@PathVariable("act_id") Long act_id) {

        try {
        Optional<Activities> activities = activitiesRepository.findById(act_id);

        if (activities.isPresent()) {
            Activities activity = activities.get();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            Long userId = user.getId();
            if (activity.getUser().getId().equals(userId)) {
                logger.info("Activity with id {} deleted by user with id: {}", act_id, userId);
                activitiesRepository.delete(activity);
            }
        }} catch (Exception e) {e.printStackTrace();}
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
                               @RequestParam(name = "isPublic", defaultValue = "false") boolean isPublic,
                               @RequestParam String coordinates) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();

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

        String[] parts = coordinates.split(", ");
        if (parts.length != 2) {
            // Handle incorrect input
        }
        Double latitude = Double.parseDouble(parts[0]);
        Double longitude = Double.parseDouble(parts[1]);

        activity.setLatitude(latitude);
        activity.setLongitude(longitude);
        activity.setName(activity_name);
        activity.setStartTime(startTime);
        activity.setStartDate(startDate);
        activity.setEndTime(endTime);
        activity.setEndDate(endDate);
        activity.setPublic(isPublic);
        errorMessage = " ";
        activitiesRepository.save(activity);

        logger.info("Activity with id {} edited by user with id: {}", act_id, userId);
        logger.info("Parametrs: name: {}, start-time: {}, end-time: {}, is-public: {}", activity_name, start_time, end_time, isPublic);

        return "redirect:/calendar/activities";
    }

}
