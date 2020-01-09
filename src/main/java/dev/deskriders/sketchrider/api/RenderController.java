package dev.deskriders.sketchrider.api;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Optional;

@Slf4j
@Controller
@Secured(SecurityRule.IS_ANONYMOUS)
public class RenderController {

    public static final String DEFAULT_DIAGRAM_TYPE = "puml";

    @Get(value = "/render/{diagramSource}{?dt}")
    public HttpResponse renderDiagram(@PathVariable String diagramSource, @QueryValue Optional<String> dt) throws URISyntaxException, IOException {
        String diagramType = dt.orElse(DEFAULT_DIAGRAM_TYPE);
        log.info("Rendering diagram for source " + diagramSource + " with type: " + diagramType);
        File imageFile = new File("src/main/resources/public/static/images/stock-plantuml.jpg");

//        URL resource = getClass().getResource("/logo.svg");
//        byte[] bytes = Files.readAllBytes(Path.of(resource.toURI()));

        byte[] bytes = Files.readAllBytes(imageFile.toPath());
        return HttpResponse.ok(bytes).header("Content-Type", MediaType.IMAGE_JPEG);
    }

}
