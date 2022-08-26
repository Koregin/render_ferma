package ru.koregin.fermabackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.koregin.fermabackend.model.Task;
import ru.koregin.fermabackend.model.User;
import ru.koregin.fermabackend.repository.UserRepository;
import ru.koregin.fermabackend.service.TaskService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/task")
public class TaskController {

    private final UserRepository userRepository;
    private final TaskService taskService;

    public TaskController(UserRepository userRepository, TaskService taskService) {
        this.userRepository = userRepository;
        this.taskService = taskService;
    }

    @GetMapping()
    @ResponseBody
    List<Task> getAllTasks(Principal principal) {
        System.out.println(principal.getName());
        Optional<User> authUser = userRepository.findByUsername(principal.getName());
        if (authUser.isPresent()) {
            return taskService.getAllTasks(authUser.get().getId());
        }
        return new ArrayList<>();
    }

    @GetMapping("/{id}")
    @ResponseBody
    Task getTaskById(Principal principal, @PathVariable("id") long id) {
        System.out.println(principal.getName());
        Optional<User> authUser = userRepository.findByUsername(principal.getName());
        if (authUser.isPresent()) {
            return taskService.getTaskById(id, authUser.get().getId());
        }
        return null;
    }

    @PostMapping()
    @ResponseBody
    ResponseEntity<Task> createTask(Principal principal, @RequestBody Map<String, String> payload) {
        String name = payload.get("name");
        log.info("Создание новой задачи " + name + " для пользователя " + principal.getName());
        Task newTask = new Task();
        newTask.setName(name);
        newTask.setStatus(Task.Status.RENDERING);
        newTask.setCreated(LocalDateTime.now().withNano(0));
        log.info("Созданная задача: " + newTask);
        Optional<User> authUser = userRepository.findByUsername(principal.getName());
        authUser.ifPresent(user -> newTask.setUserId(user.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(newTask));
    }
}
