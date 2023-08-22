package com.example.planning_system;

import com.example.planning_system.models.Activities;
import com.example.planning_system.models.User;
import com.example.planning_system.repositories.ActivitiesRepository;
import com.example.planning_system.repositories.UserRepository;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

//@DataJpaTest
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@SpringBootTest
@ActiveProfiles("test")
public class ActivitiesRepositoryTests {

    @Autowired
    private ActivitiesRepository activitiesRepository;
    @Autowired
    private UserRepository userRepository;


    @Test
    public void FindActivityNamesByUserId_ReturnActivityNames(){

        User user = new User();
        user = userRepository.save(user);

        Activities activity1 = new Activities();
        activity1.setName("Art class");
        activity1.setUser(user);
        activitiesRepository.save(activity1);

        Activities activity2 = new Activities();
        activity2.setName("Homework");
        activity2.setUser(user);
        activitiesRepository.save(activity2);

        List<String> activityNames = activitiesRepository.findActivityNamesByUserId(user.getId());
        assertThat(activityNames).containsExactly("Art class", "Homework");
    }



    @Test
    public void FindByUser_ReturnActivityList() {

        User user = new User();
        userRepository.save(user);

        Activities activity1 = new Activities();
        activity1.setName("Cinema");
        activity1.setUser(user);
        activitiesRepository.save(activity1);

        Activities activity2 = new Activities();
        activity2.setName("Dinner");
        activity2.setUser(user);
        activitiesRepository.save(activity2);

        List<Activities> userActivities = activitiesRepository.findByUser(user);
        assertThat(userActivities).hasSize(2);
        assertThat(userActivities).extracting(Activities::getName).containsExactly("Cinema", "Dinner");
    }



    @Test
    public void FindByUserId_ReturnActivityList() {

        User user = new User();
        user.setId(1L);
        userRepository.save(user);

        Activities activity1 = new Activities();
        activity1.setName("Hobby");
        activity1.setUser(user);
        activitiesRepository.save(activity1);

        Activities activity2 = new Activities();
        activity2.setName("Tennis");
        activity2.setUser(user);
        activitiesRepository.save(activity2);

        List<Activities> userActivities = activitiesRepository.findByUserId(1L);
        assertThat(userActivities).hasSize(2);
        assertThat(userActivities).extracting(Activities::getName).containsExactly("Hobby", "Tennis");
    }


    @Test
    public void optionalFindById() {

        Activities activity = new Activities();
        activity.setName("Gym");
        activitiesRepository.save(activity);

        Optional<Activities> optionalActivity = activitiesRepository.findById(activity.getId());

        assertThat(optionalActivity.get().getName()).isEqualTo("Gym");
    }






}
