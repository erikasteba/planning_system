package com.example.planning_system.models;
import java.time.LocalDate;
import java.time.Month;

public class Calendar {
    public static void showDate(int month){
//        LocalDate dateNow = LocalDate.now();
//        int year = dateNow.getYear();
//        int day = LocalDate.of(dateNow.getYear(), Month.JANUARY, 1).getDayOfWeek().getValue();
//
//        String header;/*from  w  ww  . j  a  v  a2 s . c om*/
//        System.out.println();
//        for (int month = 1; month <= 12; month++) {
//            header = "";
//            // Concatenate current month string to header
//            switch (month) {
//                case 1:
//                    header += "January ";
//                    break;
//                case 2:
//                    header += "February ";
//                    break;
//                case 3:
//                    header += "March ";
//                    break;
//                case 4:
//                    header += "April ";
//                    break;
//                case 5:
//                    header += "May ";
//                    break;
//                case 6:
//                    header += "June ";
//                    break;
//                case 7:
//                    header += "July ";
//                    break;
//                case 8:
//                    header += "August ";
//                    break;
//                case 9:
//                    header += "September ";
//                    break;
//                case 10:
//                    header += "October ";
//                    break;
//                case 11:
//                    header += "November ";
//                    break;
//                case 12:
//                    header += "December ";
//                    break;
//            }
//            // Concat current year to header
//            header += year + "";
//
//            // Center header string
//            for (int b = 0; b < 23 - (header.length() / 2); b++) {
//                System.out.print(" ");
//            }
//
//            // Display header and days of the week string
//            System.out.println(header + "\n-----------------------------------------------\n "
//                    + "Sun    Mon    Tue    Wed    Thu    Fri    Sat");
//
//            // Compute day of the week
//            day %= 7;
//            for (int b = 0; b <= day * 7; b++) {
//                System.out.print(" ");
//            }
//
//            // Compute last day of present month
//            int lastDay = 0;
//            if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12)
//                lastDay += 31;
//            else if (month == 4 || month == 6 || month == 9 || month == 11)
//                lastDay += 30;
//            else { // Test for leap year
//                if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))
//                    lastDay += 29;
//                else
//                    lastDay += 28;
//            }
//
//            // Display calendar for current month
//            for (int d = 1; d <= lastDay; d++) {
//                // Add a black space before numbers less than 10
//                if (d < 10)
//                    System.out.print(" ");
//                // Start new line after Saturday
//                if (day % 7 == 6)
//                    System.out.print(d + "\n ");
//                else {
//                    System.out.print(d + "     ");
//                    if (d == lastDay)
//                        System.out.println();
//                }
//                day++;
//
//            }
//            System.out.println();
//        }
        int year = LocalDate.now().getYear();
        int day = LocalDate.of(year, month, 1).getDayOfWeek().getValue();

        String[] monthNames = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };

        System.out.println();
        String header = monthNames[month - 1] + " " + year;

        // Center header string
        int spacesBeforeHeader = (23 - header.length()) / 2;
        System.out.print(" ".repeat(spacesBeforeHeader) + header + "\n-----------------------------------------------\n ");
        System.out.println("Sun    Mon    Tue    Wed    Thu    Fri    Sat");

        // Compute day of the week
        day %= 7;
        System.out.print(" ".repeat(day * 7));

        // Compute last day of present month
        int lastDay;
        if (month == 2) { // February
            lastDay = ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0) ? 29 : 28;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            lastDay = 30;
        } else {
            lastDay = 31;
        }

        // Display calendar for current month
        for (int d = 1; d <= lastDay; d++) {
            System.out.printf("%-6s", d); // Left-align and reserve 6 characters
            if (day % 7 == 6) {
                System.out.println();
            }
            day++;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        showDate(5);
    }

}

