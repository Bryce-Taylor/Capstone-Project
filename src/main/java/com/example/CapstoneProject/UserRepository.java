package com.example.CapstoneProject;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface UserRepository extends CrudRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.username = ?1")
    User findByUsername(String username);
    @Query("SELECT i FROM User i WHERE i.id = ?1")
    Optional<User> findById(Long id);
}
