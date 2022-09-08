package ru.koregin.fermabackend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import ru.koregin.fermabackend.model.Task;
import ru.koregin.fermabackend.repository.TaskRepository;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskScheduler taskScheduler;

    @Value("${timezone}")
    private String timezone;

    public TaskService(TaskRepository taskRepository, TaskScheduler taskScheduler) {
        this.taskRepository = taskRepository;
        this.taskScheduler = taskScheduler;
    }

    public List<Task> getAllTasks(Long userId) {
        return taskRepository.findAllByUserId(userId);
    }

    public Task createTask(Task task) {
        int min = 1000 * 60;
        int max = 1000 * 60 * 5;
        int timeForExecute = (int) Math.floor(Math.random() * (max - min + 1) + min);
        log.info("Статус задачи " + task.getName() + " будет изменен через " + timeForExecute / 1000 + " секунд");
        Task savedTask = taskRepository.save(task);
        taskScheduler.schedule(() -> taskStatusUpdate(savedTask),
                new Date(System.currentTimeMillis() + timeForExecute));
        return savedTask;
    }

    /**
     * Update task status to COMPLETED
     *
     * @param task
     */
    private void taskStatusUpdate(Task task) {
        log.info("Обновляю статус задачи " + task.getName() + " на COMPLETE. Время выполнения: "
                + ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        task.setStatus(Task.Status.COMPLETED);
        task.setCompleted(ZonedDateTime.now(ZoneId.of(timezone)).withNano(0));
        taskRepository.save(task);
    }

    public Task getTaskById(Long id, Long userId) {
        log.info("Получение задачи " + id);
        return taskRepository.findByIdAndUserId(id, userId);
    }
}
