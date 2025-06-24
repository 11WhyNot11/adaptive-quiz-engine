package com.arthur.adaptivequizengine.user.service;

import com.arthur.adaptivequizengine.user.dto.UserRequestDto;
import com.arthur.adaptivequizengine.user.dto.UserResponseDto;
import com.arthur.adaptivequizengine.user.entity.User;

import java.util.List;

public interface UserService {
    UserResponseDto save(UserRequestDto dto, User currentUser);
    UserResponseDto findById(Long id, User currentUser);
    List<UserResponseDto> findAll(User currentUser);
    void deleteById(Long id, User currentUser);
}
