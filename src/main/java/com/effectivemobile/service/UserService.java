package com.effectivemobile.service;

import com.effectivemobile.dto.request.UserRequest;
import com.effectivemobile.dto.response.UserResponse;
import com.effectivemobile.entity.User;

public interface UserService {

    UserResponse createUser(UserRequest createUserRq);

    User getUserById(Long userId);

    boolean isAccessRightsUser(String email, User performer);
}
