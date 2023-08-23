package com.example.planning_system.repositories;

import com.example.planning_system.models.Activities;
import com.example.planning_system.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ActivitiesRepository extends JpaRepository<Activities, Long>{
    List<Activities> findByUser(User user);


    @Query("SELECT a.name FROM Activities a WHERE a.user.id = :userId")
    List<String> findActivityNamesByUserId(@Param("userId") Long userId);

    List<Activities> findByUserId(Long user);

    Optional<Activities> findById(Long id);

    List<Activities> findByUserAndStartDateEquals(User user, LocalDate date);


}
