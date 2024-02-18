package com.app.PipelineConfig.controllers;

import com.app.PipelineConfig.entity.Repository;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final GitHub gitHub;
    @Value("${github.oauth.token}")
    private String token;

    @Autowired
    public HomeController(GitHub gitHub) {
        this.gitHub = gitHub;
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/indexGithub")
    public String home_alt() {
        return "indexGithub";
    }

    @GetMapping("/repositories")
    public String getRepositories(Model model) throws IOException {
        GitHub github = GitHub.connectUsingOAuth(token);

        // Получаем текущий аккаунт
        String username = github.getMyself().getLogin();

        // Получаем все репозитории пользователя
        List<GHRepository> ghRepositories = github.getMyself().listRepositories().toList();
        String languages = ghRepositories.get(0).getLanguage();
        // Преобразуем GHRepository в Repository
        List<Repository> repositories = ghRepositories.stream()
                .map(repo -> new Repository(repo.getName(), repo.getHtmlUrl().toString()))
                .collect(Collectors.toList());

        model.addAttribute("repositories", repositories);
        model.addAttribute("language", languages);
        return "repositories";
    }
}