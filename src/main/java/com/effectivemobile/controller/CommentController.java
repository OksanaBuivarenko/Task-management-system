package com.effectivemobile.controller;

import com.effectivemobile.dto.request.CommentRequest;
import com.effectivemobile.dto.response.CommentResponse;
import com.effectivemobile.security.AppUserDetails;
import com.effectivemobile.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "Bearer Authentication")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    @Operation(description = "Create comment by task id")
    @PostMapping("/task/{id}")
    public CommentResponse createComment(@RequestBody @Valid CommentRequest commentRq, @PathVariable Long id,
                                         @AuthenticationPrincipal AppUserDetails userDetails) {
        return commentService.createComment(commentRq, id, userDetails.getUsername());
    }
}
