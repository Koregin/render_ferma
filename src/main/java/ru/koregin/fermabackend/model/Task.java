package ru.koregin.fermabackend.model;

import lombok.Data;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private Long userId;
    private String name;
    private Status status;
    private ZonedDateTime created;
    private ZonedDateTime completed;

    public enum Status {
        RENDERING, COMPLETED
    }
}
