package dev.deskriders.sketchrider.api;

import dev.deskriders.sketchrider.renderer.PlantUmlRenderer;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

@Slf4j
@Controller
@Secured(SecurityRule.IS_ANONYMOUS)
public class RenderController {

    public static final String DEFAULT_DIAGRAM_TYPE = "puml";

    private PlantUmlRenderer plantUmlRenderer;

    public RenderController(PlantUmlRenderer plantUmlRenderer) {
        this.plantUmlRenderer = plantUmlRenderer;
    }

    @Get(value = "/render/{diagramSource}{?dt}")
    public HttpResponse renderDiagram(@PathVariable String diagramSource, @QueryValue Optional<String> dt) throws URISyntaxException, IOException {
        String diagramType = dt.orElse(DEFAULT_DIAGRAM_TYPE);
        byte[] diagramBytes = plantUmlRenderer.renderDiagram(diagramSource);
        return HttpResponse.ok(diagramBytes)
                .header("Content-Type", MediaType.IMAGE_JPEG);
    }

}
