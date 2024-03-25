package com.app.PipelineConfig.service;

import com.app.PipelineConfig.entity.Pipeline;
import com.app.PipelineConfig.entity.RepositoryEntity;
import com.app.PipelineConfig.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PipelineService {
    private final PipelineJPA pipelineJPA;


    @Autowired
    public PipelineService(PipelineJPA pipelineJPA) {
        this.pipelineJPA = pipelineJPA;
    }

    //public boolean ExistIs(String name, String url) {
        //repositoryRepository.find
    //    return true;
    //}

    public void save(Pipeline pipeline) {
        pipelineJPA.save(pipeline);
    }


}

