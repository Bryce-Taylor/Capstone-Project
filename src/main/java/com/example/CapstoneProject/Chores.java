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
}
