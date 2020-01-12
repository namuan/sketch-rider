package dev.deskriders.sketchrider.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import dev.deskriders.sketchrider.api.exception.BadRequestException;
import dev.deskriders.sketchrider.api.requests.CreateUserDocumentRequest;
import dev.deskriders.sketchrider.config.DbConfig;
import dev.deskriders.sketchrider.model.DocumentStatus;
import dev.deskriders.sketchrider.model.UserDocumentEntity;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Singleton
public class UserDocumentRepository {
    private DbConfig dbConfig;

    public UserDocumentRepository(DbConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    public String saveUserDocument(String ownerId, CreateUserDocumentRequest documentRequest) {
        UserDocumentEntity entity = new UserDocumentEntity();
        entity.setOwnerId(ownerId);
        entity.setDocumentId(documentRequest.getDocumentId());
        entity.setDocumentCode(documentRequest.getDocumentCode());
        entity.setCreatedDateTime(LocalDateTime.now());
        entity.setDocumentType(documentRequest.getDocumentType());
        entity.setDocumentStatus(DocumentStatus.ACTIVE.name());
        dbConfig.dynamoDbMapper().save(entity);
        log.info("Created document {} for user {}", documentRequest.getDocumentId(), ownerId);
        return documentRequest.getDocumentId();
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

    public List<UserDocumentEntity> listUserDocuments(String ownerId) {
        DynamoDBQueryExpression<UserDocumentEntity> queryExpression =
                new DynamoDBQueryExpression<>();
        UserDocumentEntity hkValue = new UserDocumentEntity();
        hkValue.setOwnerId(ownerId);
        queryExpression.withHashKeyValues(hkValue);
        PaginatedQueryList<UserDocumentEntity> queryList = dbConfig.dynamoDbMapper().query(UserDocumentEntity.class, queryExpression);
        return new ArrayList<>(queryList);
    }

    public UserDocumentEntity loadUserDocument(String ownerId, String documentId) {
        UserDocumentEntity entity = new UserDocumentEntity();
        entity.setOwnerId(ownerId);
        entity.setDocumentId(documentId);
        return dbConfig.dynamoDbMapper().load(entity);
    }
}
