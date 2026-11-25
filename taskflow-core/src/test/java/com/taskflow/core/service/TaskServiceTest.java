package com.taskflow.core.service;

import com.taskflow.core.model.Task;
import com.taskflow.core.model.TaskStatus;
import com.taskflow.core.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock TaskRepository repository;
    @InjectMocks TaskService service;

    @Test
    void createTaskSetsPendingStatus() {
        Task task = new Task("Prepare release", "Package the app");
        when(repository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task saved = service.create(task);

        assertEquals(TaskStatus.PENDING, saved.getStatus());
        assertEquals("Prepare release", saved.getTitle());
        verify(repository).save(task);
    }

    @Test
    void toggleStatusChangesPendingToCompleted() {
        Task task = new Task("Deploy", "Deploy to EC2");
        task.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(task));
        when(repository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task updated = service.toggleStatus(1L);

        assertEquals(TaskStatus.COMPLETED, updated.getStatus());
    }

    @Test
    void missingTaskThrowsHelpfulException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.findById(99L));
    }
}
