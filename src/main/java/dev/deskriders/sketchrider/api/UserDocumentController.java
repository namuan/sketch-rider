package dev.deskriders.sketchrider.api;

import dev.deskriders.sketchrider.api.requests.CreateUserDocumentRequest;
import dev.deskriders.sketchrider.repository.UserDocumentRepository;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Controller
public class UserDocumentController {

    private UserDocumentRepository userDocumentRepository;

    public UserDocumentController(UserDocumentRepository userDocumentRepository) {
        this.userDocumentRepository = userDocumentRepository;
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Get(value = "/user-documents/generate")
    public HttpResponse createData(Authentication authentication) {
        String ownerId = (String) authentication.getAttributes().get("id");
        for(int i=0;i < 1000; i++) {
            LocalDate localDate = LocalDate.now().minusDays(i);
            String docId = UUID.randomUUID().toString();
            CreateUserDocumentRequest request = new CreateUserDocumentRequest();
            request.setCode("Hello World " + i);
            request.setId(localDate + " - " + docId + " ");
            request.setTitle("Title " + localDate + "-" + i + " ");
            request.setType("puml");
            userDocumentRepository.saveUserDocument(ownerId, request);
        }
        return HttpResponse.ok();
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post(value = "/user-documents", consumes = MediaType.APPLICATION_JSON)
    public HttpResponse<Map<String, String>> createUserDocument(
            @Valid CreateUserDocumentRequest createUserDocumentRequest,
            Authentication authentication
    ) {
        String ownerId = (String) authentication.getAttributes().get("id");
        userDocumentRepository.saveUserDocument(
                ownerId,
                createUserDocumentRequest
        );

        return HttpResponse.created(CollectionUtils.mapOf("id", createUserDocumentRequest.getId()));
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Delete(value = "/user-documents/{documentId}")
    public HttpResponse deleteUserDocument(@PathVariable String documentId, Authentication authentication) {
        String ownerId = (String) authentication.getAttributes().get("id");
        userDocumentRepository.deleteUserDocument(ownerId, documentId);
        return HttpResponse.ok();
    }
}
