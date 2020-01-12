package dev.deskriders.sketchrider.api;

import dev.deskriders.sketchrider.api.requests.CreateUserDiagramRequest;
import dev.deskriders.sketchrider.repository.UserDiagramRepository;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.View;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Controller
public class UserDiagramController {

    private UserDiagramRepository userDiagramRepository;

    public UserDiagramController(UserDiagramRepository userDiagramRepository) {
        this.userDiagramRepository = userDiagramRepository;
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post(value = "/user-diagrams", consumes = MediaType.APPLICATION_JSON)
    public HttpResponse<Map<String, String>> createUserDiagram(
            @Valid CreateUserDiagramRequest createUserDiagramRequest,
            Authentication authentication
    ) {
        String ownerId = (String) authentication.getAttributes().get("id");
        userDiagramRepository.saveUserDiagram(
                createUserDiagramRequest.getDocId(),
                ownerId,
                createUserDiagramRequest.getDiagramCode()
        );

        return HttpResponse.created(CollectionUtils.mapOf("id", createUserDiagramRequest.getDocId()));
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Delete(value = "/user-diagrams/{diagramId}")
    public HttpResponse deleteUserDiagram(@PathVariable String diagramId, Authentication authentication) {
        String ownerId = (String) authentication.getAttributes().get("id");
        userDiagramRepository.deleteUserDiagram(ownerId, diagramId);
        return HttpResponse.ok();
    }
}
