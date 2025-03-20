package com.effectivemobile.mapper;

import com.effectivemobile.dto.request.CommentRequest;
import com.effectivemobile.dto.response.CommentResponse;
import com.effectivemobile.entity.Comment;
import com.effectivemobile.entity.Task;
import com.effectivemobile.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    CommentResponse toDto(Comment comment);

    @Mapping(target = "author", source = "author")
    @Mapping(target = "task", source = "task")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "time", source = "commentRq.time")
    Comment toEntity(CommentRequest commentRq, User author, Task task);
}
