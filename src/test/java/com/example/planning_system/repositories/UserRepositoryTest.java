package com.example.planning_system.repositories;

import com.example.planning_system.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByEmail_Success() {
        User user = new User("Arthur", "123@gmail.com", "123", "321");
        userRepository.save(user);

        User foundUser = userRepository.findByEmail("123@gmail.com");

        assertNotNull(foundUser);
        assertEquals("123", foundUser.getName());
        assertEquals("321", foundUser.getPassword());
    }

    @Test
    public void testExistsByUsername_Exists() {
        User user = new User("Arthur", "alice@example.com", "123", "123");
        userRepository.save(user);

        boolean exists = userRepository.existsByUsername("Arthur");

        assertTrue(exists);
    }

    @Test
    public void testExistsByUsername_NotExists() {
        boolean exists = userRepository.existsByUsername("NonExistentUser");

        assertFalse(exists);
    }

}