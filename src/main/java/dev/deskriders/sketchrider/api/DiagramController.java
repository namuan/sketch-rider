package dev.deskriders.sketchrider.api;

import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.View;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.UUID;

@Controller
@Secured(SecurityRule.IS_ANONYMOUS)
public class DiagramController {

    @Get(value = "/diagrams/new")
    public HttpResponse createNewDiagram() throws URISyntaxException {
        String diagramId = UUID.randomUUID().toString();
        return HttpResponse.temporaryRedirect(new URI("/diagrams/" + diagramId));
    }

    @View(value = "diagrams/edit")
    @Get(value = "/diagrams/{diagramId}", produces = MediaType.TEXT_HTML)
    public Map<String, Object> diagram(String diagramId) {
        return CollectionUtils.mapOf("diagramId", diagramId);
    }
}
