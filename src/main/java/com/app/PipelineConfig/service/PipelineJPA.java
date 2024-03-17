package com.app.PipelineConfig.service;

import com.app.PipelineConfig.entity.RepositoryEntity;
import com.app.PipelineConfig.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PipelineJPA extends JpaRepository<RepositoryEntity, User> {

}
