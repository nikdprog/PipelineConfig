package com.app.PipelineConfig.controllers;


import com.app.PipelineConfig.entity.Pipeline;
import com.app.PipelineConfig.entity.RepositoryEntity;
import com.app.PipelineConfig.service.PipelineJPA;
import com.app.PipelineConfig.service.PipelineService;
import com.app.PipelineConfig.service.RepositoryJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<?> savePipeline(Model model, @RequestParam String repositoryName, @RequestParam String pipelineName, @RequestParam String trigger, @RequestParam(value="stages[]") String[] stages) {
            System.out.println("this is savePipeline controller");

            for(Object stage : stages) {
                System.out.println(stage);
            }

            RepositoryEntity repository = repositoryJPA.findByName(repositoryName);
            System.out.println(repository.getName());

            Long repository_id = repository.getId(); // +
            String language_version = "1.17";
            String build_system = "maven";
            String text_pipeline = "name: Build\n" +
                    "        on:\n" +
                    "        pull_request:\n" +
                    "            branches:\n" +
                    "                - '*'\nм" +
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
}
