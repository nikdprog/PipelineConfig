package com.app.PipelineConfig.controllers;

import jakarta.servlet.http.HttpSession;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam(name="username") String username,
                        @RequestParam(name="password") String password,
                        HttpSession httpSession) {
        System.out.println(username);
        System.out.println(password);
        return "redirect:/"; // Перенаправление на главную страницу
    }

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal UserDetails userDetails, HttpSession httpSession) {
        String username = userDetails.getUsername();
        httpSession.setAttribute("username", username);
        model.addAttribute("username", username);
        return "index";
    }

    @GetMapping("/indexPage")
    public String home2(Model model, HttpSession httpSession) {
        model.addAttribute("username", httpSession.getAttribute("username"));
        return "indexPage";
    }

}