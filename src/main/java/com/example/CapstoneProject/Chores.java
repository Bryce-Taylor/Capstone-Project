package com.example.CapstoneProject;
import javax.persistence.*;
@Entity
@Table(name = "chores")
public class Chores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="chore", nullable = false, length = 100)
    private String chore;

    @Column(name="description", nullable = true, length = 100)
    private String description;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getChore() {
        return chore;
    }

    public void setChore(String chore) {
        this.chore = chore;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
