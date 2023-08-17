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


}
