package com.app.PipelineConfig.service;

import com.app.PipelineConfig.entity.Pipeline;
import com.app.PipelineConfig.entity.RepositoryEntity;
import com.app.PipelineConfig.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PipelineJPA extends JpaRepository<Pipeline, User> {
    List<Pipeline> findAll();
    List<Pipeline> findByRepositoryId(Long id);
}
