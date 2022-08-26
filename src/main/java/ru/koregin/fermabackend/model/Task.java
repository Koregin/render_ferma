package ru.koregin.fermabackend.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private LocalDateTime created;
    private LocalDateTime completed;

    public enum Status {
        RENDERING, COMPLETE
    }
}
