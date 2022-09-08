package ru.koregin.fermabackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.koregin.fermabackend.model.Task;
import ru.koregin.fermabackend.model.User;
import ru.koregin.fermabackend.repository.UserRepository;
import ru.koregin.fermabackend.service.TaskService;

import java.security.Principal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/task")
public class TaskController {

    @Value("${timezone}")
    private String timezone;
    private final UserRepository userRepository;
    private final TaskService taskService;

    public TaskController(UserRepository userRepository, TaskService taskService) {
        this.userRepository = userRepository;
        this.taskService = taskService;
    }

    @GetMapping()
    List<Task> getAllTasks(Principal principal) {
        log.info("Получение всех задач для пользователя " + principal.getName());
        Optional<User> authUser = userRepository.findByUsername(principal.getName());
        if (authUser.isPresent()) {
            return taskService.getAllTasks(authUser.get().getId());
        }
        return new ArrayList<>();
    }

    @GetMapping("/{id}")
    Task getTaskById(Principal principal, @PathVariable("id") long id) {
        log.info("Поиск задачи ID=" + id + " для пользователя " + principal.getName());
        Optional<User> authUser = userRepository.findByUsername(principal.getName());
        if (authUser.isPresent()) {
            return taskService.getTaskById(id, authUser.get().getId());
        }
        return null;
    }

    @PostMapping()
    ResponseEntity<Task> createTask(Principal principal, @RequestBody Task task) {
        String name = task.getName();
        Optional<User> authUser = userRepository.findByUsername(principal.getName());
        if (authUser.isPresent()) {
            log.info("Создание новой задачи " + name + " для пользователя " + authUser.get().getUsername());
            Task newTask = new Task();
            newTask.setName(name);
            newTask.setStatus(Task.Status.RENDERING);
            newTask.setCreated(ZonedDateTime.now(ZoneId.of(timezone)).withNano(0));
            log.info("Созданная задача: " + newTask);
            newTask.setUserId(authUser.get().getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(newTask));
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
