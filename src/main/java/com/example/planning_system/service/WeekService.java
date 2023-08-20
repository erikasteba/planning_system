package com.example.planning_system.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class WeekService {

    public static List<LocalDateTime> generateDateTimeList(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        List<LocalDateTime> dateTimeList = new ArrayList<>();

        LocalDateTime currentDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

        while (currentDateTime.isBefore(endDateTime) || currentDateTime.isEqual(endDateTime)) {
            dateTimeList.add(currentDateTime);
            currentDateTime = currentDateTime.plusHours(1);
        }

        return dateTimeList;
    }

}
