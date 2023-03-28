package com.cires.usersgenerator.repository;

import com.cires.usersgenerator.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("select u from User u where u.email = ?1 or u.userName = ?1")
    Optional<User> findByEmailOrUsername(String usernameOrEmail);
}
