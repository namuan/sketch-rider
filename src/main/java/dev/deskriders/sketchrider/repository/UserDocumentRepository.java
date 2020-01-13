package dev.deskriders.sketchrider.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.util.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.deskriders.sketchrider.api.exception.BadRequestException;
import dev.deskriders.sketchrider.api.requests.CreateUserDocumentRequest;
import dev.deskriders.sketchrider.config.DbConfig;
import dev.deskriders.sketchrider.model.DocumentStatus;
import dev.deskriders.sketchrider.model.UserDocumentEntity;
import dev.deskriders.sketchrider.util.DocumentCursor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Singleton
public class UserDocumentRepository {
    private DbConfig dbConfig;

    public UserDocumentRepository(DbConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    public String saveUserDocument(String ownerId, CreateUserDocumentRequest documentRequest) {
//        @todo: Detect if this is called to create a new document
//        or to update an existing document
//        @todo: Use CreatedDate in the SortKey - Doc-{LocalDate}-{UUID}
        UserDocumentEntity entity = new UserDocumentEntity();
        entity.setOwnerId(ownerId);
        entity.setDocumentId(documentRequest.getId());
        entity.setDocumentCode(documentRequest.getCode());
        entity.setCreatedDateTime(LocalDateTime.now());
        entity.setDocumentType(documentRequest.getType());
        entity.setDocumentStatus(DocumentStatus.ACTIVE.name());
        entity.setDocumentTitle(documentRequest.getTitle());

        dbConfig.dynamoDbMapper().save(entity);

        log.info("Created document {} for user {}", documentRequest.getId(), ownerId);
        return documentRequest.getId();
    }

    public void deleteUserDocument(String ownerId, String documentId) {
        UserDocumentEntity existingUserDocumentEntity = loadUserDocument(ownerId, documentId);
        if (existingUserDocumentEntity == null) {
            throw new BadRequestException("Unable to find document " + documentId);
        }
        existingUserDocumentEntity.setDocumentStatus(DocumentStatus.DELETED.name());
        log.info("Deleted document {} for user {}", existingUserDocumentEntity.getDocumentId(), ownerId);
        dbConfig.dynamoDbMapper().save(existingUserDocumentEntity);
    }

    public QueryResultPage<UserDocumentEntity> listUserDocuments(String ownerId, String cursor, String direction) throws IOException {
        UserDocumentEntity hkValue = new UserDocumentEntity();
        hkValue.setOwnerId(ownerId);

        DynamoDBQueryExpression<UserDocumentEntity> queryExpression = new DynamoDBQueryExpression<>();
        queryExpression
                .withHashKeyValues(hkValue)
                .withLimit(10);

        if (StringUtils.isNullOrEmpty(direction) || direction.equals("fwd")) {
            queryExpression.withScanIndexForward(true);
        } else {
            queryExpression.withScanIndexForward(false);
        }

        if (!StringUtils.isNullOrEmpty(cursor)) {
            String decompressedCursor = DocumentCursor.deCompress(cursor);
            Map<String, AttributeValue> cursorValue = new ObjectMapper()
                    .readValue(decompressedCursor, new TypeReference<Map<String, AttributeValue>>() {
                    });
            queryExpression.withExclusiveStartKey(cursorValue);
        }

        return dbConfig.dynamoDbMapper().queryPage(UserDocumentEntity.class, queryExpression);
    }

    public UserDocumentEntity loadUserDocument(String ownerId, String documentId) {
        UserDocumentEntity entity = new UserDocumentEntity();
        entity.setOwnerId(ownerId);
        entity.setDocumentId(documentId);
        return dbConfig.dynamoDbMapper().load(entity);
    }
}
