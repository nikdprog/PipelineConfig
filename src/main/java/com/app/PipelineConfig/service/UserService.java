package com.app.PipelineConfig.service;

import com.app.PipelineConfig.dto.*;
import com.app.PipelineConfig.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User save(UserRegistrationDto registrationDto);
    List<User> getAll();
}
