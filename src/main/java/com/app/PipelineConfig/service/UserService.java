package com.app.PipelineConfig.service;

import com.app.PipelineConfig.dto.*;
import com.app.PipelineConfig.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService extends UserDetailsService {
    User save(UserRegistrationDto registrationDto);

    Long getCurrentUserId(HttpSession httpSession);

    User getUserById(Long userId);

    UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException;

    List<User> getAll();
}
