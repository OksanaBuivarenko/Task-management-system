package com.effectivemobile.mapper;

import com.effectivemobile.dto.request.UserRequest;
import com.effectivemobile.dto.response.UserResponse;
import com.effectivemobile.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserResponse toDto(User user);

    @Mapping(target = "userName", source = "userRq.userName")
    @Mapping(target = "email", source = "userRq.email")
    @Mapping(target = "password", source = "password")
    User toEntity(UserRequest userRq, String password);
}
