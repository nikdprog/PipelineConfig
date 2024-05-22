package com.app.PipelineConfig.controllers;

import com.app.PipelineConfig.entity.Pipeline;
import com.app.PipelineConfig.entity.RepositoryEntity;
import com.app.PipelineConfig.service.PipelineJPA;
import com.app.PipelineConfig.service.RepositoryJPA;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import jakarta.servlet.http.HttpSession;
import org.kohsuke.github.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

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

        List<Pipeline> lst = pipelineJPA.findByRepositoryId(repository_id);

        GHRepository repositoryGitHub = github.getRepository(owner + "/" + repositoryName);
        PagedIterable<GHWorkflowRun> runs = repositoryGitHub.queryWorkflowRuns().list();

        int k = 0;
        for (Pipeline pipeline : lst) {
            // Получаем имя пайплайна из списка
            String pipelineName = pipeline.getName();

            for (GHWorkflowRun run : runs) {
                //run.getRepository().getName()
                System.out.println("-------------------------------------------------------");
                System.out.println("name: " + run.getName());
                System.out.println("Status: " + run.getStatus());
                System.out.println("Conclusion: " + run.getConclusion());
                System.out.println("Created at: " + run.getCreatedAt());
                System.out.println("Updated at: " + run.getUpdatedAt());


                //System.out.println("----" + run.listAllJobs().toList().get(0).getName());
                //System.out.println("Logs URL: " + run.getLogsUrl());
                System.out.println("-------------------------------------------------------");
                //if(lst.get(k).getName() == run.getName()) {
                //    lst.get(k).setStatus(run.getStatus().toString());
                //    System.out.println("status: " + lst.get(k).getStatus());
                //    k++;
                if(Objects.equals(pipelineName, run.getName())) {
                    pipeline.setStatus(run.getStatus().toString());
                }
            }
        }


            // Другие методы для получения информации о запущенных пайплайнах
            //System.out.println("jobsLogs: " + run.getJobsUrl());
            /*
            URL url = (URL) run.getJobsUrl();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Теперь переменная response содержит JSON-данные
                // Распарсим JSON-ответ
                Gson gson = new Gson();
                JsonElement jsonElement = gson.fromJson(response.toString(), JsonElement.class);
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                // Извлечем значение поля status
                String status = jsonObject.get("Status").getAsString();
                System.out.println("Status: " + status);
            } else {
                System.out.println("Failed to get jobs data. Response code: " + connection.getResponseCode());
            }
            connection.disconnect();


        }
        */


        // Например, model.addAttribute("repositoryInfo", repositoryInfo);
        model.addAttribute("repository_name", repositoryName);
        //model.addAttribute("repository_language", repositoryLanguage);

        model.addAttribute("pipelines", lst);
        return "repository_view";
    }
}