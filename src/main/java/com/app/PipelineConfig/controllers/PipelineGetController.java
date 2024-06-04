package com.app.PipelineConfig.controllers;

import com.app.PipelineConfig.entity.Pipeline;
import com.app.PipelineConfig.service.PipelineJPA;
import jakarta.servlet.http.HttpSession;
import org.kohsuke.github.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/getPipeline")
public class PipelineGetController {
    private PipelineJPA pipelineJPA;
    private Map<String, Date> pipelines_updates = new HashMap<>();
    @Autowired
    public PipelineGetController(PipelineJPA pipelineJPA) {
        this.pipelineJPA = pipelineJPA;

    }
    @GetMapping
    public ResponseEntity<?> getPipeline(@RequestParam Long pipelineId, @RequestParam String repositoryName, HttpSession httpSession) throws IOException {
        Optional<Pipeline> pipelineOptional = Optional.ofNullable(pipelineJPA.findById(pipelineId));
        if (pipelineOptional.isPresent()) {
            String accessToken = (String) httpSession.getAttribute("accessToken");
            GitHub github = new GitHubBuilder().withOAuthToken(accessToken).build();
            String owner = github.getMyself().getLogin();
            Pipeline pipeline = pipelineOptional.get();
            GHRepository repositoryGitHub = github.getRepository(owner + "/" + repositoryName);
            PagedIterable<GHWorkflowRun> runs = repositoryGitHub.queryWorkflowRuns().list();

            Date latestUpdate = null; // Переменная для хранения времени последнего обновления
            GHWorkflowRun latestRun = null; // Переменная для хранения информации о последнем обновленном workflow

            for(GHWorkflowRun ghWorkflowRun : runs) {
                if(Objects.equals(pipeline.getName(), ghWorkflowRun.getName())) {
                    // Проверяем, является ли это время обновления наиболее поздним
                    if (latestUpdate == null || ghWorkflowRun.getUpdatedAt().after(latestUpdate)) {
                        latestUpdate = ghWorkflowRun.getUpdatedAt();
                        latestRun = ghWorkflowRun;
                    }

                }
            }

            // Если найден последний обновленный workflow, обновляем статус пайплайна
            if (latestRun != null) {
                pipelines_updates.put(pipeline.getName(), latestUpdate); // Обновляем время последнего обновления в карте
                pipeline.setStatus(latestRun.getStatus().toString()); // Устанавливаем статус пайплайна
                System.out.println("Updated pipeline status: " + pipeline.getName() + " - " + pipeline.getStatus());
                // Получение этапов и их статусов

                String[] stages = pipeline.getStages();
                for(String str : stages) {
                    System.out.println(str);
                }
                int k = 0;
                Map<String, String> stageStatuses = new LinkedHashMap<>();
                for (GHWorkflowJob job : latestRun.listJobs()) {
                    if(!Objects.equals(job.getName(), "prepare-environment")) {
                        //stages[k] = job.getStatus().toString();
                        //pipeline.getStageStatuses().put(stages[k], job.getStatus().toString());
                        stageStatuses.put(job.getName(), job.getStatus().toString());
                        System.out.println(job.getName() + ": " + job.getStatus().toString());
                        k++;
                    }
                }
                k = 0;
                pipeline.setStageStatuses(stageStatuses);
                //pipeline.setStages(stages); // Устанавливаем этапы пайплайна
            } else {
                System.out.println("No matching workflow found for pipeline: " + pipeline.getName());
            }

            return ResponseEntity.ok(pipeline);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
