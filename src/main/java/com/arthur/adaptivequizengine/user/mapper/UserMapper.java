package com.arthur.adaptivequizengine.user.mapper;

import com.arthur.adaptivequizengine.user.dto.UserRequestDto;
import com.arthur.adaptivequizengine.user.dto.UserResponseDto;
import com.arthur.adaptivequizengine.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequestDto dto);

    UserResponseDto toDto(User user);
}
