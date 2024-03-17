package com.app.PipelineConfig.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class RepositoryViewController {

    @GetMapping("/repository/{repositoryName}")
    public String viewRepository(@PathVariable String repositoryName, Model model) {
        // подготовить метаданные и сформировать пайплайн.
        /*
        name: Build
        on:
        pull_request:
            branches:
                - '*'
        push:
            branches:
                - 'master'
        jobs:
            build:
            runs-on: windows-latest
            steps:
                - uses: actions/checkout@v1
                - name: set up JDK 19
                  uses: actions/setup-java@v1
                  with:
                    java-version: 1.17
                - name: Maven Package
                  run: mvn -B clean package -DskipTests  */
        // рабочий вариант пайплайна
        String pipeline = "name: Build\n" +
                "        on:\n" +
                "        pull_request:\n" +
                "            branches:\n" +
                "                - '*'\n" +
                "        push:\n" +
                "            branches:\n" +
                "                - 'master'\n" +
                "        jobs:\n" +
                "            build:\n" +
                "            runs-on: windows-latest\n" +
                "            steps:\n" +
                "                - uses: actions/checkout@v1\n" +
                "                - name: set up JDK 19\n" +
                "                  uses: actions/setup-java@v1\n" +
                "                  with:\n" +
                "                    java-version: 1.17\n" +
                "                - name: Maven Package\n" +
                "                  run: mvn -B clean package -DskipTests";

        // Например, model.addAttribute("repositoryInfo", repositoryInfo);
        model.addAttribute("repository_name", repositoryName);
        //model.addAttribute("repository_language", repositoryLanguage);
        return "repository_view";
    }

}

