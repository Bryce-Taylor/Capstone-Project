package com.example.CapstoneProject;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface UserRepository extends CrudRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.username = ?1")
    User findByUsername(String username);
    public Optional<User> findById(Long id);

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    public User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.reset_password_token = ?1")
    public User findByPasswordResetToken(String reset_password_token);
}
