package com.example.planning_system.repositories;
import com.example.planning_system.models.Activities;
import com.example.planning_system.models.Notes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotesRepository extends JpaRepository<Notes, Long> {
    List<Notes> findByUserId(Long user);
    Optional<Notes> findById(Long id);

}
