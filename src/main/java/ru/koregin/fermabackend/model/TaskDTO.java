package ru.koregin.fermabackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TaskDTO {

    private Long id;
    private String name;
    private Task.Status status;
    private LocalDateTime created;
    private LocalDateTime completed;
}
