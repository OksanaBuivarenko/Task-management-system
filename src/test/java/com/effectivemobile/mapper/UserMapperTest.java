package com.effectivemobile.mapper;

import com.effectivemobile.dto.request.UserRequest;
import com.effectivemobile.dto.response.UserResponse;
import com.effectivemobile.entity.User;
import com.effectivemobile.enumeration.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    void toDto() {
        User user = new User();
        user.setId(1L);
        user.setUserName("TestUser");

        UserResponse userResponse = userMapper.toDto(user);

        assertEquals(user.getId(), userResponse.getId());
        assertEquals(user.getUserName(), userResponse.getUserName());
    }

    @Test
    void toNullDto() {
        UserResponse userResponse = userMapper.toDto(null);
        assertNull(userResponse);
    }

    @Test
    void toEntity() {
        Set<RoleType> roles = new HashSet<>();
        roles.add(RoleType.ROLE_USER);
        UserRequest userRequest = UserRequest.builder()
                .userName("TestUser")
                .roles(roles)
                .email("testuser@mail.ru")
                .build();

        User user = userMapper.toEntity(userRequest, "securePassword");

        assertEquals(userRequest.getUserName(), user.getUserName());
        assertEquals(userRequest.getEmail(), user.getEmail());
        assertEquals("securePassword", user.getPassword());
        assertEquals(roles, user.getRoles());
    }

    @Test
    void toNullEntity() {
        User user = userMapper.toEntity(null, null);
        assertNull(user);
    }
}