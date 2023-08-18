package com.example.planning_system.repositories;

import com.example.planning_system.enums.FriendshipStatus;
import com.example.planning_system.models.Friendship;
import com.example.planning_system.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findByUser1OrUser2(User user1, User user2);
    boolean existsByUser1AndUser2(User user1, User user2);
    Optional<Friendship> findByUser1AndUser2AndStatus(User user1, User user2, FriendshipStatus status);

}
