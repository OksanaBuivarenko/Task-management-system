package com.effectivemobile.mapper;

import com.effectivemobile.dto.request.CommentRequest;
import com.effectivemobile.dto.response.CommentResponse;
import com.effectivemobile.entity.Comment;
import com.effectivemobile.entity.Task;
import com.effectivemobile.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CommentMapperTest {

    private CommentMapper commentMapper;

    @BeforeEach
    public void setUp() {
        commentMapper = Mappers.getMapper(CommentMapper.class);
    }

    @Test
    void toDto() {
        User author = new User();
        author.setId(1L);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setAuthor(author);
        comment.setText("This is a test comment.");
        comment.setTime(LocalDateTime.now());

        CommentResponse commentResponse = commentMapper.toDto(comment);

        assertEquals(comment.getId(), commentResponse.getId());
        assertEquals(comment.getText(), commentResponse.getText());
        assertEquals(comment.getTime(), commentResponse.getTime());
        assertEquals(author.getId(), commentResponse.getAuthor().getId());
    }

    @Test
    void toNullDto() {
        CommentResponse commentResponse = commentMapper.toDto(null);
        assertNull(commentResponse);
    }

    @Test
    void toEntity() {
        CommentRequest commentRequest = CommentRequest.builder()
                        .text("Test comment")
                        .time(LocalDateTime.now())
                        .authorId(1L)
                        .build();

        User author = new User();
        author.setId(1L);

        Task task = new Task();
        task.setId(1L);

        Comment comment = commentMapper.toEntity(commentRequest, author, task);

        assertEquals(commentRequest.getText(), comment.getText());
        assertEquals(commentRequest.getTime(), comment.getTime());
        assertEquals(task, comment.getTask());
    }

    @Test
    void toNullEntity() {
        Comment comment = commentMapper.toEntity(null, null, null);
        assertNull(comment);
    }
}