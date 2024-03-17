package com.app.PipelineConfig.controllers;

import com.app.PipelineConfig.service.impl.UserServiceImpl;
import com.app.PipelineConfig.userDetails.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
//@SessionAttributes("username")
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

    //@RequestMapping(value="/login", method=RequestMethod.POST)
    @PostMapping("/login")
    public String login(@RequestParam(name="username") String username,
                        @RequestParam(name="password") String password,
                        HttpSession httpSession) {
        // Обработка логина
        System.out.println(username);
        System.out.println(password);
        //httpSession.setAttribute("username", username);
        return "redirect:/"; // Перенаправление на главную страницу
    }

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal UserDetails userDetails, HttpSession httpSession) {
        //model.addAttribute("username", principal);
        //System.out.println(httpSession.getAttribute("username"));
        String username = userDetails.getUsername();

        //System.out.println(username);
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //UserDetails userDetails1;
        //userDetails1 = (UserDetails) authentication.getDetails();
        //System.out.println(userDetails1.getUsername());
        //String email = ((CustomUserDetails) userDetails1).getEmail();
        //System.out.println(userDetails1.getAuthorities());
        //System.out.println(email);
        //httpSession.setAttribute("userDetails", userDetails1.getUsername());
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