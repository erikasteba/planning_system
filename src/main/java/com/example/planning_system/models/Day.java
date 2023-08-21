package com.example.planning_system.models;

import java.time.LocalDate;

public class Day {


    private int dayOfMonth;
    private LocalDate Date ;
    public Day(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
        this.Date = LocalDate.parse("2023-08-02");
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }
}
