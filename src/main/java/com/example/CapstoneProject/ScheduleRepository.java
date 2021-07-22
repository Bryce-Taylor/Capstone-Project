package com.example.CapstoneProject;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface ScheduleRepository extends CrudRepository<Schedule, Long> {
    public ArrayList<Schedule> findByWeek(int week);

    public ArrayList<Schedule> findUserByUsername(String username);

    public Optional<Schedule> findChoreById(Long id);
}
