package com.app.PipelineConfig.service;

import com.app.PipelineConfig.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.app.PipelineConfig.entity.RepositoryEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryJPA extends JpaRepository<RepositoryEntity, User> {
    boolean existsByNameAndUrl(String name, String url);
    RepositoryEntity findByName(String name);
    Long findIdByName(String name);
}

