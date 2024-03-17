package com.app.PipelineConfig.service.impl;

import com.app.PipelineConfig.dto.UserRegistrationDto;
import com.app.PipelineConfig.entity.Role;
import com.app.PipelineConfig.entity.User;
import com.app.PipelineConfig.repository.UserRepository;
import com.app.PipelineConfig.service.UserService;
import com.app.PipelineConfig.userDetails.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static String email;
    private static UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        super();
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static String getEmail() { return email; }
    @Override
    public User save(UserRegistrationDto registrationDto) {

        User user = new User(registrationDto.getFirstName(),
                registrationDto.getLastName(),
                registrationDto.getEmail(),
                passwordEncoder.encode(registrationDto
                        .getPassword()),
                Arrays.asList(new Role("ROLE_ADMIN")));

        return userRepository.save(user);
    }

    @Override
    public Long getCurrentUserId(HttpSession httpSession) {
        UserDetails userDetails = (UserDetails) httpSession.getAttribute("userDetails");
        // Получаем имя пользователя (в данном случае, email)
        //String username = userDetails.getUsername();
        //CustomUserDetails customUserDetails = (CustomUserDetails) httpSession.getAttribute("userDetails");
        //System.out.println(customUserDetails.getEmail());
        //System.out.println(username);
        // Находим пользователя в базе данных по его имени
        User user = userRepository.findByEmail(email);
        // Проверяем, найден ли пользователь
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Возвращаем идентификатор пользователя
        return user.getId();
    }

    @Override
    public User getUserById(Long userId) {
        // Проверяем, передан ли идентификатор пользователя
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        // Находим пользователя в базе данных по его идентификатору
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username);
        email = username;
        if (user == null) {
            throw new UsernameNotFoundException
                    ("Invalid username or password.");
        }
        //httpSession.setAttribute("userID", user.getId());
        return new org.springframework.security
                .core.userdetails.User(user.getFirstName(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority>
    mapRolesToAuthorities(Collection<Role> roles) {

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority
                        (role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getAll() {

        return userRepository.findAll();
    }
}
