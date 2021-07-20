package com.example.CapstoneProject;

import javax.persistence.*;
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

    @Column(name="manager", nullable = false, length = 100)
    private String manager;

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
}
