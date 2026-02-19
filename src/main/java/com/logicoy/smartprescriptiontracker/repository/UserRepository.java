package com.logicoy.smartprescriptiontracker.repository;

import com.logicoy.smartprescriptiontracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository to fetch users by username.
 */
public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
