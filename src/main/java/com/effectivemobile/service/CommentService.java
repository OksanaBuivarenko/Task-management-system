package com.effectivemobile.service;

import com.effectivemobile.dto.request.CommentRequest;
import com.effectivemobile.dto.response.CommentResponse;

public interface CommentService {

    CommentResponse createComment(CommentRequest commentRq, Long id, String email);
}
