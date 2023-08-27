package com.example.planning_system.repositories;

import com.example.planning_system.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

//repository for image
public interface ImageRepository extends JpaRepository<Image, Long> {

}
