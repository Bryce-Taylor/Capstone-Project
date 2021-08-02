package com.example.CapstoneProject;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface ScheduleRepository extends CrudRepository<Schedule, Long> {
    public ArrayList<Schedule> findByWeek(int week);


    public ArrayList<Schedule> findUserByUsername(String username);

    @Query("SELECT u FROM Schedule u WHERE u.man_username = ?1")
    public ArrayList<Schedule> findUserByman_username(String man_username);

    public String findByUsername(String username);

    public Optional<Schedule> findChoreById(Long id);
    @Query("SELECT schedule FROM Schedule schedule WHERE schedule.day = ?1")
    public List<Schedule> findByDay(LocalDate day);

    public void deleteAll();
}
