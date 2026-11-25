package com.taskflow.core.service;

import com.taskflow.core.model.Task;
import com.taskflow.core.model.TaskStatus;
import com.taskflow.core.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Task> findAll() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public List<Task> findByStatus(TaskStatus status) {
        return repository.findByStatusOrderByCreatedAtDesc(status);
    }

    @Transactional(readOnly = true)
    public Task findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + id));
    }

    public Task create(Task task) {
        task.setId(null);
        task.setStatus(TaskStatus.PENDING);
        return repository.save(task);
    }

    public Task update(Long id, Task updated) {
        Task existing = findById(id);
        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        return repository.save(existing);
    }

    public Task toggleStatus(Long id) {
        Task task = findById(id);
        task.setStatus(task.getStatus() == TaskStatus.COMPLETED
                ? TaskStatus.PENDING : TaskStatus.COMPLETED);
        return repository.save(task);
    }

    public void delete(Long id) {
        repository.delete(findById(id));
    }

    @Transactional(readOnly = true)
    public long countAll() { return repository.count(); }

    @Transactional(readOnly = true)
    public long countByStatus(TaskStatus status) {
        return repository.findByStatusOrderByCreatedAtDesc(status).size();
    }
}
