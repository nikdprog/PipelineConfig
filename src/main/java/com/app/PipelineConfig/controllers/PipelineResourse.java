package com.app.PipelineConfig.controllers;


import com.app.PipelineConfig.entity.Pipeline;
import com.app.PipelineConfig.entity.RepositoryEntity;
import com.app.PipelineConfig.service.PipelineJPA;
import com.app.PipelineConfig.service.PipelineService;
import com.app.PipelineConfig.service.RepositoryJPA;
import jakarta.servlet.http.HttpSession;
import org.kohsuke.github.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class PipelineResourse {

    private final PipelineService pipelineService;
    private RepositoryJPA repositoryJPA;
    private PipelineJPA pipelineJPA;

    @Autowired
    public PipelineResourse(PipelineService pipelineService, RepositoryJPA repositoryJPA, PipelineJPA pipelineJPA) {
        this.pipelineService = pipelineService;
        this.repositoryJPA = repositoryJPA;
        this.pipelineJPA = pipelineJPA;
    }

    @PostMapping
    public ResponseEntity<?> savePipeline(Model model, @RequestParam String repositoryName, @RequestParam String pipelineName, @RequestParam String trigger, @RequestParam(value="stages[]") String[] stages, HttpSession httpSession) throws IOException {
            System.out.println("this is savePipeline controller");
            for(Object stage : stages) {
                System.out.println(stage);
            }
            RepositoryEntity repository = repositoryJPA.findByName(repositoryName);
            System.out.println(repository.getName());
            String accessToken = (String) httpSession.getAttribute("accessToken");
            GitHub github = new GitHubBuilder().withOAuthToken(accessToken).build();
            String owner = github.getMyself().getLogin();
            GHRepository repositoryGitHub = github.getRepository(owner + "/" + repositoryName);
            String language_version = "1.17";
            String build_system = "maven";
            // добавляем название пайплайна, ветку, триггеры, среду выполнения оставляем по умолчанию windows
            // осталось добавить язык программирования и его версию, среду сборки и версию jdk(пока не делать).
            String filePath = ".github/workflows/" + pipelineName + ".yml";
            String text_pipeline = "";
            String headSha = repositoryGitHub.getRef("heads/master").getObject().getSha();
            if(stages.length == 1) {
                System.out.println("build");
                text_pipeline = "name: "+ pipelineName + "\n" +
                        "\n" +
                        "on:\n" +
                        "  pull_request:\n" +
                        "    branches:\n" +
                        "      - '*'\n" +
                        "  push:\n" +
                        "    branches:\n" +
                        "      - 'master'\n" +
                        "\n" +
                        "jobs:\n" +
                        "  build:\n" +
                        "    runs-on: windows-latest\n" +
                        "    steps:\n" +
                        "      - uses: actions/checkout@v1\n" +
                        "      - name: set up JDK 19\n" +
                        "        uses: actions/setup-java@v1\n" +
                        "        with:\n" +
                        "          java-version: 1.17\n" +
                        "      - name: Maven Package\n" +
                        "        run: mvn -B clean package -DskipTests";
            } else if(stages.length == 2) {
                System.out.println("build and test");
            } else {
                System.out.println("build, test and deploy");
                // делаем программное создание пайплайнов
                //String headSha = repositoryGitHub.getRef("heads/master").getObject().getSha();
                text_pipeline = "name: " + pipelineName + "\n" +
                        "\n" +
                        "on:\n" +
                        "  push:\n" +
                        "    branches:\n" +
                        "      - 'master'\n" +
                        "\n" +
                        "jobs:\n" +
                        "  build:\n" +
                        "    runs-on: windows-latest\n" +
                        "    steps:\n" +
                        "      - uses: actions/checkout@v1\n" +
                        "      - name: set up JDK 19\n" +
                        "        uses: actions/setup-java@v1\n" +
                        "        with:\n" +
                        "          java-version: 1.17\n" +
                        "      - name: Maven Package\n" +
                        "        run: mvn -B clean package -DskipTests\n" +
                        "  test:\n" +
                        "    runs-on: windows-latest\n" +
                        "    needs: build\n" +
                        "    steps:\n" +
                        "      - name: checkout repo\n" +
                        "        uses: actions/checkout@v3\n" +
                        "      - name: set up JDK 19\n" +
                        "        uses: actions/setup-java@v1\n" +
                        "        with:\n" +
                        "          java-version: '17'\n" +
                        "      - name: Maven Package\n" +
                        "        run: mvn -B clean package -DskipTests\n" +
                        "  prepare-environment:\n" +
                        "    runs-on: self-hosted\n" +
                        "    needs: test\n" +
                        "    steps:\n" +
                        "      - name: checkout repo\n" +
                        "        uses: actions/checkout@v3\n" +
                        "      - name: Stop and remove containers, networks\n" +
                        "        run: docker-compose down\n" +
                        "      - name: Remove unused data\n" +
                        "        run: docker system prune -a -f\n" +
                        "  deploy:\n" +
                        "    runs-on: self-hosted\n" +
                        "    needs: prepare-environment\n" +
                        "    steps:\n" +
                        "      - name: checkout repo\n" +
                        "        uses: actions/checkout@v3\n" +
                        "      - name: docker-compose\n" +
                        "        run: docker-compose up -d";
            }
            try {
                repositoryGitHub.getDirectoryContent(".github/workflows");
                // Директория существует, ничего не делаем
            } catch (GHFileNotFoundException e) {
                // Директория не существует, создаем ее
                repositoryGitHub.createContent()
                        .branch("master")
                        .message("Create .github/workflows folder")
                        .path(".github/workflows/dummy-file.txt")
                        .content("")
                        .commit();
                System.out.println(".github/workflows folder created successfully.");
            }

            try {
                repositoryGitHub.getDirectoryContent(".github/workflows/" + pipelineName + ".yml");
            } catch (GHFileNotFoundException e) {
                repositoryGitHub.createContent()
                        .content(text_pipeline)
                        .message("Add workflow file")
                        .path(filePath)
                        .sha(headSha)
                        .commit();
            }
            Pipeline pipeline = new Pipeline();

            pipeline.setName(pipelineName);
            pipeline.setLanguageVersion(language_version);
            pipeline.setBuildSystem(build_system);
            pipeline.setText(text_pipeline);
            pipeline.setRepository(repository);
            pipeline.setStages(stages);
            PipelineService pipelineService = new PipelineService(pipelineJPA);

            pipelineService.save(pipeline);
            model.addAttribute("pipelineName", pipelineName);
            model.addAttribute("build_system", build_system);
            model.addAttribute("stages", stages);

        return ResponseEntity.ok("/repository/{repositoryName}");
    }

    @GetMapping
    public ResponseEntity<?> getInfoPipeline(Model model ,@RequestParam Long pipelineId) {
        System.out.println("this is edit pipeline");
        Optional<Pipeline> pipelineOptional = Optional.ofNullable(pipelineJPA.findById(pipelineId));

        if (pipelineOptional.isPresent()) {
            Pipeline pipeline = pipelineOptional.get();
            return ResponseEntity.ok(pipeline);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
