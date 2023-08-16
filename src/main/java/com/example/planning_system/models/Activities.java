package com.example.planning_system.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalTime;
@Entity
@Table(name = "\"activities\"")
public class Activities {

    @Id
    private Long id;

    private Long user_id;

    private String name;

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
