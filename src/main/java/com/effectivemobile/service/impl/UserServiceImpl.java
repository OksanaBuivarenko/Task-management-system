package com.effectivemobile.service.impl;

import com.effectivemobile.dto.request.UserRequest;
import com.effectivemobile.dto.response.UserResponse;
import com.effectivemobile.entity.User;
import com.effectivemobile.enumeration.RoleType;
import com.effectivemobile.exception.ObjectAlreadyExistsException;
import com.effectivemobile.mapper.UserMapper;
import com.effectivemobile.repository.UserRepository;
import com.effectivemobile.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse createUser(UserRequest userRq) {
        if (userRepository.existsByUserName(userRq.getUserName())) {
            throw new ObjectAlreadyExistsException("User name already exists!");
        }
        if (userRepository.existsByEmail(userRq.getEmail())) {
            throw new ObjectAlreadyExistsException("Email already exists!");
        }
        User user = userMapper.toEntity(userRq, passwordEncoder.encode(userRq.getPassword()));
        return userMapper.toDto(userRepository.save(user));

    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with id " + id + " not found."));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found."));
    }

    @Override
    public boolean isAccessRightsUser(String email, User performer) {
        User user = getUserByEmail(email);
        return user.getRoles().contains(RoleType.ROLE_ADMIN) || user.equals(performer);
    }

}
