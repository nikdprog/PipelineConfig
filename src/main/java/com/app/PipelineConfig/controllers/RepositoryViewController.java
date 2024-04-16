package com.app.PipelineConfig.controllers;

import com.app.PipelineConfig.entity.Pipeline;
import com.app.PipelineConfig.entity.RepositoryEntity;
import com.app.PipelineConfig.service.PipelineJPA;
import com.app.PipelineConfig.service.RepositoryJPA;
import jakarta.servlet.http.HttpSession;
import org.kohsuke.github.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.List;

@Controller
public class RepositoryViewController {
    private final String operating_system = "windows-latest";
    private static String repositoryName;
    private RepositoryJPA repositoryJPA;
    private  PipelineJPA pipelineJPA;
    public RepositoryViewController(RepositoryJPA repositoryJPA, PipelineJPA pipelineJPA) {
        this.repositoryJPA = repositoryJPA;
        this.pipelineJPA = pipelineJPA;
    }

    @GetMapping("/repository/{repositoryName}")
    public String viewRepository(@PathVariable String repositoryName, Model model, HttpSession httpSession) throws IOException {
        repositoryName = repositoryName;
        RepositoryEntity repository = repositoryJPA.findByName(repositoryName);
        String name_pipeline;
        Long repository_id = repository.getId(); // +
        System.out.println(repository_id);
        String name = repository.getName();
        String language = repository.getLanguage();
        String language_version = "1.17";
        String jdk_version = "19";
        String trigger1 = "push";
        String trigger2 = "pull_request";
        System.out.println(repository.getLanguage());

        String accessToken = (String) httpSession.getAttribute("accessToken");
        GitHub github = new GitHubBuilder().withOAuthToken(accessToken).build();
        String owner = github.getMyself().getLogin();
         /*
        GHRepository repositoryGitHub = github.getRepository(owner + "/" + repositoryName);
        PagedIterable<GHWorkflowRun> runs = repositoryGitHub.queryWorkflowRuns().list();
        for (GHWorkflowRun run : runs) {
            System.out.println(run.getStatus());
            System.out.println(run.getConclusion());
            System.out.println(run.getCreatedAt());
        }
         */
        GHRepository repositoryGitHub = github.getRepository(owner + "/" + repositoryName);
        PagedIterable<GHWorkflowRun> runs = repositoryGitHub.queryWorkflowRuns().list();
        for (GHWorkflowRun run : runs) {
            System.out.println("-------------------------------------------------------");
            System.out.println("Status: " + run.getStatus());
            System.out.println("Conclusion: " + run.getConclusion());
            System.out.println("Created at: " + run.getCreatedAt());
            System.out.println("Updated at: " + run.getUpdatedAt());
            System.out.println("Logs URL: " + run.getLogsUrl());
            System.out.println("-------------------------------------------------------");
            // Другие методы для получения информации о запущенных пайплайнах
        }
        List<Pipeline> lst = pipelineJPA.findByRepositoryId(repository_id);
        for(Pipeline pipe : lst) {

        }

        // Например, model.addAttribute("repositoryInfo", repositoryInfo);
        model.addAttribute("repository_name", repositoryName);
        //model.addAttribute("repository_language", repositoryLanguage);

        model.addAttribute("pipelines", lst);
        return "repository_view";
    }
}