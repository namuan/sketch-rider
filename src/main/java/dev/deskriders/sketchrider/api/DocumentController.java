package dev.deskriders.sketchrider.api;

import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.deskriders.sketchrider.model.UserDocumentEntity;
import dev.deskriders.sketchrider.renderer.PlantUmlRenderer;
import dev.deskriders.sketchrider.repository.UserDocumentRepository;
import dev.deskriders.sketchrider.util.DocumentCursor;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.View;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import javax.validation.constraints.Null;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Controller
@Secured(SecurityRule.IS_ANONYMOUS)
public class DocumentController {

    private UserDocumentRepository userDocumentRepository;
    private PlantUmlRenderer plantUmlRenderer;
    private ObjectMapper objectMapper;

    public DocumentController(UserDocumentRepository userDocumentRepository, PlantUmlRenderer plantUmlRenderer, ObjectMapper objectMapper) {
        this.userDocumentRepository = userDocumentRepository;
        this.plantUmlRenderer = plantUmlRenderer;
        this.objectMapper = objectMapper;
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
        String defaultDocumentTitle = "Enter title...";

        if (authentication != null) {
            String ownerId = (String) authentication.getAttributes().get("id");
            UserDocumentEntity userDocumentEntity = userDocumentRepository.loadUserDocument(ownerId, documentId);
            if (userDocumentEntity == null) {
                return CollectionUtils.mapOf("documentId", documentId, "documentTitle", defaultDocumentTitle, "documentCode", defaultDocumentCode);
            } else {
                return CollectionUtils.mapOf(
                        "documentId", userDocumentEntity.getDocumentId(),
                        "documentTitle", userDocumentEntity.getDocumentTitle(),
                        "documentCode", this.plantUmlRenderer.convertToSource(userDocumentEntity.getDocumentCode())
                );
            }
        }
        return CollectionUtils.mapOf("documentId", documentId, "documentTitle", defaultDocumentTitle, "documentCode", defaultDocumentCode);
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @View(value = "documents/list")
    @Get(value = "/documents{?nPage}{?dir}", produces = MediaType.TEXT_HTML)
    public Map<String, Object> listDocuments(
            Authentication authentication,
            @Nullable String nPage,
            @Nullable String dir
    ) throws IOException {
        String ownerId = (String) authentication.getAttributes().get("id");
        QueryResultPage<UserDocumentEntity> resultPage = this.userDocumentRepository.listUserDocuments(
                ownerId,
                nPage,
                dir
        );
        Map<String, AttributeValue> lastEvaluatedKey = resultPage.getLastEvaluatedKey();
        String nextPage = null;

        if (lastEvaluatedKey != null) {
            String cursorJson = this.objectMapper.writeValueAsString(lastEvaluatedKey);
            log.info("Cursor JSON: {}", cursorJson);
            if (StringUtils.isNotEmpty(cursorJson)) {
                nextPage = DocumentCursor.compress(cursorJson);
            }
        }
        log.info("Next Page: {}", nextPage);
        return CollectionUtils.mapOf(
                "documents", resultPage.getResults(),
                "next", nextPage
        );
    }
}
