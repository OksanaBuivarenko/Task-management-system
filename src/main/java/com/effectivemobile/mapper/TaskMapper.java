package com.effectivemobile.mapper;

import com.effectivemobile.dto.request.TaskRequest;
import com.effectivemobile.dto.response.TaskResponse;
import com.effectivemobile.entity.User;
import com.effectivemobile.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    TaskResponse toDto(Task task);

    @Mapping(target = "author", source = "author")
    @Mapping(target = "performer", source = "performer")
    @Mapping(target = "id", ignore = true)
    Task toEntity(TaskRequest taskRq, User author, User performer);
}
