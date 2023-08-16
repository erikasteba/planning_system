package com.example.planning_system.models;

import java.time.LocalDate;
import java.time.Month;
public class Activity extends Calendar{
    private String[][][] activities;
 private int year;
    public Activity(int year) {
        this.year=year;
        this.activities = new String[12][31][24]; // Month-Day-Hour
    }
    public void addActivity(int month, int day, int hour, String activity) {
        if (month >= 1 && month <= 12 && day >= 1 && day <= 31 && hour >= 0 && hour <= 23) {
            activities[month - 1][day - 1][hour] = activity;
        }
    }
    public String getActivity(int month, int day, int hour) {
        if (month >= 1 && month <= 12 && day >= 1 && day <= 31 && hour >= 0 && hour <= 23) {
            return activities[month - 1][day - 1][hour];
        }
        return null;
    }
    public static void main(String[] args) {
        Activity activityCalendar = new Activity(2023);

        activityCalendar.addActivity(5, 10, 15, "Meeting");
        activityCalendar.addActivity(5, 10, 16, "Gym");

        int monthToShow = 5;
        activityCalendar.showDate(monthToShow);
        for (int day = 1; day <= 31; day++) {
            for (int hour = 0; hour < 24; hour++) {
                String activity = activityCalendar.getActivity(monthToShow, day, hour);
                if (activity != null) {
                    System.out.printf("%02d/%02d %02d:00 - %s\n", monthToShow, day, hour, activity);
                }
            }
        }
    }
}
