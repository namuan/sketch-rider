package dev.deskriders.sketchrider.repository;

import dev.deskriders.sketchrider.api.exception.BadRequestException;
import dev.deskriders.sketchrider.config.DbConfig;
import dev.deskriders.sketchrider.model.UserDiagramEntity;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.time.LocalDateTime;

@Slf4j
@Singleton
public class UserDiagramRepository {
    private DbConfig dbConfig;

    public UserDiagramRepository(DbConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    public String saveUserDiagram(String docId, String userId, String diagramCode) {
        UserDiagramEntity entity = new UserDiagramEntity();
        entity.setId(userId);
        entity.setDocumentId(docId);
        entity.setDiagramCode(diagramCode);
        entity.setCreatedDateTime(LocalDateTime.now());
        dbConfig.dynamoDbMapper().save(entity);
        log.info("Created diagram {} for user {}", docId, userId);
        return docId;
    }

    public void deleteUserDiagram(String ownerId, String docId) {
        UserDiagramEntity entity = new UserDiagramEntity();
        entity.setId(ownerId);
        entity.setDocumentId(docId);
        UserDiagramEntity existingUserDiagramEntity = dbConfig.dynamoDbMapper().load(entity);
        if (existingUserDiagramEntity == null) {
            throw new BadRequestException("Unable to find diagram " + docId);
        }

        dbConfig.dynamoDbMapper().delete(entity);
    }
}
