package com.app.PipelineConfig.controllers;


import com.app.PipelineConfig.entity.Pipeline;
import com.app.PipelineConfig.service.PipelineJPA;
import com.app.PipelineConfig.service.PipelineService;
import com.app.PipelineConfig.service.RepositoryJPA;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/editPipeline")
public class PipelineEditController {

    private PipelineJPA pipelineJPA;
    private RepositoryJPA repositoryJPA;
    @Autowired
    public PipelineEditController(PipelineJPA pipelineJPA, RepositoryJPA repositoryJPA) {
        this.pipelineJPA = pipelineJPA;
        this.repositoryJPA = repositoryJPA;
    }

    @PostMapping
    public ResponseEntity<?> editPipeline(Model model, @RequestParam String repositoryName, @RequestParam String pipelineName,
                                          @RequestParam String trigger, @RequestParam(value="stages[]") String[] stages,
                                          @RequestParam String text_of_pipeline, @RequestParam Long pipelineId, HttpSession httpSession) {
        System.out.println("hello");
        System.out.println(pipelineId);
        System.out.println(repositoryName);
        System.out.println(text_of_pipeline);
        System.out.println(pipelineName);
        System.out.println(trigger);
        for(String stage : stages) {
            System.out.println(stage);
        }
        Optional<Pipeline> pipelineOptional = Optional.ofNullable(pipelineJPA.findById(pipelineId));
        Pipeline pipeline = pipelineOptional.get();
        pipeline.setName(pipelineName);
        pipeline.setText(text_of_pipeline);
        //pipeline.setStages(stages);
        PipelineService pipelineService = new PipelineService(pipelineJPA);

        pipelineService.save(pipeline);

        return ResponseEntity.ok("/repository/{repositoryName}");
    }
}
