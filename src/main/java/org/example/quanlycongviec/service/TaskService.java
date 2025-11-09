package org.example.quanlycongviec.service;

import lombok.RequiredArgsConstructor;
import org.example.quanlycongviec.dto.TaskRequest;
import org.example.quanlycongviec.dto.TaskResponse;
import org.example.quanlycongviec.entity.Task;
import org.example.quanlycongviec.entity.TaskStatus;
import org.example.quanlycongviec.entity.User;
import org.example.quanlycongviec.repository.TaskRepository;
import org.example.quanlycongviec.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    private TaskResponse mapToTaskResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus().name());
        response.setDueDate(task.getDueDate());
        response.setUserId(task.getUser().getId());
        return response;
    }

    public TaskResponse createTask(TaskRequest request) {
        User currentUser = getCurrentUser();

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());
        if (request.getStatus() != null) {
            task.setStatus(TaskStatus.valueOf(request.getStatus().toUpperCase()));
        }
        task.setUser(currentUser);
        Task savedTask = taskRepository.save(task);
        return mapToTaskResponse(savedTask);
    }

    public List<TaskResponse> getAllTasksForCurrentUser() {
        User currentUser = getCurrentUser();
        List<Task> tasks = taskRepository.findByUserId(currentUser.getId());
        return tasks.stream()
                .map(this::mapToTaskResponse)
                .collect(Collectors.toList());
    }

    public TaskResponse getTaskById(Long taskId) {
        User currentUser = getCurrentUser();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        if (!task.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to view this task");
        }
        return mapToTaskResponse(task);
    }

    public TaskResponse updateTask(Long taskId, TaskRequest request) {
        User currentUser = getCurrentUser();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        if (!task.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to update this task");
        }
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());
        if (request.getStatus() != null) {
            task.setStatus(TaskStatus.valueOf(request.getStatus().toUpperCase()));
        }
        Task updatedTask = taskRepository.save(task);
        return mapToTaskResponse(updatedTask);
    }

    public void deleteTask(Long taskId) {
        User currentUser = getCurrentUser();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        if (!task.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to delete this task");
        }
        taskRepository.delete(task);
    }
}