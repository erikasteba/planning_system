package com.example.planning_system.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class WeekService {


    public static void main(String[] args) {
        LocalDate startDate = LocalDate.of(2023, 8, 14);
        LocalTime startTime = LocalTime.of(19, 38);
        LocalDate endDate = LocalDate.of(2023, 8, 17);
        LocalTime endTime = LocalTime.of(21, 38);

        List<LocalDateTime> dateTimeList = generateDateTimeList(startDate, startTime, endDate, endTime);

        for (LocalDateTime dateTime : dateTimeList) {
            System.out.println(dateTime);
        }
    }

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
