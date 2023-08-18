package com.example.planning_system.models;
//>:)

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@ToString
@Entity
@Table(name = "\"activities\"")
public class Activities {


    public Long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    private LocalDate startDate;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    private LocalDate endDate;

    public boolean isPublic() {
        return isPublic;
    }

    private boolean isPublic;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    public Activities() {
    }

    public Activities(String name, String startTime, String endTime, boolean isPublic) {
        LocalDateTime start = LocalDateTime.parse(startTime);
        LocalDateTime end = LocalDateTime.parse(endTime);
        this.name = name;
        this.startTime = start.toLocalTime();
        this.startDate = start.toLocalDate();
        this.endTime = end.toLocalTime();
        this.endDate = end.toLocalDate();
        this.isPublic = isPublic;
    }
}
