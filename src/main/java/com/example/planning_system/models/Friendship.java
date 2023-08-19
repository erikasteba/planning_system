package com.example.planning_system.models;

import com.example.planning_system.enums.FriendshipStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@Entity
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user1_id")
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user2_id")
    private User user2;

    private FriendshipStatus status;

    private LocalDateTime createdAt;



}

