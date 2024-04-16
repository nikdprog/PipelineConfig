package com.app.PipelineConfig.controllers;

import com.app.PipelineConfig.entity.RepositoryEntity;
import com.app.PipelineConfig.service.RepositoryJPA;
import com.app.PipelineConfig.service.RepositoryService;
import com.app.PipelineConfig.service.UserService;
import jakarta.servlet.http.HttpSession;
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


    private UserService userService;

    private RepositoryJPA repositoryJPA;

    private OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    private final GitHub github;

    public RepositoriesController(RepositoryJPA repositoryJPA ,GitHub github, OAuth2AuthorizedClientService oAuth2AuthorizedClientService, UserService userService) {
        this.github = github;
        this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
        this.userService = userService;
        this.repositoryJPA = repositoryJPA;
    }


    @GetMapping("/repositories_connect")
    public String pageWithoutRepositories(Model model, HttpSession httpSession) {
        model.addAttribute("username", httpSession.getAttribute("username"));
        return "repositories_connect";
    }


    @GetMapping("/repositories")
    public String getRepositories(Model model, @AuthenticationPrincipal OAuth2User oauth2User, OAuth2AuthenticationToken oAuth2AuthenticationToken, HttpSession httpSession) throws IOException {
        System.out.println(oAuth2AuthenticationToken.getAuthorities().toString());
        String accessToken = getAccess_token(oAuth2AuthenticationToken, "github");

        httpSession.setAttribute("accessToken", accessToken);
        System.out.println(accessToken);
        GitHub github = GitHub.connectUsingOAuth(accessToken);
        // Получаем текущий аккаунт
        String username = github.getMyself().getLogin();
        Long userID = userService.getCurrentUserId(httpSession);
        System.out.println(userID);
        // Получаем все репозитории пользователя
        List<GHRepository> ghRepositories = github.getMyself().listRepositories().toList();

        String languages = ghRepositories.get(0).getLanguage();

        // добавляем в базу данных
        for(GHRepository ghRepository : ghRepositories) {
            String name = ghRepository.getName();
            String url = ghRepository.getHtmlUrl().toString();
            //System.out.println(repositoryJPA.existsByNameAndUrl(name, url));
            if(!repositoryJPA.existsByNameAndUrl(name, url)) {
                String language_ = ghRepository.getLanguage();
                System.out.println(name);
                System.out.println(language_);
                System.out.println(url);
                RepositoryEntity repository = new RepositoryEntity();
                repository.setName(name);
                repository.setLanguage(language_);
                repository.setUrl(url);
                repository.setUser(userService.getUserById(userID));
                RepositoryService repositoryService = new RepositoryService(repositoryJPA);
                repositoryService.saveRepository(repository);
            }
        }
        // получить список зависимостей и используемых технологий
        // Преобразуем GHRepository в Repository
        List<RepositoryEntity> repositories = ghRepositories.stream()
                .map(repo -> new RepositoryEntity(repo.getName(), repo.getHtmlUrl().toString(), repo.getLanguage()))
                .collect(Collectors.toList());

        model.addAttribute("repositories", repositories);
        model.addAttribute("language", languages);
        model.addAttribute("username", httpSession.getAttribute("username"));
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
