package com.arthur.adaptivequizengine.user.service;

import com.arthur.adaptivequizengine.common.access.AccessValidator;
import com.arthur.adaptivequizengine.exception.handler.UserNotFoundException;
import com.arthur.adaptivequizengine.user.dto.UserRequestDto;
import com.arthur.adaptivequizengine.user.dto.UserResponseDto;
import com.arthur.adaptivequizengine.user.entity.Role;
import com.arthur.adaptivequizengine.user.entity.User;
import com.arthur.adaptivequizengine.user.mapper.UserMapper;
import com.arthur.adaptivequizengine.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AccessValidator accessValidator;

    @Override
    public UserResponseDto save(UserRequestDto dto, User currentUser) {
        accessValidator.validateIsAdmin(currentUser);

        var entity = userMapper.toEntity(dto);
        entity.setRole(Role.USER);

        var savedEntity = userRepository.save(entity);

        return userMapper.toDto(savedEntity);
    }

    @Override
    public UserResponseDto createAdmin(UserRequestDto dto, User currentUser) {
        accessValidator.validateIsAdmin(currentUser);

        var entity = userMapper.toEntity(dto);
        entity.setRole(Role.ADMIN);

        var savedEntity = userRepository.save(entity);

        return userMapper.toDto(savedEntity);
    }

    @Override
    public UserResponseDto findById(Long id, User currentUser) {
        accessValidator.validateIsAdmin(currentUser);

        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return userMapper.toDto(user);
    }

    @Override
    public List<UserResponseDto> findAll(User currentUser) {
        accessValidator.validateIsAdmin(currentUser);

        var users = userRepository.findAll();

        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }


    @Override
    public void deleteById(Long id, User currentUser) {
        accessValidator.validateCanDeleteUser(currentUser, id);

        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userRepository.delete(user);
    }
}
