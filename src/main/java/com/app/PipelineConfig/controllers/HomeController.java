package com.app.PipelineConfig.controllers;

import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    private final GitHub gitHub;

    @Autowired
    public HomeController(GitHub gitHub, OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
        this.gitHub = gitHub;
        this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
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

}