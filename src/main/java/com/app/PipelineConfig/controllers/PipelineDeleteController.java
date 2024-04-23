package com.app.PipelineConfig.controllers;

import com.app.PipelineConfig.entity.Pipeline;
import com.app.PipelineConfig.service.PipelineJPA;
import jakarta.servlet.http.HttpSession;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/pipelinesDelete")
public class PipelineDeleteController {
    private PipelineJPA pipelineJPA;
    private GitHub github;
    public PipelineDeleteController(PipelineJPA pipelineJPA, GitHub github) {
        this.pipelineJPA = pipelineJPA;
        this.github = github;
    }

    @PostMapping
    public ResponseEntity<String> deletePipeline(@RequestParam Long pipelineId, @RequestParam String repositoryName, HttpSession httpSession) throws IOException {
        System.out.println("deleting");
        Pipeline pipeline = pipelineJPA.findById(pipelineId);
        pipelineJPA.delete(pipeline);
        String accessToken = (String) httpSession.getAttribute("accessToken");
        GitHub github = new GitHubBuilder().withOAuthToken(accessToken).build();
        String owner = github.getMyself().getLogin();
        GHRepository repository = github.getRepository(owner + "/" + repositoryName);
        String filePath = ".github/workflows/" + pipeline.getName() + ".yml";
        GHContent fileToDelete = repository.getFileContent(filePath);
        fileToDelete.delete("deleting yml. pipeline file");
        System.out.println(owner + "/" + repositoryName);
        return ResponseEntity.ok("pipeline delete successful");
    }

}
