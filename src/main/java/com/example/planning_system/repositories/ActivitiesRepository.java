package com.example.planning_system.repositories;

import com.example.planning_system.models.Activities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ActivitiesRepository extends JpaRepository<Activities, Long>{

}
