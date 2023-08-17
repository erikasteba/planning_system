package com.example.planning_system.models;
//>:)

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Entity
@Table(name = "\"activities\"")
public class Activities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long user_id;
    private String name;
    @JsonFormat(pattern = "HH:mm a")
    private String startTime;
    private int startMonth;
    private int startDay;
    @JsonFormat(pattern = "HH:mm")
    private String endTime;
    private int endMonth;
    private int endDay;
    private boolean isPublic;

    // I am not sure if I've made it correctly :D
    // @ManyToOne
    // @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    // private User user;
    public Activities() {
    }

    public Activities(String name, String startTime, int startMonth, int startDay,
                      String endTime, int endMonth, int endDay, boolean isPublic) {
        this.name = name;
        this.startTime = startTime;
        this.startMonth = startMonth;
        this.startDay = startDay;
        this.endTime = endTime;
        this.endMonth = endMonth;
        this.endDay = endDay;
        this.isPublic = isPublic;
    }
}
