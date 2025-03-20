package com.effectivemobile.service.impl;

import com.effectivemobile.dto.request.CommentRequest;
import com.effectivemobile.dto.response.CommentResponse;
import com.effectivemobile.entity.Comment;
import com.effectivemobile.entity.Task;
import com.effectivemobile.entity.User;
import com.effectivemobile.mapper.CommentMapper;
import com.effectivemobile.repository.CommentRepository;
import com.effectivemobile.service.CommentService;
import com.effectivemobile.service.TaskService;
import com.effectivemobile.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    private final UserService userService;

    private final TaskService taskService;

    @Override
    @Transactional
    public CommentResponse createComment(CommentRequest commentRq, Long taskId, String email) {
        Task task = taskService.getTaskById(taskId);
        if (!userService.isAccessRightsUser(email, task.getPerformer())) {
            throw new AccessDeniedException("User does not have access rights.");
        }
        User author = userService.getUserById(commentRq.getAuthorId());
        Comment comment = commentMapper.toEntity(commentRq, author, task);
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toDto(savedComment);
    }

}
