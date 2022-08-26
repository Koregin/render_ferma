package ru.koregin.fermabackend.repository;

import ru.koregin.fermabackend.model.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findAllByUserId(Long userId);
    Task findByIdAndUserId(Long id, Long userId);
}
