package com.app.PipelineConfig.repository;

import com.app.PipelineConfig.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
