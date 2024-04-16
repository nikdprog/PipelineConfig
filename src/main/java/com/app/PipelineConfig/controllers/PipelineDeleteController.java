package com.app.PipelineConfig.controllers;

import com.app.PipelineConfig.entity.Pipeline;
import com.app.PipelineConfig.service.PipelineJPA;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/pipelines")
public class PipelineDeleteController {
    private PipelineJPA pipelineJPA;

    public PipelineDeleteController(PipelineJPA pipelineJPA) {
        this.pipelineJPA = pipelineJPA;
    }

    public ResponseEntity<String> deletePipeline(@PathVariable Long pipelineID) {
        System.out.println("deleting");
        Pipeline pipeline = pipelineJPA.findById(pipelineID);
        pipelineJPA.delete(pipeline);
        return ResponseEntity.ok("pipeline delete successful");
    }
}
