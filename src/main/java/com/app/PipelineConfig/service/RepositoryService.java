package com.app.PipelineConfig.service;

import com.app.PipelineConfig.entity.RepositoryEntity;
import com.app.PipelineConfig.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepositoryService {
    private final RepositoryJPA repositoryRepository;

    @Autowired
    public RepositoryService(RepositoryJPA repositoryRepository) {
        this.repositoryRepository = repositoryRepository;
    }

    public boolean ExistIs(String name, String url) {
        //repositoryRepository.find
        return true;
    }

    public void saveRepository(RepositoryEntity repositoryEntity) {
        repositoryRepository.save(repositoryEntity);
    }
}

