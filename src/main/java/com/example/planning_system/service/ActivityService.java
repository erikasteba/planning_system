package com.example.planning_system.service;

import com.example.planning_system.models.Activities;
import com.example.planning_system.models.Day;
import com.example.planning_system.models.User;
import com.example.planning_system.repositories.ActivitiesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {
    @Autowired
    private ActivitiesRepository activitiesRepository;

    public List<LocalDate> getActivitiesDatesForUser(User user) {
        List<Activities> userActivities = activitiesRepository.findByUser(user);

        List<LocalDate> activityDates = userActivities.stream()
                .flatMap(activity -> {
                    List<LocalDate> dates = new ArrayList<>();
                    LocalDate currentDate = activity.getStartDate();
                    while (!currentDate.isAfter(activity.getEndDate())) {
                        dates.add(currentDate);
                        currentDate = currentDate.plusDays(1);
                    }
                    return dates.stream();
                })
                .collect(Collectors.toList());

        return activityDates;
    }

    public static String getCssColor(int index) {
        String[] colors = {
                "#FF5733", "#C70039", "#900C3F", "#581845", "#F39C12",
                "#F4D03F", "#D4AC0D", "#6E2C00", "#229954", "#27AE60",
                "#145A32", "#1F618D", "#2874A6", "#154360", "#8E44AD",
                "#9B59B6", "#5B2C6F", "#34495E", "#2E4053", "#212F3C"
        };

        if (index >= 0 && index < colors.length) {
            return colors[index];
        } else{
            Random random = new Random();
            int randomIndex = random.nextInt(colors.length);
            return colors[randomIndex];
    }
    }


}
