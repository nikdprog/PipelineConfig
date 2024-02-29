package com.app.PipelineConfig.controllers;

import com.app.PipelineConfig.entity.Repository;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class RepositoriesController {

    private OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    private final GitHub github;

    public RepositoriesController(GitHub github, OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
        this.github = github;
        this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
    }

    @GetMapping("/repositories")
    public String getRepositories(Model model, @AuthenticationPrincipal OAuth2User oauth2User, OAuth2AuthenticationToken oAuth2AuthenticationToken) throws IOException {
        String accessToken = getAccess_token(oAuth2AuthenticationToken, "github");
        System.out.println(accessToken);
        GitHub github = GitHub.connectUsingOAuth(accessToken);
        // Получаем текущий аккаунт
        String username = github.getMyself().getLogin();

        // Получаем все репозитории пользователя
        List<GHRepository> ghRepositories = github.getMyself().listRepositories().toList();
        String languages = ghRepositories.get(0).getLanguage();
        // получить список зависимостей и используемых технологий
        // Преобразуем GHRepository в Repository
        List<Repository> repositories = ghRepositories.stream()
                .map(repo -> new Repository(repo.getName(), repo.getHtmlUrl().toString(), repo.getLanguage()))
                .collect(Collectors.toList());

        model.addAttribute("repositories", repositories);
        model.addAttribute("language", languages);
        return "repositories";
    }

    // getting access token for GitHub API
    public String getAccess_token(OAuth2AuthenticationToken oAuth2AuthenticationToken, String clientRegistrationId) {
        OAuth2AuthorizedClient client = oAuth2AuthorizedClientService.loadAuthorizedClient(clientRegistrationId,
                oAuth2AuthenticationToken.getName());
        if(client != null) {
            OAuth2AccessToken accessToken = client.getAccessToken();
            if (accessToken != null) {
                return accessToken.getTokenValue();
            }
        }
        return "";
    }
}
