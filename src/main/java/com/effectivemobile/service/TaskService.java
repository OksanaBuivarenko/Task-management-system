package com.effectivemobile.service;

import com.effectivemobile.dto.request.PriorityRequest;
import com.effectivemobile.dto.request.StatusRequest;
import com.effectivemobile.dto.request.TaskRequest;
import com.effectivemobile.dto.request.TaskSearchRequest;
import com.effectivemobile.dto.response.MessageResponse;
import com.effectivemobile.dto.response.PageResponse;
import com.effectivemobile.dto.response.TaskResponse;
import com.effectivemobile.entity.Task;

import java.util.List;

public interface TaskService {

    PageResponse<List<TaskResponse>> getAllTasks(Integer offset, Integer limit);

    Task getTaskById(Long id);

    TaskResponse getTaskResponse(Long id, String email);

    PageResponse<List<TaskResponse>> searchTask(TaskSearchRequest searchRq, Integer offset, Integer limit);

    TaskResponse createTask(TaskRequest taskRq);

    TaskResponse updateTask(Long id, TaskRequest taskRq);

    MessageResponse deleteTask(Long id);

    PageResponse<List<TaskResponse>> getAllTasksByPerformer(Long id, Integer offset, Integer limit, String username);

    TaskResponse changeTaskStatus(Long id, StatusRequest statusRq, String email);

    TaskResponse changeTaskPriority(Long id, PriorityRequest priorityRq);
}
