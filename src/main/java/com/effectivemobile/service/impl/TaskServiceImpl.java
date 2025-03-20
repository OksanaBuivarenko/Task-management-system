package com.effectivemobile.service.impl;

import com.effectivemobile.dto.request.PriorityRequest;
import com.effectivemobile.dto.request.StatusRequest;
import com.effectivemobile.dto.request.TaskRequest;
import com.effectivemobile.dto.request.TaskSearchRequest;
import com.effectivemobile.dto.response.MessageResponse;
import com.effectivemobile.dto.response.PageResponse;
import com.effectivemobile.dto.response.TaskResponse;
import com.effectivemobile.entity.User;
import com.effectivemobile.entity.Task;
import com.effectivemobile.entity.Task_;
import com.effectivemobile.entity.User_;
import com.effectivemobile.exception.ObjectAlreadyExistsException;
import com.effectivemobile.mapper.TaskMapper;
import com.effectivemobile.repository.TaskRepository;
import com.effectivemobile.repository.specification.TaskSpecification;
import com.effectivemobile.service.UserService;
import com.effectivemobile.service.TaskService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    private final PageableService pageableService;

    private final UserService userService;

    @Override
    public PageResponse<List<TaskResponse>> getAllTasks(Integer offset, Integer limit) {
        return PageResponse.<List<TaskResponse>>builder()
                .offset(offset)
                .limit(limit)
                .data(taskRepository.findAll(pageableService.getPageable(offset, limit)).stream()
                        .map((taskMapper::toDto))
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public PageResponse<List<TaskResponse>> getAllTasksByPerformer(Long id, Integer offset, Integer limit, String email) {
        List<Task> tasks = taskRepository.findAllByPerformerId(id, pageableService.getPageable(offset, limit));
        if (!userService.isAccessRightsUser(email, tasks.get(0).getPerformer())) {
            throw new AccessDeniedException("User does not have access rights.");
        }
        return PageResponse.<List<TaskResponse>>builder()
                .offset(offset)
                .limit(limit)
                .data(tasks.stream()
                        .map((taskMapper::toDto))
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Task with id %d not found", id)));
    }

    @Override
    public TaskResponse getTaskResponse(Long id, String email) {
        Task task = getTaskById(id);
        if (!userService.isAccessRightsUser(email, task.getPerformer())) {
            throw new AccessDeniedException("User does not have access rights.");
        }
        return taskMapper.toDto(task);
    }

    @Override
    public PageResponse<List<TaskResponse>> searchTask(TaskSearchRequest searchRq, Integer offset, Integer limit) {
        List<TaskResponse> resultList = taskRepository.findAll(
                        Specification.where(TaskSpecification.idEquals(searchRq.getId()))
                                .and(TaskSpecification.like(Task_.title, searchRq.getTitle()))
                                .and(TaskSpecification.like(Task_.author, User_.userName,
                                        searchRq.getAuthorName()))
                                .and(TaskSpecification.like(Task_.performer, User_.userName,
                                        searchRq.getPerformerName())),
                        pageableService.getPageable(offset, limit))
                .stream().map(taskMapper::toDto)
                .collect(Collectors.toList());

        return PageResponse.<List<TaskResponse>>builder()
                .offset(offset)
                .limit(limit)
                .data(resultList)
                .build();
    }

    @Override
    @Transactional
    public TaskResponse createTask(TaskRequest taskRq) {
        if (taskRepository.existsByTitle(taskRq.getTitle())) {
            throw new ObjectAlreadyExistsException(String.format("Task with title %s already exists",
                    taskRq.getTitle()));
        }
        User author = userService.getUserById(taskRq.getAuthorId());
        User performer = userService.getUserById(taskRq.getPerformerId());
        Task task = taskMapper.toEntity(taskRq, author, performer);
        Task savedTask = taskRepository.save(task);
        return taskMapper.toDto(savedTask);
    }

    @Override
    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest taskRq) {
        Task task = getTaskById(id);
        User author = userService.getUserById(taskRq.getAuthorId());
        User performer = userService.getUserById(taskRq.getPerformerId());
        Task updatedTask = taskMapper.toEntity(taskRq, author, performer);
        updatedTask.setId(task.getId());
        return taskMapper.toDto(taskRepository.save(updatedTask));
    }

    @Override
    @Transactional
    public TaskResponse changeTaskStatus(Long id, StatusRequest statusRq, String email) {
        Task task = getTaskById(id);
        if (!userService.isAccessRightsUser(email, task.getPerformer())) {
            throw new AccessDeniedException("User does not have access rights.");
        }
        task.setStatus(statusRq.getStatus());
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    public TaskResponse changeTaskPriority(Long id, PriorityRequest priorityRq) {
        Task task = getTaskById(id);
        task.setPriority(priorityRq.getPriority());
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    @Transactional
    public MessageResponse deleteTask(Long id) {
        taskRepository.delete(getTaskById(id));
        String deleteMessage = String.format("Task with id %d delete success", id);
        log.info(deleteMessage);
        return MessageResponse.builder().message(deleteMessage).build();
    }
}
