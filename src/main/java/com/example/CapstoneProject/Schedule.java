package com.example.CapstoneProject;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Column(name="day")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate day;

    @Column(name="user", nullable = false, length = 100)
    private String user;

    @Column(name = "username", nullable = false, length = 20)
    private String username;

    @Column(name="manager", nullable = false, length = 100)
    private String manager;

    @Column(name = "man_username", nullable = false, length = 20)
    private String man_username;

    @Column(name="user_checked", nullable = false, length = 100)
    private boolean user_checked;

    @Column(name="man_checked", nullable = false, length = 100)
    private boolean man_checked;

    @Column(name="start_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate start_date;

    @Column(name="end_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate end_date;

    @Column(name="user_checkoff_time")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime user_checkoff_time;

    @Column(name="man_checkoff_time")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime man_checkoff_time;

    @Column(name="missed", nullable = false, length = 100)
    private boolean missed;

    @Column(name="completed", nullable = false, length = 100)
    private boolean completed;

    @Column(name="user_email", nullable = false, length = 100)
    private String user_email;

    @Column(name="man_email", nullable = false, length = 100)
    private String man_email;

    @Column(name="notified", nullable = false, length = 1)
    private boolean notified;

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

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
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

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }

    public LocalDateTime getUser_checkoff_time() {
        return user_checkoff_time;
    }

    public void setUser_checkoff_time(LocalDateTime user_checkoff_time) {
        this.user_checkoff_time = user_checkoff_time;
    }

    public LocalDateTime getMan_checkoff_time() {
        return man_checkoff_time;
    }

    public void setMan_checkoff_time(LocalDateTime man_checkoff_time) {
        this.man_checkoff_time = man_checkoff_time;
    }

    public String getMan_username() {
        return man_username;
    }

    public void setMan_username(String man_username) {
        this.man_username = man_username;
    }

    public boolean isMissed() {
        return missed;
    }

    public void setMissed(boolean missed) {
        this.missed = missed;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getMan_email() {
        return man_email;
    }

    public void setMan_email(String man_email) {
        this.man_email = man_email;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

}
