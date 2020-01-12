package dev.deskriders.sketchrider.api;

import dev.deskriders.sketchrider.model.UserDiagramEntity;
import dev.deskriders.sketchrider.repository.UserDiagramRepository;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.View;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@Secured(SecurityRule.IS_ANONYMOUS)
public class DiagramController {

    private UserDiagramRepository userDiagramRepository;

    public DiagramController(UserDiagramRepository userDiagramRepository) {
        this.userDiagramRepository = userDiagramRepository;
    }

    @Get(value = "/diagrams/new")
    public HttpResponse createNewDiagram() throws URISyntaxException {
        String diagramId = UUID.randomUUID().toString();
        return HttpResponse.temporaryRedirect(new URI("/diagrams/" + diagramId));
    }

    @View(value = "diagrams/edit")
    @Get(value = "/diagrams/{diagramId}", produces = MediaType.TEXT_HTML)
    public Map<String, Object> editDiagram(String diagramId) {
        return CollectionUtils.mapOf("diagramId", diagramId);
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @View(value = "diagrams/list")
    @Get(value = "/diagrams", produces = MediaType.TEXT_HTML)
    public Map<String, Object> listDiagrams(Authentication authentication) {
        String ownerId = (String) authentication.getAttributes().get("id");
        List<UserDiagramEntity> userDiagramEntities = this.userDiagramRepository.selectUserDiagrams(ownerId);
        return CollectionUtils.mapOf("diagrams", userDiagramEntities);
    }
}
