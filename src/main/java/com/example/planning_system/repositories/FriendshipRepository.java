package com.example.planning_system.repositories;

import com.example.planning_system.enums.FriendshipStatus;
import com.example.planning_system.models.Activities;
import com.example.planning_system.models.Friendship;
import com.example.planning_system.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findByUser1OrUser2(User user1, User user2);

    @Query("SELECT f.user2 FROM Friendship f WHERE f.user1 = :user AND f.status = 1")
    List<User> findUser2IdsByUser1IdAndStatus(User user);
    @Query("SELECT f.id FROM Friendship f WHERE (f.user1 = :user1 AND f.user2 = :user2) OR (f.user1 = :user2 AND f.user2 = :user1)")
    Long findIdByUser1AndUser2(@Param("user1") User user1, @Param("user2") User user2);
    @Query("SELECT f.status FROM Friendship f WHERE (f.user1 = :user1 AND f.user2 = :user2) OR (f.user1 = :user2 AND f.user2 = :user1)")
    FriendshipStatus findStatusByUser1AndUser2(@Param("user1") User user1, @Param("user2") User user2);

    boolean existsByUser1AndUser2(User user1, User user2);

    Optional<Friendship> findById(Long id);
    Optional<Friendship> findByUser1AndUser2AndStatus(User user1, User user2, FriendshipStatus status);

    List<Friendship> findByUser2AndStatus(User user, FriendshipStatus pending);

    List<Friendship> findByUser1AndStatus(User user, FriendshipStatus declined);
}
