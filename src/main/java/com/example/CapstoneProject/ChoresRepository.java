package com.example.CapstoneProject;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface ChoresRepository extends CrudRepository<Chores, Long> {
}