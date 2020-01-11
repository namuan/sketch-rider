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
        entity.setDocId(docId);
        entity.setOwnerId(userId);
        entity.setDiagramCode(diagramCode);
        entity.setCreatedDateTime(LocalDateTime.now());
        dbConfig.dynamoDbMapper().save(entity);
        log.info("Created diagram {} for user {}", docId, userId);
        return docId;
    }

    public void deleteUserDiagram(String ownerId, String diagramId) {
        UserDiagramEntity entity = new UserDiagramEntity();
        entity.setDocId(diagramId);
        UserDiagramEntity existingUserDiagramEntity = dbConfig.dynamoDbMapper().load(entity);
        if (existingUserDiagramEntity == null || existingUserDiagramEntity.notOwnedBy(ownerId)) {
            throw new BadRequestException("Unable to find diagram " + diagramId);
        }

        dbConfig.dynamoDbMapper().delete(entity);
    }
}
