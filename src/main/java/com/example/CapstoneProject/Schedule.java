package com.example.CapstoneProject;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="week", nullable = false, length = 100)
    private int week;

    @Column(name="chore", nullable = false, length = 100)
    private String chore;

    @Column(name="day", nullable = false, length = 3)
    private int day;

    @Column(name="user", nullable = false, length = 100)
    private String user;

    @Column(name = "username", nullable = false, length = 20)
    private String username;

    @Column(name="manager", nullable = false, length = 100)
    private String manager;

    @Column(name="user_checked", nullable = false, length = 100)
    private boolean user_checked;

    @Column(name="man_checked", nullable = false, length = 100)
    private boolean man_checked;

    @Column(name="date_made")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate date_made;

    @Column(name="end_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate end_date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getChore() {
        return chore;
    }

    public void setChore(String chore) {
        this.chore = chore;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public boolean isUser_checked() {
        return user_checked;
    }

    public boolean setUser_checked(boolean user_checked) {

        return  this.user_checked = user_checked;
    }

    public boolean isMan_checked() {
        return man_checked;
    }

    public void setMan_checked(boolean man_checked) {
        this.man_checked = man_checked;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getDate_made() {
        return date_made;
    }

    public void setDate_made(LocalDate date_made) {
        this.date_made = date_made;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }
}
