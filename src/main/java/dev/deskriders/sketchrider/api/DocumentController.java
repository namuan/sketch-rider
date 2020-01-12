package dev.deskriders.sketchrider.api;

import dev.deskriders.sketchrider.model.UserDocumentEntity;
import dev.deskriders.sketchrider.renderer.PlantUmlRenderer;
import dev.deskriders.sketchrider.repository.UserDocumentRepository;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.View;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@Secured(SecurityRule.IS_ANONYMOUS)
public class DocumentController {

    private UserDocumentRepository userDocumentRepository;
    private PlantUmlRenderer plantUmlRenderer;

    public DocumentController(UserDocumentRepository userDocumentRepository, PlantUmlRenderer plantUmlRenderer) {
        this.userDocumentRepository = userDocumentRepository;
        this.plantUmlRenderer = plantUmlRenderer;
    }

    @Get(value = "/documents/new")
    public HttpResponse createNewDocument() throws URISyntaxException {
        String documentId = UUID.randomUUID().toString();
        return HttpResponse.temporaryRedirect(new URI("/documents/" + documentId));
    }

    @View(value = "documents/edit")
    @Get(value = "/documents/{documentId}", produces = MediaType.TEXT_HTML)
    public Map<String, Object> editDocument(String documentId, @Nullable Authentication authentication) throws IOException {
        String defaultDocumentCode = "@startuml\nA->B: test\nB->C: hello\n@enduml";
        if (authentication != null) {
            String ownerId = (String) authentication.getAttributes().get("id");
            UserDocumentEntity userDocumentEntity = userDocumentRepository.loadUserDocument(ownerId, documentId);
            if (userDocumentEntity == null) {
                return CollectionUtils.mapOf("documentId", documentId, "documentCode", defaultDocumentCode);
            } else {
                return CollectionUtils.mapOf(
                        "documentId", userDocumentEntity.getDocumentId(),
                        "documentCode", this.plantUmlRenderer.convertToSource(userDocumentEntity.getDocumentCode())
                );
            }
        }
        return CollectionUtils.mapOf("documentId", documentId, "documentCode", defaultDocumentCode);
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @View(value = "documents/list")
    @Get(value = "/documents", produces = MediaType.TEXT_HTML)
    public Map<String, Object> listDocuments(Authentication authentication) {
        String ownerId = (String) authentication.getAttributes().get("id");
        List<UserDocumentEntity> userDocuments = this.userDocumentRepository.listUserDocuments(ownerId);
        return CollectionUtils.mapOf("documents", userDocuments);
    }
}
