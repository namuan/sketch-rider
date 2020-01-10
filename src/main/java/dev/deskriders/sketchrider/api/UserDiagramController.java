package dev.deskriders.sketchrider.api;

import dev.deskriders.sketchrider.api.requests.CreateUserDiagramRequest;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.security.Security;
import java.util.Map;

@Slf4j
@Controller
public class UserDiagramController {

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post(value = "/user-diagrams", consumes = MediaType.APPLICATION_JSON)
    public HttpResponse<Map<String, String>> createUserDiagram(
            @Valid CreateUserDiagramRequest createUserDiagramRequest,
            Authentication authentication
    ) {
        if (authentication == null) {
            return HttpResponse.unauthorized();
        }
        // call service class
        log.info("Creating user " + authentication.getAttributes().get("id") + " diagram " + createUserDiagramRequest);
        return HttpResponse.created(CollectionUtils.mapOf("id", "diagram-id"));
    }
}
