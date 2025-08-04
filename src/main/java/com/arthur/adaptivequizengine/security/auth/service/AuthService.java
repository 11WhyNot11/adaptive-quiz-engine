package com.arthur.adaptivequizengine.security.auth.service;

import com.arthur.adaptivequizengine.security.auth.dto.AuthResponseDto;
import com.arthur.adaptivequizengine.security.auth.dto.LoginRequestDto;
import com.arthur.adaptivequizengine.security.auth.dto.RegisterRequestDto;

public interface AuthService {
    AuthResponseDto register(RegisterRequestDto request);
    AuthResponseDto login (LoginRequestDto request);
}
